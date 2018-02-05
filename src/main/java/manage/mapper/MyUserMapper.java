package manage.mapper;

import manage.vo.MyUser;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author mengshuai
 */
@Repository
public interface MyUserMapper extends Mapper<MyUser> {
    List<MyUser> getUsers(Map<String, Object> param);
}
