package comet;

import javax.servlet.*;

public class Constants  implements ServletContextListener {

	private ConnectionManager connectionManager;
	private ServletContext sc;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		connectionManager = null;
		sc = null;
	}

	public void contextInitialized(ServletContextEvent e) {
		sc = e.getServletContext();
		
		connectionManager = new ConnectionManager();
		sc.setAttribute("connectionManager", connectionManager);
	}
}
