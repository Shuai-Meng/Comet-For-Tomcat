package manage.dao;

import manage.vo.User;

import java.util.*;

/**
 * Created by m on 17-5-13.
 */
public interface UserMapper {
    void update(User user);
    List<User> selectUserByRoleOrName(User user);
    int getCount(User user);
    List<User> selectUserByType(Map<String, Object> map);
}
