package comet;

import com.fasterxml.jackson.databind.ObjectMapper;
import manage.dao.MessageMapper;
import manage.vo.Message;
import org.apache.catalina.comet.CometEvent;
import utils.SpringUtil;

import java.io.PrintWriter;
import java.util.*;

public class MessageQueue implements Runnable {

    private static MessageQueue onlyInstance = new MessageQueue();
    private MessageMapper messageMapper;
    private List<Message> messageList;

    private MessageQueue() {
        messageList = new ArrayList<Message>();
        messageMapper = (MessageMapper) SpringUtil.getBean("messageMapper");
    }

    public static MessageQueue getOnlyInstance() {
        return onlyInstance;
    }

    public synchronized void addMessage(Message message) {
        if(messageList.size() >= 100)
            sendMessage();

        messageList.add(message);
        notifyAll();
    }

    private void sendMessage() {
        for(Message message : messageList) {
            System.out.println("sending...");
            List<Integer> userList = getUserIdOfType(message.getType());
            for(int userId : userList) {
                CometEvent event = Container.getContainer().get(userId);
                if (event != null) {
                    doSend(event, message);
                } else {

                }
            }
//            updateMessage(message);
        }
        messageList.clear();
    }

    private void doSend(CometEvent event, Message message) {
        try {
            PrintWriter writer = event.getHttpServletResponse().getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.println(objectMapper.writeValueAsString(message));
            writer.println(objectMapper.writeValueAsString(message));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println(Thread.currentThread().isAlive());
            e.printStackTrace();
        }
    }

    private void updateMessage(Message message) {
        message.setValid("0");
        messageMapper.updateMessage(message);
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