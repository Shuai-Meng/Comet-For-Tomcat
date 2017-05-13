package comet;

import java.util.*;

public class MessageQueue implements Runnable {
    private List<Message> messageQuque;
    Map<String, Connection> container = Container.getContainer();

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
            for(Connection connection: container.values()) {
                connection.returnResponse(message.getContent());
            }
        }
        messageQuque.clear();
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