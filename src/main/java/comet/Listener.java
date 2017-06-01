package comet;

import javax.servlet.*;

public class Listener  implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent e) {
		new Thread(MessageQueue.getMessageQueue()).start();
        new Thread(new MessageManager()).start();
	}
}
