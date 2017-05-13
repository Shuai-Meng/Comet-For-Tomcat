package comet;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * Created by m on 17-5-12.
 */
public class MessageManager implements Runnable {
    private List<Message> list;
    private static MessageQueue messageQueue;

    static {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        messageQueue = (MessageQueue)servletContext.getAttribute("messageQueue");
    }

    private synchronized void getMessageFromDataBase() {
        list = new ArrayList<Message>();
        try {
            wait();
        } catch (InterruptedException e) {

        }
    }

    private void checkTime() {
        for(Message message : list) {
            long internal = message.getDate().getTime() - System.currentTimeMillis();

            if(internal < 60000 || internal > -60000)
                messageQueue.addMessage(message);

            list.remove(message);
            resetTime(message);
        }
    }

    private void resetTime(Message message) {

    }

    public void run() {
        while (true) {
            if(list == null || list.size() == 0)
                getMessageFromDataBase();
            else
                checkTime();
        }
    }
}
