package manage.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import manage.mapper.MessageMapper;
import manage.mapper.PublishMapper;
import manage.mapper.ReceiveMapper;
import manage.service.MessageService;
import manage.vo.MyMessage;
import manage.vo.MyUser;
import manage.vo.Publish;
import manage.vo.Receive;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
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
    @Resource
    private JmsTemplate   jmsTemplate;
    @Autowired
    @Qualifier("queueDestination")
    private Destination   destination;

    @Override public void addMessage(int userId, final MyMessage message) {
        Publish publish = new Publish();

        if ("1".equals(message.getImmediate())) {//TODO
            jmsTemplate.send(destination, new MessageCreator() {
                @Override
                public Message createMessage(Session session)
                        throws JMSException {
                    ActiveMQObjectMessage msg = (ActiveMQObjectMessage) session.createObjectMessage();
                    msg.setObject(message);
                    return msg;
                }
            });
            publish.setStatus(true);
            message.setSendTime(new Date());
        } else {
            publish.setStatus(false);
        }

        messageMapper.insertMessage(message);

        publish.setUserId(userId);
        //TODO no id
        publish.setMessageId(message.getId());
        publishMapper.insertSelective(publish);
    }

    @Override public void markAsRead(int messageId, int userId) {
        Example example = new Example(Receive.class);
        example.createCriteria().andEqualTo("userId", userId).andEqualTo("messageId", messageId);

        Receive receive = new Receive();
        receive.setStatus(true);
        receiveMapper.updateByExampleSelective(receive, example);
    }

    @Override public void modifyMessage(MyMessage message, String operation) {
        Example example = new Example(MyMessage.class);
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
        Example example = new Example(MyMessage.class);
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
        List<MyMessage> list = messageMapper.selectByExample(example);

        Map<String, Object> res = new HashMap<String, Object>(2);
        res.put("rows", list);
        res.put("total", ((Page) list).getTotal());
        return res;
    }

    @Override public void insertReceive(int messageId, MyUser myUser) {
        Receive receive = new Receive();
        receive.setMessageId(messageId);
        receive.setUserId(myUser.getId());
        receive.setStatus(myUser.isOnline());

        receiveMapper.insertSelective(receive);
    }

    @Override public void markAsPublished(int messageId) {
        Example example = new Example(Publish.class);
        example.createCriteria().andEqualTo("messageId", messageId);

        Publish publish = new Publish();
        publish.setStatus(true);
        publish.setMessageId(messageId);
        publishMapper.updateByExampleSelective(publish, example);
    }
}
