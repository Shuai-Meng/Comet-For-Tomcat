package manage.controller;

import manage.service.MessageService;
import manage.vo.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author mengshuai
 */
@Controller
@RequestMapping("/manage")
public class MessageController {
    @Resource
    private MessageService messageService;

    @RequestMapping(value = "/getMessage")
    @ResponseBody
    public Map<String,Object> getMessage(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("name");
        String page = httpServletRequest.getParameter("page");
        String rows = httpServletRequest.getParameter("rows");
        return messageService.getMessage(key, page, rows);
    }

    @RequestMapping(value = "/addMessage")
    @ResponseBody
    public void addMessage(Message message) {
        messageService.addMessage(message);
    }

    @RequestMapping(value = "/modifyMessage")
    @ResponseBody
    public void modifyMessage(Message message, HttpServletRequest httpServletRequest) {
        String operation = httpServletRequest.getParameter("operation");
        messageService.modifyMessage(message, operation);
    }

    @RequestMapping(value = "/getUnreadMessages")
    @ResponseBody
    public List<Message> getUnreadMessage() {
        return messageService.getUnreadMessages();
    }

    @RequestMapping(value = "/removeUnreadMessage")
    @ResponseBody
    public void removeUnreadMessage(HttpServletRequest httpServletRequest) {
        int messageId = Integer.parseInt(httpServletRequest.getParameter("messageId"));

        messageService.deleteUnreandMessage(messageId);
    }
}
