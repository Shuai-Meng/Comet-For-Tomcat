package utils;

import manage.service.ManageService;
import manage.service.impl.MyUserServiceImpl;
import manage.vo.SecurityUser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

/**
 * Created by m on 17-5-30.
 */
public class SpringSecurityUtil implements ApplicationContextAware {
    private static ApplicationContext	applicationContext;

    public static SecurityUser getCurrentUser() {
        Object p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.isInstanceOf(User.class, p, "该用户登入已过期，请重新登入");
        Assert.notNull(p, "该用户登入已过期，请重新登入");
        return (SecurityUser)p;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringSecurityUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static Object getBean(String name) throws BeansException{
        return applicationContext.getBean(name);
    }
}
