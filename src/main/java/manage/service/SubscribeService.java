package manage.service;

import java.util.Map;

public interface SubscribeService {
    Map<String,Object> getSubscribeType(String page, String rows, String key, int userId);

    void subscribe(int userId, String typeId, String operation);
}
