package comet;

import java.util.*;

public class MessageQueue implements Runnable {
	private volatile static MessageQueue messageQueue;
	private List<String> mq = new LinkedList<String>();

	private MessageQueue() { }

	public static MessageQueue getMessageQueue() {
		if(messageQueue == null) {
			synchronized (MessageQueue.class) {
				if(messageQueue == null)
					messageQueue = new MessageQueue();
			}
		}

		return messageQueue;
	}

	public void addMessage(String msg) {
		mq.add(msg);
	}

    public void run() {
        if(mq.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        } else {
            for(String msg: mq) {
                Map<String, Connection> container = Container.getContainer();
                for(Connection connection : container.values()) {
                    System.out.println(msg);
                    connection.returnResponse(msg);
                }
            }
        }
    }
}