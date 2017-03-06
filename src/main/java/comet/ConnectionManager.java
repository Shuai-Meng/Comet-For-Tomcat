package comet;

import java.util.*;

public class ConnectionManager implements Runnable{
	private static final long MAX_INTERVAL = 180000;//3min
	private Map<String, Connection> connections;
	private Queue<String> mq;


	public ConnectionManager() {
		MessageQueue messageQueue = MessageQueue.getMessageQueue();
		mq = messageQueue.getMessage();
	}
	public void setConnections(Map<String, Connection> connections) {
		this.connections = connections;
	}

	public void send(String msg) {
		for(Connection connection : connections.values()) {
			connection.returnResponse(msg);
			connections.remove(connection);
		}
	}

	public void checkTimeOut() {
		//TODO 谁来调用该函数？定时器？
		for(Connection connection : connections.values()) {
			Date createTime = connection.getDate();
			Date now = new Date();
			long length = now.getTime() - createTime.getTime();

			if(length > MAX_INTERVAL)
				connection.returnResponse("");
			connections.remove(connection);
		}
	}

	public void run() {
		while(true) {
			System.out.println(mq.size());
			if(mq.size() > 0) {
				for(String msg: mq) {
					System.out.println(msg);
					send(msg);
				}

			}
		}
	}
}