package manage.mapper;

import manage.vo.Message;
import manage.vo.MessageType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by m on 17-5-21.
 */
@Repository
public interface MessageMapper {
    List<MessageType> getTypeRows(String name);
    int getTypeCount(@Param("name")String name);

    List<MessageType> getSubscribeType(MessageType messageType);
    Integer getSubscribeTypeCount(@Param("name") String name);

    void deleteType(int id);
    void updateType(MessageType messageType);
    void insertType(String name);

    List<Message> getRows(String name);
    int getCount(String name);
    void updateMessage(Message message);
    void insertMessage(Message message);

    List<Integer> getUserIdOfType(@Param("typeId")int type);

    List<Message> getMessagesOfThisMin(Date date);

    void deleteMessage(Message message);
}
