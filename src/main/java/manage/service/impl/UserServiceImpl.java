package manage.service.impl;

import manage.mapper.UserMapper;
import manage.service.UserService;
import manage.vo.MyUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shuaimeng
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override public Map<String,Object> getUsers(String page, String rows, MyUser myUser) {
        if("all".equals(myUser.getRole())) {
            myUser.setRole(null);
        } else if("".equals(myUser.getName())) {
            myUser.setName(null);
        }

        Map<String, Object> param = getPagination(page, rows);
        param.put("name", myUser.getName());
        param.put("role", myUser.getRole());

        Map<String, Object> res = new HashMap<String,Object>(2);
        res.put("total", userMapper.getCount(myUser));
        res.put("rows", userMapper.selectUserByRoleOrName(param));
        return res;
    }

    @Override public void modifyAuth(MyUser myUser) {
        userMapper.update(myUser);
    }
}
