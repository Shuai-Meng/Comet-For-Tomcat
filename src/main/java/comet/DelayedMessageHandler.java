package comet;

import constants.Constants;
import manage.mapper.MessageMapper;
import manage.vo.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import utils.*;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * @author mengshuai
 * @date 17-8-22
 */
@Component
public class DelayedMessageHandler {
    @Resource
    private MessageMapper messageMapper;

    private List<Message> getMessageFromDataBase() {
        return messageMapper.getMessagesOfThisMin(TimeUtil.getNow());
    }

    @Scheduled(cron = "0 * * * * ?")
    public void pushMessageToQueue() throws InterruptedException {
        for (Message message : getMessageFromDataBase()) {
            RedisUtil.lpush(Constants.SENDING_LIST, message);
        }
    }
}
