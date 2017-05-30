package manage.dao;

import manage.vo.Message;
import manage.vo.MessageType;
import manage.vo.User;

import java.util.List;

/**
 * Created by m on 17-5-21.
 */
public interface MessageMapper {
    List<MessageType> getTypeRows(String name);
    int getTypeCount(String name);

    List<MessageType> getSubscribeType(MessageType messageType);
    int getSubscribeTypeCount(String name);

    void deleteType(int id);
    void updateType(MessageType messageType);
    void insertType(String name);

    List<MessageType> getRows(String name);
    int getCount(String name);
    void deleteMessage(int id);
    void updateMessage(Message message);
    void insertMessage(Message message);
}
