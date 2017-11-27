package manage.service.impl;

import constants.Constants;
import manage.mapper.MessageMapper;
import manage.service.MessageService;
import manage.service.RecordService;
import manage.vo.Message;
import org.springframework.stereotype.Service;
import utils.RedisUtil;
import utils.TimeUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mengshuai
 */
@Service
public class MessageServiceImpl extends BaseService implements MessageService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private RecordService recordService;

    @Override public void addMessage(int userId, Message message) {
        if("1".equals(message.getImmediate())) {
            message.setSendTime(new Date());
            RedisUtil.lpush(Constants.SENDING_LIST, message);
        }

        messageMapper.insertMessage(message);
        recordService.insertPubRecord(userId, message.getId());
    }

    @Override public List<Message> getUnreadMessages(int userId) {
        return messageMapper.getUnread(userId);
    }

    @Override public void deleteUnreandMessage(int messageId, int userId) {
        Map<String, Integer> map = new HashMap<String, Integer>(2);
        map.put("userId", userId);
        map.put("messageId", messageId);
        messageMapper.deleteUnread(map);
    }

    @Override public void modifyMessage(Message message, String operation) {
        //TODO maybe old status
        if (!message.isSended()) {
            if ("delete".equals(operation)) {
                messageMapper.deleteMessage(message);
            } else {
                messageMapper.updateMessage(message);
            }
        }
    }

    @Override public Map<String, Object> getMessage(Map<String, Object> param) {
        param.putAll(getPagination(param.get("page").toString(),
                param.get("rows").toString()));
        if ("sub".equals(param.get("type").toString())) {
            int userId = Integer.valueOf(param.get("userId").toString());
            param.put("range", recordService.getSubRecord(userId));
        }

        Map<String, Object> res = new HashMap<String, Object>(2);
        res.put("rows", messageMapper.getRows(param));
        res.put("total", messageMapper.getCount(param));
        return res;
    }
}
