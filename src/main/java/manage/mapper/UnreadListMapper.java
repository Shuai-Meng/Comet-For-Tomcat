package manage.mapper;

import manage.vo.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by mengshuai on 2017/9/21.
 */
@Repository
public interface UnreadListMapper {
    List<Message> getUnreadList(@Param("userId")int userId);
    void insert(Map<String, Integer> map);
    void delete(Map<String, Integer> map);
}
