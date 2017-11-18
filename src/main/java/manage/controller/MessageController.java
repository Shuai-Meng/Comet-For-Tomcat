package manage.controller;

import manage.service.MessageService;
import manage.vo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static constants.Constants.ROLES;

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
        SecurityUser user = getUser();
        if (ROLES.contains(user.getAuthorities())) {
            String key = httpServletRequest.getParameter("name");
            String page = httpServletRequest.getParameter("page");
            String rows = httpServletRequest.getParameter("rows");
            return messageService.getMessage(key, page, rows);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/addMessage")
    @ResponseBody
    public void addMessage(Message message) {
        SecurityUser user = getUser();
        if ("ROLE_PUB".equals(user.getAuthorities())) {
            message.setPublisher(user.getUsername());
            messageService.addMessage(message);
        }
    }

    @RequestMapping(value = "/modifyMessage")
    @ResponseBody
    public void modifyMessage(Message message, HttpServletRequest httpServletRequest) {
        SecurityUser user = getUser();
        if ("ROLE_PUB".equals(user.getAuthorities())) {
            String operation = httpServletRequest.getParameter("operation");
            message.setPublisher(user.getUsername());
            messageService.modifyMessage(message, operation);
        }
    }

    @RequestMapping(value = "/getUnreadMessages")
    @ResponseBody
    public List<Message> getUnreadMessage() {
        SecurityUser user = getUser();
        if (ROLES.contains(user.getAuthorities())) {
            return messageService.getUnreadMessages(user.getUserId());
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/removeUnreadMessage")
    @ResponseBody
    public void removeUnreadMessage(HttpServletRequest httpServletRequest) {
        SecurityUser user = getUser();
        if (ROLES.contains(user.getAuthorities())) {
            int messageId = Integer.parseInt(httpServletRequest.getParameter("messageId"));
            messageService.deleteUnreandMessage(messageId, user.getUserId());
        }
    }
}
