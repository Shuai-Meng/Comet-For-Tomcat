package manage.dao;

import manage.vo.MyUser;

import java.util.*;

/**
 * Created by m on 17-5-13.
 */
public interface UserMapper {
    void update(MyUser myUser);
    List<MyUser> selectUserByRoleOrName(MyUser myUser);
    int getCount(MyUser myUser);
    List<MyUser> selectUserByType(Map<String, Object> map);

    void subsribe(Map<String, Integer> map);

    void deSubsribe(Map<String, Integer> map);

    List<MyUser> selectUserByName(String name);
}
