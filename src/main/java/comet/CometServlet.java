package comet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;

public class CometServlet extends HttpServlet  implements CometProcessor {
	 private static final long serialVersionUID = 1L;
	 
	 public void event(CometEvent event) throws IOException, ServletException {
         if (event.getEventType() == CometEvent.EventType.BEGIN) {
        	Connection connection = new Connection();
        	connection.setRequest(event.getHttpServletRequest());
        	connection.setDate(new Date());
        	connection.setResponse(event.getHttpServletResponse());
        	
        	ServletContext sc = getServletContext();
        	ConnectionManager connectionManager = (ConnectionManager) sc.getAttribute("connectionManager");
        	connectionManager.addConnection(connection);
        	
        	try {
        		synchronized (connection) {
        			connection.wait();
        		}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        } else if (event.getEventType() == CometEvent.EventType.ERROR) {
            event.close();
        } else if (event.getEventType() == CometEvent.EventType.END) {
            event.close();
        } else if (event.getEventType() == CometEvent.EventType.READ) {
            throw new UnsupportedOperationException("This servlet does not accept data");
        }
	}
}
