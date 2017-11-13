package manage.service.impl;

import manage.mapper.UserMapper;
import manage.vo.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * @author shuaimeng2
 * @date 2017/5/30
 */
@Service
public class MyUserServiceImpl implements UserDetailsService {
    @Resource
    UserMapper userMapper;

    @Override public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        List<MyUser> list = userMapper.selectUserByName(name);
        userMapper.selectUserByName1("admin");

        if (list == null || list.isEmpty()) {
            throw new UsernameNotFoundException(name + " not found!");
        }

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
