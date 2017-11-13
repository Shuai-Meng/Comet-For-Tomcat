package manage.controller;

import manage.service.UserService;
import manage.vo.MyUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
        String page = httpServletRequest.getParameter("page");
        String rows = httpServletRequest.getParameter("rows");
        return authService.getUsers(page, rows, myUser);
    }

    @RequestMapping(value = "/getUser")
    @ResponseBody
    public List<MyUser> getUser(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("q");
        String type = httpServletRequest.getParameter("type");
        return authService.getUsers(key, Integer.valueOf(type));
    }

    @RequestMapping(value = "/modifyAuth")
    @ResponseBody
    public void modifyAuth(MyUser myUser) {
        authService.modifyAuth(myUser);
    }
}
