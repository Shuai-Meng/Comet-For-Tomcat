package comet;

import org.apache.catalina.comet.CometEvent;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Connection {

	private CometEvent event;

	public CometEvent getEvent() {
		return event;
	}

	public void setEvent(CometEvent event) {
		this.event = event;
	}

	public void returnResponse(String msg) {
		PrintWriter writer;
		try {
			writer = event.getHttpServletResponse().getWriter();
			writer.println(msg);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}