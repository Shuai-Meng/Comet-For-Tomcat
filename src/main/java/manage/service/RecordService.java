package manage.service;

import java.util.*;

/**
 * @author mengshuai
 */
public interface RecordService {
    void insertRecord(int userId, List<Integer> messageList);
    List<Integer> getRecord(int userId);
}
