package comet;

import java.util.concurrent.*;

import javax.servlet.*;

public class Listener  implements ServletContextListener, Runnable {

	private ConnectionManager connectionManager;
	private ServletContext sc;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		connectionManager = null;
		sc = null;
	}

	public void contextInitialized(ServletContextEvent e) {
		connectionManager = new ConnectionManager();
		sc = e.getServletContext();
		sc.setAttribute("connectionManager", connectionManager);
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(this, 20, 60, TimeUnit.SECONDS);
	}

	public void run() {
		connectionManager.checkTimeOut();
	}
}
