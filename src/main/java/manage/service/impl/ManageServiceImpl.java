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

    public Map<String,Object> getUsers(String page, String rows, MyUser myUser) {
        if("all".equals(myUser.getRole()))
            myUser.setRole(null);
        else if("".equals(myUser.getName()))
            myUser.setName(null);

        Map<String, Object> param = getPagination(page, rows);
        param.put("name", myUser.getName());
        param.put("role", myUser.getRole());

        Map<String, Object> res = new HashMap<String,Object>();
        res.put("total", userMapper.getCount(myUser));
        res.put("rows", userMapper.selectUserByRoleOrName(param));
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

    public Map<String, Object> getMessageType(String key, String page, String rows) {
        Map<String,Object> res = new HashMap<String,Object>();

        Map<String, Object> param = getPagination(page, rows);
        param.put("name", key);
        res.put("total", messageMapper.getTypeCount(key));
        res.put("rows", messageMapper.getTypeRows(param));
        return res;
    }

    public List<MessageType> getMessageTypes() {
        return messageMapper.getAllTypes();
    }

    public void addMessage(Message message) {
        message.setPublisher(getUser().getUsername());
        messageMapper.insertMessage(message);

        if("1".equals(message.getImmediate())) {
            message.setSendTime(new Date());
            RedisUtil.lpush(Constants.SENDING_LIST, message);
        }
    }

    public Map<String, Object> getSubscribeType(String page, String rows, String key) {
        Map<String, Object> param = getPagination(page, rows);
        param.put("name", key);
        param.put("id", getUser().getUserId());

        Map<String,Object> res = new HashMap<String,Object>();
        res.put("rows", messageMapper.getSubscribeType(param));
        res.put("total", messageMapper.getSubscribeTypeCount(key));
        return res;
    }

    public void subscribe(String typeId, String operation) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("typeId", Integer.valueOf(typeId));
        map.put("userId", getUser().getUserId());

        if(operation.equals("sub"))//TODO
            userMapper.subscribe(map);
        else
            userMapper.unSubscribe(map);
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

    public Map<String, Object> getMessage(String name, String page, String rows) {
        Map<String, Object> res = new HashMap<String, Object>();

        Map<String, Object> param = getPagination(page, rows);
        param.put("name", name);
        res.put("rows", messageMapper.getRows(param));
        res.put("total", messageMapper.getCount(name));
        return res;
    }

    private Map<String, Object> getPagination(String page, String rows) {
        int pageInt = page == null ? 1 : Integer.parseInt(page);
        int rowsInt = rows == null ? 10 : Integer.parseInt(rows);
        int offset = (pageInt - 1) * rowsInt;

        Map<String,Object> param = new HashMap<String,Object>();
        param.put("offset", offset);
        param.put("size", rowsInt);
        return param;
    }

    private SecurityUser getUser() {
        return SpringSecurityUtil.getCurrentUser();
    }
}
