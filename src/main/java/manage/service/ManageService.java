package manage.service;

import manage.vo.MessageType;
import manage.vo.*;

import java.util.*;

/**
 * Created by m on 17-5-12.
 */
public interface ManageService {
    void modifyAuth(MyUser myUser);

    Map<String,Object> getUsers(String page, String rows, MyUser myUser);

    void modifyType(String id, String name, String operation);

    List<MyUser> getUsers(String key, int typeId);

    List<MessageType> getMessageTypes();

    void addMessage(Message message);

    Map<String,Object> getSubscribeType(String page, String rows, String key);

    void subscribe(String typeId, String operation);

    List<Message> getUnreadMessages();

    void deleteUnreandMessage(int messageId);

    void modifyMessage(Message message, String operation);

    void addMessageType(MessageType messageType);

    Map<String,Object> getMessage(String key, String page, String rows);

    Map<String,Object> getMessageType(String key, String page, String rows);
}
