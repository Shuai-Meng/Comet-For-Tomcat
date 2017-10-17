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

    private MessageQueue() {
        messageMapper = (MessageMapper) SpringUtil.getBean("messageMapper");
        unreadListMapper = (UnreadListMapper) SpringUtil.getBean("unreadListMapper");
    }

    public static MessageQueue getSingleInstance() {
        return onlyInstance;
    }

    //TODO multi thread for efficiency
    //TODO 通信机制有点复杂 多个即时消息发送同一用户，链接已断开；一个链接只能发送一条消息
    private void sendMessage(List<Object> messageList) {
        for (Object object : messageList) {
            Message message = (Message)object;
            for(int userId : getUserIdOfType(message.getType())) {
                CometEvent event = ConnectionManager.getContainer().get(userId);

                if (event != null) {
                    doSend(event, message);
                } else {//means the user is offline
                    storeUnreadMessage(userId, message.getId());
                }
            }
        }
    }

    private void storeUnreadMessage(int userId, int messageId) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("userId", userId);
        map.put("messageId", messageId);
        unreadListMapper.insert(map);
    }

    private void doSend(CometEvent event, Message message) {
        try {
            PrintWriter writer = event.getHttpServletResponse().getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            writer.println(objectMapper.writeValueAsString(message));
            writer.flush();
            writer.close();
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
                    sendMessage(messageList);
                }

                RedisUtil.ltrim(Constants.SENDING_LIST, Constants.LIST_VOLUME, -1);
            }
        }
    }
}