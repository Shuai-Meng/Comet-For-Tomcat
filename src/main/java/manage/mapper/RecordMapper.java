package manage.mapper;

import manage.vo.MyUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author mengshuai
 */
@Repository
public interface RecordMapper {
    String getMessageList(@Param("userId") int userId);
    void updateRecord(MyUser myUser);
    void insertRecord(MyUser myUser);
}
