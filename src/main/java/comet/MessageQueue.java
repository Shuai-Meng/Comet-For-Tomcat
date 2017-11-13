package comet;

import com.fasterxml.jackson.databind.ObjectMapper;
import manage.mapper.MessageMapper;
import manage.mapper.TypeMapper;
import manage.vo.Message;
import org.apache.catalina.comet.CometEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RedisUtil;
import utils.SpringSecurityUtil;
import utils.SpringUtil;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author mengshuai
 */
public class MessageQueue implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MessageQueue.class);

    private static MessageQueue onlyInstance = new MessageQueue();
    private MessageMapper messageMapper;
    private TypeMapper typeMapper;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<Integer, List<CometEvent>> cometContainer = ConnectionManager.getContainer();

    private MessageQueue() {
        messageMapper = (MessageMapper) SpringUtil.getBean("messageMapper");
        typeMapper = (TypeMapper) SpringUtil.getBean("typeMapper");
    }

    public static MessageQueue getSingleInstance() {
        return onlyInstance;
    }

    private Map<Integer, List<Message>> dispatchMessage(List<Object> messageList) {
        Map<Integer, List<Message>> map = new HashMap<Integer, List<Message>>();

        for (Object object : messageList) {
            Message message = (Message)object;

            for(int userId : getUserIdOfType(message.getType())) {
                List<Message> tmp = map.get(userId);

                if (tmp == null) {
                    tmp = new ArrayList<Message>();
                    map.put(userId, tmp);
                }

                tmp.add(message);
            }
        }

        return map;
    }

    private void storeUnreadMessage(int userId, List<Message> list) {
        for (Message message : list) {
            Map<String, Integer> map = new HashMap<String, Integer>(2);
            map.put("userId", userId);
            map.put("messageId", message.getId());
            messageMapper.insertUnread(map);
        }
    }

    private void sendMessage(Map<Integer, List<Message>> map) {
        for (int userId : map.keySet()) {
            LOG.info("sending for user: " + userId);
            List<CometEvent> list = cometContainer.get(userId);

            if (list == null || list.isEmpty()) {
                LOG.info(userId + " is offline");
                storeUnreadMessage(userId, map.get(userId));
            } else {
                LOG.info("user: {} has {} connections", userId, list.size());
                doSend(list, map.get(userId));
            }
        }
    }

    private void doSend(List<CometEvent> eventList, List<Message> list) {
        try {
            for (CometEvent event : eventList) {
                ServletResponse response = event.getHttpServletResponse();
                response.setCharacterEncoding("UTF-8");

                PrintWriter writer = response.getWriter();
                writer.println(objectMapper.writeValueAsString(list));
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getUserIdOfType(int type) {
        return typeMapper.getUserIdOfType(type);
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
                    Map<Integer, List<Message>> map = dispatchMessage(messageList);
                    sendMessage(map);
                    RedisUtil.ltrim(Constants.SENDING_LIST, Constants.LIST_VOLUME, -1);
                }
            }
        }
    }
}