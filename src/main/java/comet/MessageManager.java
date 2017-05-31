package comet;

import manage.dao.MessageMapper;
import manage.vo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * Created by m on 17-5-12.
 */
public class MessageManager implements Runnable {
    private List<Message> list = new ArrayList<Message>();
    private static MessageQueue messageQueue;
    @Autowired
    MessageMapper messageMapper;

    static {
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        messageQueue = (MessageQueue)servletContext.getAttribute("messageQueue");
    }

    private void getLatestMessages() {
        list.addAll(messageMapper.getLatestMessages());
    }

    private void pushMessageToQueue() {
        for(Message message : list) {
            messageQueue.addMessage(message);
            list.remove(message);
        }
    }

    public void run() {
        while (true) {
            if(list.size() == 0)
                getLatestMessages();
            else
                pushMessageToQueue();
        }
    }
}
