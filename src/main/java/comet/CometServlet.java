package comet;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;

public class CometServlet extends HttpServlet  implements CometProcessor {
	private static final long serialVersionUID = 1L;
	private Map<String, Connection> container;

	public void init() {
		container = Container.getContainer();
	}

	public void event(CometEvent event) throws IOException, ServletException {
		event.setTimeout(600000);
		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			Connection connection = new Connection();
			connection.setRequest(event.getHttpServletRequest());
			connection.setDate(new Date());
			connection.setResponse(event.getHttpServletResponse());

			container.put(event.getHttpServletRequest().getRequestedSessionId(), connection);
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.END) {
			event.close();
		} else if (event.getEventType() == CometEvent.EventType.READ) {
			throw new UnsupportedOperationException("This servlet does not accept data");
		}
	}
}