package manage.service.impl;

import comet.Constants;
import manage.mapper.MessageMapper;
import manage.service.MessageService;
import manage.vo.Message;
import org.springframework.stereotype.Service;
import utils.RedisUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mengshuai
 */
@Service
public class MessageServiceImpl extends BaseService implements MessageService {
    @Resource
    private MessageMapper messageMapper;

    @Override public void addMessage(Message message) {
        message.setPublisher(getUser().getUsername());
        messageMapper.insertMessage(message);

        if("1".equals(message.getImmediate())) {
            message.setSendTime(new Date());
            RedisUtil.lpush(Constants.SENDING_LIST, message);
        }
    }

    @Override public List<Message> getUnreadMessages() {
        return messageMapper.getUnread(getUser().getUserId());
    }

    @Override public void deleteUnreandMessage(int messageId) {
        Map<String, Integer> map = new HashMap<String, Integer>(2);
        map.put("userId", getUser().getUserId());
        map.put("messageId", messageId);
        messageMapper.deleteUnread(map);
    }

    @Override public void modifyMessage(Message message, String operation) {
        if ("delete".equals(operation)) {
            messageMapper.deleteMessage(message);
        } else {
            message.setPublisher(getUser().getUsername());
            messageMapper.updateMessage(message);
        }
    }

    @Override public Map<String, Object> getMessage(String name, String page, String rows) {
        Map<String, Object> res = new HashMap<String, Object>(2);

        Map<String, Object> param = getPagination(page, rows);
        param.put("name", name);
        res.put("rows", messageMapper.getRows(param));
        res.put("total", messageMapper.getCount(name));
        return res;
    }
}
