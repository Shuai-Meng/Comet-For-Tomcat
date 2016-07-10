package comet;

import java.io.*;
import java.util.*;

import javax.servlet.*;

public class MessageSender implements Runnable {
	
    private ServletResponse connection;
    private  String message = null;
    
    public void setConn(ServletResponse connection) {
    	this.connection = connection;
    }
    
    public  void setMsg(String message){
    	this.message = message;
    	System.out.println(message);
    	synchronized (this) {
    		notify();
		}
    }
    
    public void send() {
			PrintWriter writer = null;
			try {
				writer = connection.getWriter();
				writer.println(message);
	            writer.flush();
	            writer.close();
	            message = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

	public void run() {
			if(this.message == null)
				try {
					synchronized (this) {
						wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				send();
		}
}
