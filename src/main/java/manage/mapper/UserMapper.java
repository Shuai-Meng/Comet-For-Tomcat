package manage.mapper;

import manage.vo.MyUser;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by m on 17-5-13.
 */
@Repository
public interface UserMapper {
    void update(MyUser myUser);
    List<MyUser> selectUserByRoleOrName(MyUser myUser);
    int getCount(MyUser myUser);
    List<MyUser> selectUserByType(Map<String, Object> map);

    void subsribe(Map<String, Integer> map);

    void deSubsribe(Map<String, Integer> map);

    List<MyUser> selectUserByName(String name);
    MyUser selectUserByName1(String password);
}