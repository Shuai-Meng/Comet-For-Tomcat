package manage.service;

import manage.vo.Message;
import manage.vo.MessageType;

import java.util.List;
import java.util.Map;

public interface MessageService {
    void addMessage(Message message);

    List<Message> getUnreadMessages(int userId);

    void deleteUnreandMessage(int messageId, int userId);

    void modifyMessage(Message message, String operation);

    Map<String,Object> getMessage(int userId, String key, String page, String rows);
}
