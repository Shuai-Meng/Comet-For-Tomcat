package manage.controller;

import manage.service.SubscribeService;
import manage.vo.SecurityUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static constants.Constants.ROLES;

/**
 * @author mengshuai
 */
@Controller
@RequestMapping("/manage")
public class SubscribeController extends BaseController {
    @Resource
    private SubscribeService subscribeService;

    @RequestMapping(value = "/modifySubscribe")
    @ResponseBody
    public void subscribe(HttpServletRequest httpServletRequest) {
        SecurityUser user = getUser();
        if (ROLES.contains(user.getAuthorities())) {
            String typeId = httpServletRequest.getParameter("id");
            String operation = httpServletRequest.getParameter("operation");
            subscribeService.subscribe(getUser().getUserId(), typeId, operation);
        }
    }

    @RequestMapping(value = "/getSubscribeType")
    @ResponseBody
    public Map<String,Object> getSubscribeType(HttpServletRequest httpServletRequest) {
        SecurityUser user = getUser();
        if (ROLES.contains(user.getAuthorities())) {
            String key = httpServletRequest.getParameter("name");
            String page = httpServletRequest.getParameter("page");
            String rows = httpServletRequest.getParameter("rows");
            return subscribeService.getSubscribeType(page, rows, key, user.getUserId());
        } else {
            return null;
        }
    }
}