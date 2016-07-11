package comet;

import java.util.List;

import javax.servlet.*;

public class Constants  implements ServletContextListener {

	private MessageSender messageSender = null;
	private ConnectionManager connectionManager = null;
	private ServletContext sc = null;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		messageSender = null;
		connectionManager = null;
		sc = null;
	}

	public void contextInitialized(ServletContextEvent e) {
		sc = e.getServletContext();
		messageSender = new MessageSender();
		sc.setAttribute("sender", messageSender);
		connectionManager = new ConnectionManager();
		sc.setAttribute("connectionManager", connectionManager);
		
		manage();
	}
	
	private void manage() {
		List<Connection> connections = connectionManager.getConnections();
		
		if(connections.size() == 0)
			try {
				synchronized(this) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		synchronized(connections) {
			messageSender.send(connections);
		}
	}
}
