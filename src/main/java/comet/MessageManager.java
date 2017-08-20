package comet;

import manage.dao.MessageMapper;
import manage.vo.Message;

import java.util.*;

/**
 * Created by m on 17-5-12.
 */
public class MessageManager implements Runnable {
    private List<Message> list = new ArrayList<Message>();
    MessageMapper messageMapper;

    private void getLatestMessages() {
        list.addAll(messageMapper.getLatestMessages());
    }

    private void pushMessageToQueue() {
        for(Message message : list)
            MessageQueue.getOnlyInstance().addMessage(message);

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
