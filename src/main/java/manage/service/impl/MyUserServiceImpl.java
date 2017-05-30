package manage.service.impl;

import manage.dao.UserMapper;
import manage.vo.MyUser;
import manage.vo.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import java.util.*;

/**
 * Created by shuaimeng2 on 2017/5/30.
 */
public class MyUserServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        List<MyUser> list = userMapper.selectUserByName(name);

        if (list == null || list.isEmpty())
            throw new UsernameNotFoundException(name + " not found!");

        return generateUserDetails(list.get(0));
    }

    private User generateUserDetails(MyUser myUser) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(myUser.getRole()));

        SecurityUser user = new SecurityUser(myUser.getName(), myUser.getPassword(), authorities);
        user.setUserId(myUser.getId());
        return user;
    }
}
