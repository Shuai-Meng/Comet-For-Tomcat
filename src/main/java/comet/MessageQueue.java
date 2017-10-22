package comet;

import com.fasterxml.jackson.databind.ObjectMapper;
import manage.mapper.MessageMapper;
import manage.mapper.UnreadListMapper;
import manage.vo.Message;
import org.apache.catalina.comet.CometEvent;
import utils.RedisUtil;
import utils.SpringUtil;

import java.io.PrintWriter;
import java.util.*;

public class MessageQueue implements Runnable {
    private static MessageQueue onlyInstance = new MessageQueue();
    private MessageMapper messageMapper;
    private UnreadListMapper unreadListMapper;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<Integer, List<CometEvent>> cometContainer = ConnectionManager.getContainer();

    private MessageQueue() {
        messageMapper = (MessageMapper) SpringUtil.getBean("messageMapper");
        unreadListMapper = (UnreadListMapper) SpringUtil.getBean("unreadListMapper");
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
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("userId", userId);
            map.put("messageId", message.getId());
            unreadListMapper.insert(map);
        }
    }

    private void sendMessage(Map<Integer, List<Message>> map) {
        for (int userId : map.keySet()) {
            List<CometEvent> list = cometContainer.get(userId);
            if (list == null || list.isEmpty()) {
                storeUnreadMessage(userId, map.get(userId));
            } else {
                doSend(list, map.get(userId));
            }
        }
    }

    private void doSend(List<CometEvent> eventList, List<Message> list) {
        try {
            for (CometEvent event : eventList) {
                PrintWriter writer = event.getHttpServletResponse().getWriter();
                writer.println(objectMapper.writeValueAsString(list));
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getUserIdOfType(int type) {
        return messageMapper.getUserIdOfType(type);
    }

    public void run() {
        while(true) {
            synchronized (this) {
                List<Object> messageList = RedisUtil.lrange(Constants.SENDING_LIST, 0,
                        Constants.LIST_VOLUME);
                if(messageList.size() == 0) {
                    try {
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