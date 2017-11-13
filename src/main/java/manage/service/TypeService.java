package manage.service;

import manage.vo.MessageType;

import java.util.List;
import java.util.Map;

public interface TypeService {
    void modifyType(String id, String name, String operation);

    List<MessageType> getMessageTypes();

    void addMessageType(MessageType messageType);

    Map<String,Object> getMessageType(String key, String page, String rows);
}
