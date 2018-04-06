package utils;

import manage.vo.SecurityUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

/**
 *
 * @author m
 * @date 17-5-30
 */
public class SpringSecurityUtil {
    public static SecurityUser getUser() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.isInstanceOf(User.class, p, "该用户登入已过期，请重新登入");
        Assert.notNull(p, "该用户登入已过期，请重新登入");
        return (SecurityUser)p;
    }

    public static String getRole() {
        Object[] roles = getUser().getAuthorities().toArray();
        return ((GrantedAuthority) roles[0]).getAuthority();
    }

    public static String getUserName() {
        return getUser().getUsername();
    }

    public static int getUserId() {
        return getUser().getUserId();
    }

    public static int getDepartmentId() {
        return getUser().getDepartmentId();
    }
}
