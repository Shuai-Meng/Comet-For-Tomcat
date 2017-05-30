package utils;

import manage.vo.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

/**
 * Created by m on 17-5-30.
 */
public class SpringSecurityUtil {
    public static SecurityUser getCurrentUser() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.isInstanceOf(User.class, p, "该用户登入已过期，请重新登入");
        Assert.notNull(p, "该用户登入已过期，请重新登入");
        return (SecurityUser)p;
    }
}
