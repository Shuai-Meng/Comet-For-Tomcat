package manage.controller;

import manage.service.TypeService;
import manage.vo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static constants.Constants.*;

/**
 * @author mengshuai
 */
@Controller
@RequestMapping("/manage")
public class TypeController extends BaseController {
    @Resource
    private TypeService typeService;

    @RequestMapping(value = "/getMessageTypes")
    @ResponseBody
    public List<MessageType> getMessageTypes() {
        SecurityUser user = getUser();
        if (ROLE_PUB.equals(user.getAuthorities())) {
            return typeService.getMessageTypes();
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/getMessageType")
    @ResponseBody
    public Map<String,Object> getMessageType(HttpServletRequest httpServletRequest) {
        SecurityUser user = getUser();
        if (ROLE_ADMIN.equals(user.getAuthorities())) {
            String key = httpServletRequest.getParameter("name");
            String page = httpServletRequest.getParameter("page");
            String rows = httpServletRequest.getParameter("rows");
            return typeService.getMessageType(key, page, rows);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/addType")
    @ResponseBody
    public void addType(MessageType messageType) {
        SecurityUser user = getUser();
        if (ROLE_ADMIN.equals(user.getAuthorities())) {
            typeService.addMessageType(messageType);
        }
    }

    @RequestMapping(value = "/modifyType")
    @ResponseBody
    public void modifyType(HttpServletRequest httpServletRequest) {
        SecurityUser user = getUser();
        if (ROLE_ADMIN.equals(user.getAuthorities())) {
            String id = httpServletRequest.getParameter("id");
            String name = httpServletRequest.getParameter("name");
            String operation = httpServletRequest.getParameter("operation");
            typeService.modifyType(id, name, operation);
        }
    }
}
