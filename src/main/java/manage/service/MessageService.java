package manage.service;

import manage.vo.Message;
import manage.vo.MessageType;
import manage.vo.Publish;
import manage.vo.Receive;

import java.util.List;
import java.util.Map;

public interface MessageService {
    void addMessage(int userId, Message message);

    void markAsRead(int messageId, int userId);

    void modifyMessage(Message message, String operation);

    Map<String,Object> getMessage(Map<String, String> param);

    void insertReceive(int userId, List<Message> messageList, boolean status);

    void markAsPublished(int messageId);
}
