package comet;

import java.util.*;

/**
 * Created by m on 17-5-10.
 */
public class ImmediateQueue implements Runnable{
    private List<Message> messageQuque;

    public ImmediateQueue() {
        this.messageQuque = new ArrayList<Message>();
    }

    public synchronized void addMessage(Message message) {
//        if(messageQuque.size() >= 100)
//            sendMessage();

//        System.out.println(message.getContent());
        messageQuque.add(message);
        notifyAll();
    }


    private void sendMessage() {

        Map<String, Connection> container = Container.getContainer();
        for(Message message : messageQuque) {
            System.out.println("sending...");
            for(Connection connection: container.values()) {
                System.out.println("LLL:"+message.getContent());
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
                        System.out.println("waiting...");
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
