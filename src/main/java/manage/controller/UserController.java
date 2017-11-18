package manage.controller;

import manage.service.UserService;
import manage.vo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static constants.Constants.ROLE_ADMIN;

/**
 * @author mengshuai
 */
@Controller
@RequestMapping("/manage")
public class UserController extends BaseController {
    @Resource
    private UserService authService;

    @RequestMapping(value = "/getUsers")
    @ResponseBody
    public Map<String,Object> getUsers(HttpServletRequest httpServletRequest, MyUser myUser) {
        SecurityUser user = getUser();
        if (ROLE_ADMIN.equals(user.getAuthorities())) {
            String page = httpServletRequest.getParameter("page");
            String rows = httpServletRequest.getParameter("rows");
            return authService.getUsers(page, rows, myUser);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/getUser")
    @ResponseBody
    public List<MyUser> getUser(HttpServletRequest httpServletRequest) {
        SecurityUser user = getUser();
        if (ROLE_ADMIN.equals(user.getAuthorities())) {
            String key = httpServletRequest.getParameter("q");
            String type = httpServletRequest.getParameter("type");
            return authService.getUsers(key, Integer.valueOf(type));
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/modifyAuth")
    @ResponseBody
    public void modifyAuth(MyUser myUser) {
        SecurityUser user = getUser();
        if (ROLE_ADMIN.equals(user.getAuthorities())) {
            authService.modifyAuth(myUser);
        }
    }
}
