package comet;

import constants.Constants;
import manage.service.MessageService;
import manage.service.TypeService;
import manage.vo.MyMessage;
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
    private TypeService typeService;
    private MessageService messageService;

    private MessageQueue() {
        typeService = (TypeService) SpringUtil.getBean("typeServiceImpl");
        messageService = (MessageService) SpringUtil.getBean("messageServiceImpl");
    }

    public static MessageQueue getSingleInstance() {
        return onlyInstance;
    }

    private Map<Integer, List<MyMessage>> distributeMessage(List<Object> messageList) {
        Map<Integer, List<MyMessage>> messageMap = new HashMap<Integer, List<MyMessage>>();
        List<Future<Map<Integer, List<MyMessage>>>> futures = new ArrayList<Future<Map<Integer, List<MyMessage>>>>();

        for (Object object : messageList) {
            MyMessage message = (MyMessage)object;
            LOG.info("message: " + message.getTitle());
            List<Integer> userList = typeService.getSubscribed(message.getType());
            for (List<Integer> list : getListGroup(userList)) {
                futures.add(THREAD_POOL.submit(new DistributeMessage(list, message)));
            }

            messageService.markAsPublished(message.getId());
        }

        for (Future<Map<Integer, List<MyMessage>>> future : futures) {
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

    private void sendMessage(Map<Integer, List<MyMessage>> messageMap) {
        List<Integer> keyList = new ArrayList<Integer>(messageMap.keySet());
        for (List<Integer> list : getListGroup(keyList)) {
            THREAD_POOL.execute(new SendMessage(list, messageMap, messageService));
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
                    Map<Integer, List<MyMessage>> map = distributeMessage(messageList);
                    LOG.info("msgMapSize: " + map.size());
                    sendMessage(map);
                    RedisUtil.ltrim(Constants.SENDING_LIST, Constants.LIST_VOLUME, -1);
                }
            }
        }
    }
}