package comet;

import manage.mapper.MessageMapper;
import manage.vo.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by m on 17-8-22.
 */
@Component
public class DelayedMessageHandler {
    @Resource
    private MessageMapper messageMapper;

    private List<Message> getMessageFromDataBase(Date date) {
        return messageMapper.getMessagesOfThisMin(date);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void pushMessageToQueue() throws InterruptedException {
        for (Message message : getMessageFromDataBase(new Date())) {
            MessageQueue.getSingleInstance().addMessage(message);
        }
    }
}
