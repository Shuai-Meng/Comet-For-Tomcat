package comet;

import javax.servlet.*;

public class Constants  implements ServletContextListener {

	private MessageSender messageSender = null;
	private ServletContext sc = null;
	
	public void contextDestroyed(ServletContextEvent arg0) {
		messageSender = null;
	}

	public void contextInitialized(ServletContextEvent e) {
		sc = e.getServletContext();
		messageSender = new MessageSender();
		sc.setAttribute("sender", messageSender);
	}
}
