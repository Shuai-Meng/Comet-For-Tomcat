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

	@Override public void init() {
		container = ConnectionManager.getContainer();
	}

	@Override
	public void event(CometEvent event) throws IOException, ServletException {
		int userId = getUserID(event);
		CometEvent old = container.get(userId);
		if (old != null) {
			old.close();
		}

		switch (event.getEventType()) {
			case BEGIN:
				//TODO customize
				event.setTimeout(60000);
				container.put(userId, event);
				break;
			case READ:
				throw new UnsupportedOperationException("This servlet does not accept data");
			case ERROR:
				if (event.getEventSubType() == CometEvent.EventSubType.TIMEOUT) {
					event.getHttpServletResponse().setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
				}
				event.close();
				container.remove(userId);
				break;
			case END:
				event.close();
				container.remove(userId);
				break;
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