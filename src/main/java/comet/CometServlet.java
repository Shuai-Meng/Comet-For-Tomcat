package comet;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;

public class CometServlet extends HttpServlet  implements CometProcessor {
	private static final long serialVersionUID = 1L;
	private Map<String, Connection> container;

	public void init() {
		container = Container.getContainer();
	}

	public void event(CometEvent event) throws IOException, ServletException {

		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			Connection connection = new Connection();
            event.setTimeout(15000);
			connection.setEvent(event);

			container.put(event.getHttpServletRequest().getRequestedSessionId(), connection);
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			event.close();
            container.remove(event);
		} else if (event.getEventType() == CometEvent.EventType.END) {
			event.close();
            container.remove(event);
		} else if (event.getEventType() == CometEvent.EventType.READ) {
			throw new UnsupportedOperationException("This servlet does not accept data");
		}
	}
}