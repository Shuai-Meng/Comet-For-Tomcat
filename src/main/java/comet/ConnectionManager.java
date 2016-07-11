package comet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletResponse;

public class ConnectionManager {
	
	private List<Connection> connections = new ArrayList<Connection>();
	
	public  void addConn(Connection connection) {
		synchronized(connections) {
			connections.add(connection);
		}
    }
	
	public List<Connection> getConnections() {
		return connections;
	}
}
