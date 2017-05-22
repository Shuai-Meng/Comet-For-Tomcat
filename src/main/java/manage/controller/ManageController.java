package manage.controller;

import comet.Message;
import comet.MessageQueue;
import manage.service.ManageService;
import manage.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by m on 17-5-4.
 */
@Controller
@RequestMapping("/manage")
public class ManageController {
    @Resource
    ManageService manageService;

    @RequestMapping(value = "/home")
    public ModelAndView getHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");

        return modelAndView;
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public void saveMsg(HttpServletRequest httpServletRequest) {
        String msg = httpServletRequest.getParameter("msg");
        Message message = new Message();
        message.setContent(msg);
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        ServletContext servletContext = webApplicationContext.getServletContext();
        MessageQueue messageQueue = (MessageQueue)servletContext.getAttribute("messageQueue");
        messageQueue.addMessage(message);
    }

    @RequestMapping(value = "/getUsers")
    @ResponseBody
    public Map<String,Object> getUsers(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("key");
        String type = httpServletRequest.getParameter("type");
        return manageService.getUsers(key, type);
    }

    @RequestMapping(value = "/modifyAuth")
    @ResponseBody
    public void modifyAuth(User user) {
        manageService.modifyAuth(user);
    }

    @RequestMapping(value = "/getMessageType")
    @ResponseBody
    public Map<String,Object> getMessageType(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("key");
        return manageService.getMessageType(key);
    }

    @RequestMapping(value = "/modifyType")
    @ResponseBody
    public void modifyType(HttpServletRequest httpServletRequest) {
        String id = httpServletRequest.getParameter("id");
        String name = httpServletRequest.getParameter("name");
        String operation = httpServletRequest.getParameter("operation");
        manageService.modifyType(id, name, operation);
    }
}
