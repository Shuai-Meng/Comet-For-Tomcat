package manage.mapper;

import manage.vo.MyUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author mengshuai
 */
@Repository
public interface RecordMapper {
    String getSubMessageList(@Param("userId") int userId);
    void updateSubRecord(MyUser myUser);
    void insertSubRecord(MyUser myUser);

    void insertPubRecord(Map<String, Integer> map);
    void deletePubRecord(Map<String, Integer> map);
}
