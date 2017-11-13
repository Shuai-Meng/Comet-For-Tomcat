package manage.mapper;

import manage.vo.MessageType;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author mengshuai
 */
@Repository
public interface SubscribeMapper {
    List<MessageType> getSubscribeType(Map<String, Object> param);

    int getSubscribeTypeCount(String key);

    void subscribe(Map<String, Integer> map);

    void unSubscribe(Map<String, Integer> map);
}
