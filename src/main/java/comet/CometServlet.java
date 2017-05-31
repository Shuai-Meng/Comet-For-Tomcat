package comet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;
import utils.SpringSecurityUtil;


public class CometServlet extends HttpServlet  implements CometProcessor {
	private Map<Integer, CometEvent> container;
	private Set<String> session;

	public void init() {
		container = Container.getContainer();
		session = Container.getSession();
	}

	public void event(CometEvent event) throws IOException, ServletException {
		int userId = SpringSecurityUtil.getCurrentUser().getUserId();

		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			event.setTimeout(15000);
			saveConnection(userId, event);
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			event.close();
            container.remove(userId);
		} else if (event.getEventType() == CometEvent.EventType.END) {
			event.close();
            container.remove(userId);
		} else if (event.getEventType() == CometEvent.EventType.READ) {
			throw new UnsupportedOperationException("This servlet does not accept data");
		}
	}

	private void saveConnection(int userId, CometEvent event) {
		if(!checkExists(event)) {
			container.put(userId, event);
			session.add(event.getHttpServletRequest().getRequestedSessionId());
		}
	}

	private boolean checkExists(CometEvent event) {
		if(session.contains(event.getHttpServletRequest().getRequestedSessionId())) {
			PrintWriter writer = null;
			try {
				writer = event.getHttpServletResponse().getWriter();
				writer.println("exists");
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}