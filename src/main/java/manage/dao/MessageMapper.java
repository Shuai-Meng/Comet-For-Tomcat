package manage.dao;

import manage.vo.MessageType;

import java.util.List;

/**
 * Created by m on 17-5-21.
 */
public interface MessageMapper {
    List<MessageType> getRows(String name);
    int getCount(String name);

    void deleteType(int id);
    void updateType(MessageType messageType);
    void insertType(String name);
}
