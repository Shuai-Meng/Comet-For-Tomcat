package manage.service;

import manage.vo.MyUser;

import java.util.List;
import java.util.Map;

public interface UserService {
    void modifyAuth(MyUser myUser);

    Map<String,Object> getUsers(String page, String rows, MyUser myUser);

    List<MyUser> getUsers(String key, int typeId);
}
