package manage.service.impl;

import comet.MessageQueue;
import manage.mapper.*;
import manage.service.ManageService;
import manage.vo.Message;
import manage.vo.MessageType;
import manage.vo.MyUser;
import manage.vo.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        else if("ROLE_SUB".equals(role))
            myUser.setWhetherApplying("1");
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
        else if("modify".equals(operation)) {
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
        SecurityUser securityUser = SpringSecurityUtil.getCurrentUser();
        message.setPublisher(securityUser.getUsername());

        if("1".equals(message.getImmediate())) {
            message.setSendTime(new Date());
            MessageQueue.getSingleInstance().addMessage(message);
        }

        messageMapper.insertMessage(message);
    }

    public Map<String, Object> getSubscribeType(String key) {
        MessageType messageType = new MessageType();
        messageType.setName(key);
        SecurityUser securityUser = SpringSecurityUtil.getCurrentUser();
        messageType.setId(securityUser.getUserId());

        Map<String,Object> res = new HashMap<String,Object>();
        res.put("rows", messageMapper.getSubscribeType(messageType));
        res.put("total", messageMapper.getSubscribeTypeCount(key));
        return res;
    }

    public void subscribe(String typeId, String operation) {
        SecurityUser securityUser = SpringSecurityUtil.getCurrentUser();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("typeId", Integer.valueOf(typeId));
        map.put("userId", securityUser.getUserId());

        if(operation.equals("sub"))
            userMapper.subsribe(map);
        else
            userMapper.deSubsribe(map);
    }

    public List<Message> getUnreadMessages(int userId) {
        return unreadListMapper.getUnreadList(userId);
    }

    public void deleteUnreandMessage(int userId, int messageId) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("userId", userId);
        map.put("messageId", messageId);
        unreadListMapper.delete(map);
    }

    public Map<String, Object> getMessage(String key) {
        Map<String,Object> res = new HashMap<String,Object>();
        res.put("total", messageMapper.getCount(key));
        res.put("rows", messageMapper.getRows(key));
        return res;
    }
}
