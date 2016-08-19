package comet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager{
	private static final long MAX_INTERVAL = 180000;//3min
	private Map<String, Connection> connections = new ConcurrentHashMap<String, Connection>();
	
	public  void addConnection(Connection connection) {
			connections.put(connection.getRequest().getRequestedSessionId(), connection);
    }
	
	public void send(String msg) {
		//TODO 循环是不是太慢了？connections本身是ConcurrentHashMap
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
}
