package manage.service;

import manage.vo.MessageType;

import java.util.List;
import java.util.Map;

public interface TypeService {
    void modifyType(String id, String name, String operation);

    List<MessageType> getAllTypes();

    void addMessageType(MessageType messageType);

    Map<String,Object> getMessageType(Map<String, String> param);

    void modifySubscribe(int userId, String typeId, String operation);

    List<Integer> getSubscribed(int userId);
}
