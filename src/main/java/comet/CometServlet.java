package comet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;

public class CometServlet extends HttpServlet  implements CometProcessor {
	 private static final long serialVersionUID = 1L;
	 
	 public void event(CometEvent event) throws IOException, ServletException {
         if (event.getEventType() == CometEvent.EventType.BEGIN) {
        	ServletContext sc = getServletContext();
        	ConnectionManager connectionManager = (ConnectionManager) sc.getAttribute("connectionManager");
        	
        	Connection connection = new Connection();
        	connection.setRequest(event.getHttpServletRequest());
        	connectionManager.addConnection(connection);
        	
        	connection.setResponse(event.getHttpServletResponse());
        } else if (event.getEventType() == CometEvent.EventType.ERROR) {
            event.close();
        } else if (event.getEventType() == CometEvent.EventType.END) {
            event.close();
        } else if (event.getEventType() == CometEvent.EventType.READ) {
            throw new UnsupportedOperationException("This servlet does not accept data");
        }
	}
}
