package manage.service.impl;

import comet.Constants;
import manage.mapper.*;
import manage.service.ManageService;
import manage.vo.Message;
import manage.vo.MessageType;
import manage.vo.MyUser;
import manage.vo.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.RedisUtil;
import utils.SpringSecurityUtil;

import java.util.*;

/**
 * Created by m on 17-5-20.
 */
@Service
public class ManageServiceImpl implements ManageService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UnreadListMapper unreadListMapper;

    public Map<String,Object> getUsers(String key, String role) {
        MyUser myUser = new MyUser();
        myUser.setRole(role);
        myUser.setName(key);

        if("all".equals(role))
            myUser.setRole(null);
        else if("".equals(key))
            myUser.setName(null);

        Map<String,Object> res = new HashMap<String,Object>();
        res.put("total", userMapper.getCount(myUser));
        res.put("rows", userMapper.selectUserByRoleOrName(myUser));
        return res;
    }

    public List<MyUser> getUsers(String key, int typeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", key);
        map.put("typeId", typeId);
        return userMapper.selectUserByType(map);
    }

    public void modifyAuth(MyUser myUser) {
        userMapper.update(myUser);
    }

    public void modifyType(String id, String name, String operation) {
        if("delete".equals(operation))
            messageMapper.deleteType(Integer.valueOf(id));
        else if("update".equals(operation)) {
            MessageType messageType = new MessageType();
            messageType.setId(Integer.valueOf(id));
            messageType.setName(name);
            messageMapper.updateType(messageType);
        } else
            messageMapper.insertType(name);
    }

    public Map<String, Object> getMessageType(String key) {
        Map<String,Object> res = new HashMap<String,Object>();
        res.put("total", messageMapper.getTypeCount(key));
        res.put("rows", messageMapper.getTypeRows(key));
        return res;
    }

    public List<MessageType> getMessageTypes() {
        return messageMapper.getTypeRows(null);
    }

    public void addMessage(Message message) {
        message.setPublisher(getUser().getUsername());
        messageMapper.insertMessage(message);

        if("1".equals(message.getImmediate())) {
            message.setSendTime(new Date());
            RedisUtil.lpush(Constants.SENDING_LIST, message);
        }

    }

    public Map<String, Object> getSubscribeType(String key) {
        MessageType messageType = new MessageType();
        messageType.setName(key);
        messageType.setId(getUser().getUserId());

        Map<String,Object> res = new HashMap<String,Object>();
        res.put("rows", messageMapper.getSubscribeType(messageType));
        res.put("total", messageMapper.getSubscribeTypeCount(key));
        return res;
    }

    public void subscribe(String typeId, String operation) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("typeId", Integer.valueOf(typeId));
        map.put("userId", getUser().getUserId());

        if(operation.equals("sub"))//TODO
            userMapper.subsribe(map);
        else
            userMapper.deSubsribe(map);
    }

    public List<Message> getUnreadMessages() {
        return unreadListMapper.getUnreadList(getUser().getUserId());
    }

    public void deleteUnreandMessage(int messageId) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("userId", getUser().getUserId());
        map.put("messageId", messageId);
        unreadListMapper.delete(map);
    }

    public void modifyMessage(Message message, String operation) {
        if ("delete".equals(operation)) {
            messageMapper.deleteMessage(message);
        } else {
            message.setPublisher(getUser().getUsername());
            messageMapper.updateMessage(message);
        }
    }

    public void addMessageType(MessageType messageType) {
        messageMapper.insertType(messageType.getName());
    }

    public Map<String, Object> getMessage(String key) {
        Map<String,Object> res = new HashMap<String,Object>();
        res.put("total", messageMapper.getCount(key));
        res.put("rows", messageMapper.getRows(key));
        return res;
    }

    private SecurityUser getUser() {
        return SpringSecurityUtil.getCurrentUser();
    }
}
