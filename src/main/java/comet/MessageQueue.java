package comet;

import manage.dao.MessageMapper;
import manage.vo.Message;
import org.apache.catalina.comet.CometEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class MessageQueue implements Runnable {
    @Autowired
    MessageMapper messageMapper;
    private List<Message> messageQuque;

    public MessageQueue() {
        this.messageQuque = new ArrayList<Message>();
    }

    public synchronized void addMessage(Message message) {
        if(messageQuque.size() >= 100)
            sendMessage();

        messageQuque.add(message);
        notifyAll();
    }

    private void sendMessage() {
        for(Message message : messageQuque) {
            System.out.println("sending...");
            List<Integer> userList = getUserIdOfType(message.getType());
            for(int userId : userList) {
                CometEvent event = Container.getContainer().get(userId);
                PrintWriter writer;
                try {
                    writer = event.getHttpServletResponse().getWriter();
                    writer.println(message);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            updateMessage(message);
        }
        messageQuque.clear();
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
                if(messageQuque.size() == 0) {
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