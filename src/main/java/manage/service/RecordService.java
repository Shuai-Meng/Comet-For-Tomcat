package manage.service;

import java.util.*;

/**
 * @author mengshuai
 */
public interface RecordService {
    void insertSubRecord(int userId, List<Integer> messageList);
    List<Integer> getSubRecord(int userId);

    void insertPubRecord(int userId, int messageId);
    void deletePubRecord(int userId, int messageId);
}
