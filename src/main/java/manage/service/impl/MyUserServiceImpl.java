package manage.service.impl;

import manage.mapper.MyUserMapper;
import manage.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 *
 * @author shuaimeng2
 * @date 2017/5/30
 */
@Service
public class MyUserServiceImpl implements UserDetailsService {
    @Autowired
    private MyUserMapper mapper;

    @Override public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Example example = new Example(MyUser.class);
        example.createCriteria().andEqualTo("name", name);
        List<MyUser> list = mapper.selectByExample(example);

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
