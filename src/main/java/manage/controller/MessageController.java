package manage.controller;

import constants.Constants;
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
            Map<String, Object> param = new HashMap<String, Object>(5);
            param.put("userId", getUserId());
            param.put("type", httpServletRequest.getParameter("type"));
            param.put("key", httpServletRequest.getParameter("name"));
            param.put("page", httpServletRequest.getParameter("page"));
            param.put("rows", httpServletRequest.getParameter("rows"));
            return messageService.getMessage(param);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/addMessage")
    @ResponseBody
    public void addMessage(Message message) {
        if (ROLE_PUB.equals(getRole())) {
            message.setPublisher(getUserName());
            messageService.addMessage(getUserId(), message);
        }
    }

    @RequestMapping(value = "/modifyMessage")
    @ResponseBody
    public void modifyMessage(Message message, HttpServletRequest httpServletRequest) {
        if (ROLE_PUB.equals(getRole())) {
            String operation = httpServletRequest.getParameter("operation");
            message.setPublisher(getUserName());
            messageService.modifyMessage(message, operation);
        }
    }

    @RequestMapping(value = "/getUnreadMessages")
    @ResponseBody
    public List<Message> getUnreadMessage() {
        if (ROLES.contains(getRole())) {
            return messageService.getUnreadMessages(getUserId());
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/removeUnreadMessage")
    @ResponseBody
    public void removeUnreadMessage(HttpServletRequest httpServletRequest) {
        if (ROLES.contains(getRole())) {
            int messageId = Integer.parseInt(httpServletRequest.getParameter("messageId"));
            messageService.deleteUnreandMessage(messageId, getUserId());
        }
    }
}
