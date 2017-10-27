package manage.controller;

import com.mysql.jdbc.MysqlIO;
import manage.service.ManageService;
import manage.vo.Message;
import manage.vo.MessageType;
import manage.vo.MyUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**----  Auth ----*/
    @RequestMapping(value = "/getUsers")
    @ResponseBody
    public Map<String,Object> getUsers(HttpServletRequest httpServletRequest, MyUser myUser) {
        String page = httpServletRequest.getParameter("page");
        String rows = httpServletRequest.getParameter("rows");
        return manageService.getUsers(page, rows, myUser);
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

    /**----  Subscribe ----*/
    @RequestMapping(value = "/modifySubscribe")
    @ResponseBody
    public void subscribe(HttpServletRequest httpServletRequest) {
        String typeId = httpServletRequest.getParameter("id");
        String operation = httpServletRequest.getParameter("operation");
        manageService.subscribe(typeId, operation);
    }

    @RequestMapping(value = "/getSubscribeType")
    @ResponseBody
    public Map<String,Object> getSubscribeType(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("name");
        String page = httpServletRequest.getParameter("page");
        String rows = httpServletRequest.getParameter("rows");
        return manageService.getSubscribeType(page, rows, key);
    }

    /**----  Type ----*/
    @RequestMapping(value = "/getMessageTypes")
    @ResponseBody
    public List<MessageType> getMessageTypes() {
        return manageService.getMessageTypes();
    }

    @RequestMapping(value = "/getMessageType")
    @ResponseBody
    public Map<String,Object> getMessageType(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("name");
        String page = httpServletRequest.getParameter("page");
        String rows = httpServletRequest.getParameter("rows");
        return manageService.getMessageType(key, page, rows);
    }

    @RequestMapping(value = "/addType")
    @ResponseBody
    public void addType(MessageType messageType) {
        manageService.addMessageType(messageType);
    }

    @RequestMapping(value = "/modifyType")
    @ResponseBody
    public void modifyType(HttpServletRequest httpServletRequest) {
        String id = httpServletRequest.getParameter("id");
        String name = httpServletRequest.getParameter("name");
        String operation = httpServletRequest.getParameter("operation");
        manageService.modifyType(id, name, operation);
    }

    /**----  Message ----*/
    @RequestMapping(value = "/getMessage")
    @ResponseBody
    public Map<String,Object> getMessage(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("name");
        String page = httpServletRequest.getParameter("page");
        String rows = httpServletRequest.getParameter("rows");
        return manageService.getMessage(key, page, rows);
    }

    @RequestMapping(value = "/addMessage")
    @ResponseBody
    public void addMessage(Message message) {
        manageService.addMessage(message);
    }

    @RequestMapping(value = "/modifyMessage")
    @ResponseBody
    public void modifyMessage(Message message, HttpServletRequest httpServletRequest) {
        String operation = httpServletRequest.getParameter("operation");
        manageService.modifyMessage(message, operation);
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
