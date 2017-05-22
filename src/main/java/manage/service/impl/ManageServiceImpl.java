package manage.service.impl;

import manage.dao.*;
import manage.service.ManageService;
import manage.vo.MessageType;
import manage.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by m on 17-5-20.
 */
@Service
public class ManageServiceImpl implements ManageService{
    @Autowired
    UserMapper userMapper;
    @Autowired
    MessageMapper messageMapper;

    public Map<String,Object> getUsers(String key, String type) {
        User user = new User();
        if("all".equals(type))
            user.setRole(null);
        else if("ROLE_SUB".equals(type))
            user.setFlag("1");
        else if("".equals(key))
            user.setName(null);

        Map<String,Object> res = new HashMap<String,Object>();
        res.put("total", userMapper.getCount(user));
        res.put("rows", userMapper.selectUserByRoleOrName(user));
        return res;
    }

    public void modifyAuth(User user) {
        userMapper.update(user);
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
        res.put("total", messageMapper.getCount(key));
        res.put("rows", messageMapper.getRows(key));
        return res;
    }

}
