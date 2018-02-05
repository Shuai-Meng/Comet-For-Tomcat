package manage.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import constants.Constants;
import manage.mapper.MessageMapper;
import manage.mapper.PublishMapper;
import manage.mapper.ReceiveMapper;
import manage.service.MessageService;
import manage.vo.Message;
import manage.vo.Publish;
import manage.vo.Receive;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import utils.RedisUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mengshuai
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private ReceiveMapper receiveMapper;
    @Resource
    private PublishMapper publishMapper;

    @Override public void addMessage(int userId, Message message) {
        Publish publish = new Publish();
        publish.setUserId(userId);
        publish.setMessageId(message.getId());//TODO

        if ("1".equals(message.getImmediate())) {//TODO
            message.setSendTime(new Date());
            RedisUtil.lpush(Constants.SENDING_LIST, message);
            publish.setStatus(true);
        } else {
            publish.setStatus(false);
        }

        messageMapper.insertSelective(message);
        publishMapper.insertSelective(publish);
    }

    @Override public void markAsRead(int messageId, int userId) {
        Example example = new Example(Receive.class);
        example.createCriteria().andEqualTo("userId", userId).andEqualTo("messageId", messageId);

        Receive receive = new Receive();
        receive.setStatus(true);//TODO
        receiveMapper.updateByExampleSelective(receive, example);
    }

    @Override public void modifyMessage(Message message, String operation) {
        Example example = new Example(Message.class);
        example.createCriteria().andEqualTo("id", message.getId());

        if ("delete".equals(operation)) {
            messageMapper.deleteByExample(example);
        } else if ("update".equals(operation)) {
            messageMapper.updateByExampleSelective(message, example);
        }
        //TODO maybe old status
//        if (!message.isSended()) {
//        }
    }

    @Override public Map<String, Object> getMessage(Map<String, String> param) {
        Example example = new Example(Message.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("title", param.get("title"));
        int userId = Integer.parseInt(param.get("userId"));
        String type = (String) param.get("type");
        if ("published".equals(type)) {
            criteria.andIn("id", publishMapper.selectByExample(userId));//TODO
        } else if ("received".equals(type)) {
            criteria.andIn("id", receiveMapper.selectByExample(userId));
        }

        int page = Integer.parseInt((String) param.get("page"));
        int size = Integer.parseInt((String) param.get("rows"));
        PageHelper.startPage(page, size);
        List<Message> list = messageMapper.selectByExample(example);

        Map<String, Object> res = new HashMap<String, Object>(2);
        res.put("rows", list);
        res.put("total", ((Page) list).getTotal());
        return res;
    }

    @Override public void insertReceive(int userId, List<Message> messageList, boolean status) {
        for (Message message : messageList) {
            Receive receive = new Receive();
            receive.setUserId(userId);
            receive.setMessageId(message.getId());
            receive.setStatus(status);
            receiveMapper.insertSelective(receive);
        }
    }

    @Override public void markAsPublished(int messageId) {
        Example example = new Example(Publish.class);
        example.createCriteria().andEqualTo("messageid", messageId);

        Publish publish = new Publish();
        publish.setStatus(true);
        publish.setMessageId(messageId);
        publishMapper.updateByExampleSelective(publish, example);
    }
}
