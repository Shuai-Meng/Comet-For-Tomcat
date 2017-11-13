package manage.service;

import java.util.Map;

public interface SubscribeService {
    Map<String,Object> getSubscribeType(String page, String rows, String key);

    void subscribe(String typeId, String operation);
}
