package manage.controller;

import manage.service.MessageService;
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
public class MessageController extends BaseController {
    @Resource
    private MessageService messageService;

    @RequestMapping(value = "/getMessage")
    @ResponseBody
    public Map<String,Object> getMessage(HttpServletRequest httpServletRequest) {
        if (ROLES.contains(getRole())) {
            Map<String, String> param = new HashMap<String, String>(5);
            param.put("userId", String.valueOf(getUserId()));
            param.put("type", httpServletRequest.getParameter("type"));
            param.put("title", httpServletRequest.getParameter("name"));
            param.put("page", httpServletRequest.getParameter("page"));
            param.put("rows", httpServletRequest.getParameter("rows"));
            return messageService.getMessage(param);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/addMessage")
    @ResponseBody
    public void addMessage(MyMessage message) {
        if (ROLE_PUB.equals(getRole())) {
            message.setPublisher(getUserName());
            messageService.addMessage(getUserId(), message);
        }
    }

    @RequestMapping(value = "/modifyMessage")
    @ResponseBody
    public void modifyMessage(MyMessage message, HttpServletRequest httpServletRequest) {
        if (ROLE_PUB.equals(getRole())) {
            String operation = httpServletRequest.getParameter("operation");
            message.setPublisher(getUserName());
            messageService.modifyMessage(message, operation);
        }
    }

    @RequestMapping(value = "/removeUnreadMessage")
    @ResponseBody
    public void removeUnreadMessage(HttpServletRequest httpServletRequest) {
        if (ROLES.contains(getRole())) {
            int messageId = Integer.parseInt(httpServletRequest.getParameter("messageId"));
            messageService.markAsRead(messageId, getUserId());
        }
    }
}
