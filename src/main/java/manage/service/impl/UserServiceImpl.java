package manage.service.impl;

import com.github.pagehelper.*;
import manage.mapper.MyUserMapper;
import manage.service.UserService;
import manage.vo.MyUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author shuaimeng
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private MyUserMapper mapper;

    @Override public Map<String,Object> getUsers(String page, String rows, MyUser myUser) {
        Example example = new Example(MyUser.class);
        Example.Criteria criteria = example.createCriteria();
        if ("ROLE_ALL".equals(myUser.getRole())) {
            myUser.setRole(null);
        }
        criteria.andEqualTo("role", myUser.getRole());
        if (!StringUtils.isEmpty(myUser.getName())) {
            criteria.andLike("name", "%" + myUser.getName() + "%");
        }

        PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
        List<MyUser> list = mapper.selectByExample(example);

        Map<String, Object> res = new HashMap<String,Object>(2);
        res.put("rows", list);
        res.put("total", ((Page) list).getTotal());
        return res;
    }

    @Override public void modifyAuth(MyUser myUser) {
        Example example = new Example(MyUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", myUser.getId());

        mapper.updateByExampleSelective(myUser, example);
    }
}
