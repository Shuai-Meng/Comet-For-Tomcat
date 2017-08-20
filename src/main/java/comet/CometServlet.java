package comet;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import manage.vo.SecurityUser;
import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometProcessor;
import org.springframework.security.core.context.SecurityContext;

public class CometServlet extends HttpServlet  implements CometProcessor {
	private Map<Integer, CometEvent> container;

	public void init() {
		container = Container.getContainer();
	}

	public void event(CometEvent event) throws IOException, ServletException {
		int userId = getUserID(event);

		if (event.getEventType() == CometEvent.EventType.BEGIN) {
			event.setTimeout(60000);
			container.put(userId, event);
		} else if (event.getEventType() == CometEvent.EventType.ERROR) {
			if (event.getEventSubType() == CometEvent.EventSubType.TIMEOUT) {
			    event.getHttpServletResponse().setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
			}
			event.close();
			container.remove(userId);
		} else if (event.getEventType() == CometEvent.EventType.END) {
			event.close();
            container.remove(userId);
		} else if (event.getEventType() == CometEvent.EventType.READ) {
			throw new UnsupportedOperationException("This servlet does not accept data");
		}
	}

	private int getUserID(CometEvent event) {
		HttpServletRequest httpServletRequest = event.getHttpServletRequest();
		HttpSession httpSession = httpServletRequest.getSession(false);
		SecurityContext securityContext = (SecurityContext) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
		Object principal = securityContext.getAuthentication().getPrincipal();
		return ((SecurityUser) principal).getUserId();
	}
}