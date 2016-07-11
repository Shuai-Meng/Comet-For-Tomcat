package comet;

import java.io.*;
import java.util.*;

import javax.servlet.*;

public class MessageSender {
	
	private Queue<String> mq = new LinkedList<String>();
	String msg = null;
	
	public void addMessage(String msg) {
//		mq.add(msg);
//		System.out.println("mq:"+mq);
		this.msg = msg;
		synchronized (this) {
			notify();
		}
	}
	
    public void send(List<Connection> connections) {
    	if(msg == null)
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	
    	for(Connection connection : connections) {
    		System.out.println(connection.getRequest().getRequestedSessionId());
			 writeResponse(connection.getResponse());
			 connections.remove(connection);
		 }
//		 mq.clear();
	}

    private void writeResponse(ServletResponse connection) {
    	PrintWriter writer = null;
		try {
			writer = connection.getWriter();
//			for(String s : mq)
			writer.println(msg);
            writer.flush();
            writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
