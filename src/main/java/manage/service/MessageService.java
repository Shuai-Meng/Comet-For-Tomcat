package manage.service;

import manage.vo.MyMessage;
import manage.vo.MyUser;

import java.util.List;
import java.util.Map;

public interface MessageService {
    void addMessage(int userId, MyMessage message);

    void markAsRead(int messageId, int userId);

    void modifyMessage(MyMessage message, String operation);

    Map<String,Object> getMessage(Map<String, String> param);

    void insertReceive(int messageId, MyUser myUser);

    void markAsPublished(int messageId);
}
