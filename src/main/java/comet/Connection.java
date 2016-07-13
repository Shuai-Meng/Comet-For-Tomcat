package comet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Connection {
	
	private HttpServletResponse response;
	private HttpServletRequest request;
	
	public synchronized void returnResponse(String msg) {
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.println(msg);
            writer.flush();
            writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		notify();
	}
	public synchronized void setResponse(HttpServletResponse response) {
		this.response = response;
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
}
