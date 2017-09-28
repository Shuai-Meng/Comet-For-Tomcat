package manage.controller;

import manage.service.ManageService;
import manage.vo.Message;
import manage.vo.MessageType;
import manage.vo.MyUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by m on 17-5-4.
 */
@Controller
@RequestMapping("/manage")
public class ManageController {
    @Resource
    private ManageService manageService;

    @RequestMapping(value = "/home")
    public ModelAndView getHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");

        return modelAndView;
    }

    @RequestMapping(value = "/getUsers")
    @ResponseBody
    public Map<String,Object> getUsers(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("key");
        String role = httpServletRequest.getParameter("role");
        return manageService.getUsers(key, role);
    }

    @RequestMapping(value = "/getUser")
    @ResponseBody
    public List<MyUser> getUser(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("q");
        String type = httpServletRequest.getParameter("type");
        return manageService.getUsers(key, Integer.valueOf(type));
    }

    @RequestMapping(value = "/modifyAuth")
    @ResponseBody
    public void modifyAuth(MyUser myUser) {
        manageService.modifyAuth(myUser);
    }

    @RequestMapping(value = "/getMessageType")
    @ResponseBody
    public Map<String,Object> getMessageType(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("key");
        return manageService.getMessageType(key);
    }

    @RequestMapping(value = "/getSubscribeType")
    @ResponseBody
    public Map<String,Object> getSubscribeType(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("key");
        return manageService.getSubscribeType(key);
    }

    @RequestMapping(value = "/subscribe")
    public void subscribe(HttpServletRequest httpServletRequest) {
        String typeId = httpServletRequest.getParameter("typeId");
        String operation = httpServletRequest.getParameter("operation");
        manageService.subscribe(typeId, operation);
    }

    @RequestMapping(value = "/getMessageTypes")
    @ResponseBody
    public List<MessageType> getMessageTypes(HttpServletRequest httpServletRequest) {
        return manageService.getMessageTypes();
    }

    @RequestMapping(value = "/modifyType")
    @ResponseBody
    public void modifyType(HttpServletRequest httpServletRequest) {
        String id = httpServletRequest.getParameter("id");
        String name = httpServletRequest.getParameter("name");
        String operation = httpServletRequest.getParameter("operation");
        manageService.modifyType(id, name, operation);
    }

    @RequestMapping(value = "/getMessage")
    @ResponseBody
    public Map<String,Object> getMessage(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("key");
        return manageService.getMessage(key);
    }

    @RequestMapping(value = "/addMessage")
    @ResponseBody
    public void addMessage(Message message) {
        manageService.addMessage(message);
    }

    @RequestMapping(value = "/getUnreadMessages")
    @ResponseBody
    public List<Message> getUnreadMessage() {
        return manageService.getUnreadMessages();
    }

    @RequestMapping(value = "/removeUnreadMessage")
    @ResponseBody
    public void removeUnreadMessage(HttpServletRequest httpServletRequest) {
        int messageId = Integer.parseInt(httpServletRequest.getParameter("messageId"));

        manageService.deleteUnreandMessage(messageId);
    }
}
