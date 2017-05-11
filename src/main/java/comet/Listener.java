package comet;

import java.util.concurrent.*;

import javax.servlet.*;

public class Listener  implements ServletContextListener{
	private ServletContext sc;

	public void contextDestroyed(ServletContextEvent arg0) {
		sc = null;
	}

	public void contextInitialized(ServletContextEvent e) {
		sc = e.getServletContext();
		ImmediateQueue  immediateQueue = new ImmediateQueue();
		sc.setAttribute("immediateQueue", immediateQueue);
		new Thread(immediateQueue).start();
	}
}
