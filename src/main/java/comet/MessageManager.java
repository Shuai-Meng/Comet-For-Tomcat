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
    @Autowired
    MessageMapper messageMapper;

    private void getLatestMessages() {
        list.addAll(messageMapper.getLatestMessages());
    }

    private void pushMessageToQueue() {
        for(Message message : list)
            MessageQueue.getMessageQueue().addMessage(message);

        list.clear();
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
