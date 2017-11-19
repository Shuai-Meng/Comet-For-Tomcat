package comet;

import constants.Constants;
import manage.mapper.MessageMapper;
import manage.mapper.TypeMapper;
import manage.service.RecordService;
import manage.vo.Message;
import org.slf4j.*;
import utils.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static constants.Constants.N_CPU;
import static comet.ThreadStarter.THREAD_POOL;

/**
 * @author mengshuai
 */
public class MessageQueue implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MessageQueue.class);
    private static MessageQueue onlyInstance = new MessageQueue();
    private TypeMapper    typeMapper;
    private MessageMapper messageMapper;
    private RecordService recordService;

    private MessageQueue() {
        typeMapper = (TypeMapper) SpringUtil.getBean("typeMapper");
        messageMapper = (MessageMapper) SpringUtil.getBean("messageMapper");
        recordService = (RecordService) SpringUtil.getBean("recordServiceImpl");
    }

    public static MessageQueue getSingleInstance() {
        return onlyInstance;
    }

    private Map<Integer, List<Message>> distributeMessage(List<Object> messageList) {
        Map<Integer, List<Message>> messageMap = new HashMap<Integer, List<Message>>();
        List<Future<Map<Integer, List<Message>>>> futures = new ArrayList<Future<Map<Integer, List<Message>>>>();

        for (Object object : messageList) {
            Message message = (Message)object;
            LOG.info("message: " + message.getTitle());
            List<Integer> userList = typeMapper.getUserIdOfType(message.getType());
            for (List<Integer> list : getListGroup(userList)) {
                futures.add(THREAD_POOL.submit(new DistributeMessage(list, message)));
            }

            messageMapper.changeSendStatus(message.getId());
        }

        for (Future<Map<Integer, List<Message>>> future : futures) {
            try {
                messageMap.putAll(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return messageMap;
    }

    private void sendMessage(Map<Integer, List<Message>> messageMap) {
        List<Integer> keyList = new ArrayList<Integer>(messageMap.keySet());
        for (List<Integer> list : getListGroup(keyList)) {
            THREAD_POOL.execute(new SendMessage(list, messageMap, messageMapper, recordService));
        }
    }

    private List<List<Integer>> getListGroup(List<Integer> originalList) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();

        int span = originalList.size() / N_CPU;
        int location = 0;
        while (location < originalList.size()) {
            int toIndex = location + span;
            toIndex = toIndex > originalList.size() ? originalList.size() : toIndex;

            result.add(originalList.subList(location, toIndex));
            location = location + span;
        }
        return result;
    }

    @Override public void run() {
        while(true) {
            synchronized (this) {
                List<Object> messageList = RedisUtil.lrange(Constants.SENDING_LIST, 0,
                        Constants.LIST_VOLUME);
                if(messageList.size() == 0) {
                    try {
                        LOG.info("no message, waiting...");
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Map<Integer, List<Message>> map = distributeMessage(messageList);
                    LOG.info("msgMapSize: " + map.size());
                    sendMessage(map);
                    RedisUtil.ltrim(Constants.SENDING_LIST, Constants.LIST_VOLUME, -1);
                }
            }
        }
    }
}