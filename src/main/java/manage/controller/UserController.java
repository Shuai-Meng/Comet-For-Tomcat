package manage.controller;

import manage.service.UserService;
import manage.vo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.SpringSecurityUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static constants.Constants.ROLE_ADMIN;

/**
 * @author mengshuai
 */
@Controller
@RequestMapping("/manage")
public class UserController {
    @Resource
    private UserService authService;

    @RequestMapping(value = "/getUsers")
    @ResponseBody
    public Map<String,Object> getUsers(HttpServletRequest httpServletRequest, MyUser myUser) {
        if (ROLE_ADMIN.equals(SpringSecurityUtil.getRole())) {
            String page = httpServletRequest.getParameter("page");
            String rows = httpServletRequest.getParameter("rows");
            return authService.getUsers(page, rows, myUser);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/modifyAuth")
    @ResponseBody
    public void modifyAuth(MyUser myUser) {
        if (ROLE_ADMIN.equals(SpringSecurityUtil.getRole())) {
            authService.modifyAuth(myUser);
        }
    }
}
