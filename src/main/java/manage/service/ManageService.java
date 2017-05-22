package manage.service;

import manage.vo.User;

import java.util.*;

/**
 * Created by m on 17-5-12.
 */
public interface ManageService {
    void modifyAuth(User user);

    Map<String,Object> getUsers(String key, String type);

    void modifyType(String id, String name, String operation);

    Map<String,Object> getMessageType(String key);
}
