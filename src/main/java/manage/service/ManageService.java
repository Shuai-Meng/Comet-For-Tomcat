package manage.service;

import manage.vo.MessageType;
import manage.vo.*;

import java.util.*;

/**
 * Created by m on 17-5-12.
 */
public interface ManageService {
    void modifyAuth(User user);

    Map<String,Object> getUsers(String key, String role);

    void modifyType(String id, String name, String operation);

    Map<String,Object> getMessageType(String key);

    Map<String,Object> getMessage(String key);

    List<User> getUsers(String key, int typeId);

    List<MessageType> getMessageTypes();

    void addMessage(Message message);

    Map<String,Object> getSubscribeType(String key);
}
