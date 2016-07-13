package comet;

import java.util.*;

public class MessageQueue {
		
	private Queue<String> mq = new LinkedList<String>();
	
	public synchronized void addMessage(String msg) {
		mq.add(msg);
	}
	
	public synchronized Queue<String> getMessage() {
		return mq;
	}
}
