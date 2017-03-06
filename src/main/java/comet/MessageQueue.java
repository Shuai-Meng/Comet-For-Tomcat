package comet;

import java.util.*;

public class MessageQueue {
	private volatile static MessageQueue messageQueue;
	private Queue<String> mq = new LinkedList<String>();

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

	public synchronized void addMessage(String msg) {
		mq.add(msg);
		System.out.println(msg);
		notifyAll();
	}

	public  Queue<String> getMessage() {
		return mq;
	}
}