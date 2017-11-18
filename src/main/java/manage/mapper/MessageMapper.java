package manage.mapper;

import manage.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by m on 17-5-21.
 */
@Repository
public interface MessageMapper {
    List<Message> getRows(Map<String, Object> param);
    int getCount(@Param("name")String name);
    void updateMessage(Message message);
    void insertMessage(Message message);
    void deleteMessage(Message message);
    List<Message> getMessagesOfThisMin(Date date);
    List<Message> getUnread(@Param("userId")int userId);
    void insertUnread(Map<String, Integer> map);
    void deleteUnread(Map<String, Integer> map);
    void changeSendStatus(@Param("id") int messageId);
}
