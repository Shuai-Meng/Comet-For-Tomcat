package comet;

import constants.Constants;
import manage.mapper.MessageMapper;
import manage.vo.MyMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
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

    private List<MyMessage> getMessageFromDataBase() {
        Example example = new Example(MyMessage.class);
        example.createCriteria().andEqualTo("sendTime", TimeUtil.getNow());
        return messageMapper.selectByExample(example);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void pushMessageToQueue() throws InterruptedException {
        for (MyMessage message : getMessageFromDataBase()) {
            RedisUtil.lpush(Constants.SENDING_LIST, message);
        }
    }
}
