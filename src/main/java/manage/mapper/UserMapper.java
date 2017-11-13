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

    List<MyUser> selectUserByRoleOrName(Map<String, Object> map);

    int getCount(MyUser myUser);

    List<MyUser> selectUserByType(Map<String, Object> map);

    List<MyUser> selectUserByName(String name);

    MyUser selectUserByName1(String password);
}
