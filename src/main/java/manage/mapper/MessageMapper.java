package manage.mapper;

import manage.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

/**
 * Created by m on 17-5-21.
 */
@Repository
public interface MessageMapper {
    List<MessageType> getTypeRows(Map<String, Object> map);
    int getTypeCount(@Param("name")String name);

    List<MessageType> getSubscribeType(Map<String, Object> map);
    Integer getSubscribeTypeCount(@Param("name") String name);
    void deleteType(@Param("id")int id);
    void updateType(MessageType messageType);
    void insertType(@Param("name")String name);
    List<MessageType> getAllTypes();

    List<Message> getRows(Map<String, Object> param);
    int getCount(@Param("name")String name);
    void updateMessage(Message message);
    void insertMessage(Message message);
    void deleteMessage(Message message);
    List<Message> getMessagesOfThisMin(Date date);

    List<Integer> getUserIdOfType(@Param("typeId")int type);
}
