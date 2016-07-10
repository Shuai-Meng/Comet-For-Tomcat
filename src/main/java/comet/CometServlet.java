package comet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;

public class CometServlet extends HttpServlet  implements CometProcessor {
	 private static final long serialVersionUID = 1L;
	 
	 public void event(CometEvent event) throws IOException, ServletException {
         if (event.getEventType() == CometEvent.EventType.BEGIN) {
        	HttpServletResponse response = event.getHttpServletResponse();
        	ServletContext sc = getServletContext();
        	MessageSender messageSender = (MessageSender) sc.getAttribute("sender");
        	messageSender.setConn(response);
        	messageSender.run();
        } else if (event.getEventType() == CometEvent.EventType.ERROR) {
            event.close();
        } else if (event.getEventType() == CometEvent.EventType.END) {
            event.close();
        } else if (event.getEventType() == CometEvent.EventType.READ) {
            throw new UnsupportedOperationException("This servlet does not accept data");
        }
	}
}
