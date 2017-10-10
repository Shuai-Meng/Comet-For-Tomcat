package comet;

import com.fasterxml.jackson.databind.ObjectMapper;
import manage.mapper.MessageMapper;
import manage.mapper.UnreadListMapper;
import manage.vo.Message;
import org.apache.catalina.comet.CometEvent;
import utils.SpringUtil;

import java.io.PrintWriter;
import java.util.*;

public class MessageQueue implements Runnable {
    private static MessageQueue onlyInstance = new MessageQueue();
    private MessageMapper messageMapper;
    private UnreadListMapper unreadListMapper;
    private List<Message> messageList;

    private MessageQueue() {
        messageList = new ArrayList<Message>();
        messageMapper = (MessageMapper) SpringUtil.getBean("messageMapper");
        unreadListMapper = (UnreadListMapper) SpringUtil.getBean("unreadListMapper");
    }

    public static MessageQueue getSingleInstance() {
        return onlyInstance;
    }

    public synchronized void addMessage(Message message) {
        //TODO redis
        if(messageList.size() >= 100)
            sendMessage();

        messageList.add(message);
        notifyAll();
    }

    //TODO multi thread for efficiency
    private void sendMessage() {
        for(Message message : messageList) {
            for(int userId : getUserIdOfType(message.getType())) {
                CometEvent event = ConnectionManager.getContainer().get(userId);

                if (event != null) {
                    doSend(event, message);
                } else {//means the user is offline
                    storeUnreadMessage(userId, message.getId());
                }
            }
        }
        messageList.clear();
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
                if(messageList.size() == 0) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else
                    sendMessage();
            }
        }
    }
}