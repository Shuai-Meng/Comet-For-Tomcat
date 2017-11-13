package manage.service;

import manage.vo.Message;
import manage.vo.MessageType;

import java.util.List;
import java.util.Map;

public interface MessageService {
    void addMessage(Message message);

    List<Message> getUnreadMessages();

    void deleteUnreandMessage(int messageId);

    void modifyMessage(Message message, String operation);

    Map<String,Object> getMessage(String key, String page, String rows);
}
