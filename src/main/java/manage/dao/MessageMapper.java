package manage.dao;

import manage.vo.Message;
import manage.vo.MessageType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by m on 17-5-21.
 */
public interface MessageMapper {
    List<MessageType> getTypeRows(String name);
    int getTypeCount(@Param("name")String name);

    List<MessageType> getSubscribeType(MessageType messageType);
    Integer getSubscribeTypeCount(@Param("name") String name);

    void deleteType(int id);
    void updateType(MessageType messageType);
    void insertType(String name);

    List<MessageType> getRows(String name);
    int getCount(String name);
    void deleteMessage(int id);
    void updateMessage(Message message);
    void insertMessage(Message message);
}
