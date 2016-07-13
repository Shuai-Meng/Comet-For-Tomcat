package comet;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager{
	
	private Map<String, Connection> connections = new ConcurrentHashMap<String, Connection>();
	
	public  void addConnection(Connection connection) {
			connections.put(connection.getRequest().getRequestedSessionId(), connection);
    }
	
	public void send(String msg) {
    	for(Connection connection : connections.values()) {
//    		System.out.println(connection.getRequest().getRequestedSessionId());
    		connection.returnResponse(msg);
    		connections.remove(connection);
		 }
	}
}
