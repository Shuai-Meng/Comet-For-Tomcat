package manage.controller;

import manage.service.TypeService;
import manage.vo.MessageType;
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
public class TypeController {
    @Resource
    private TypeService typeService;

    @RequestMapping(value = "/getMessageTypes")
    @ResponseBody
    public List<MessageType> getMessageTypes() {
        return typeService.getMessageTypes();
    }

    @RequestMapping(value = "/getMessageType")
    @ResponseBody
    public Map<String,Object> getMessageType(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("name");
        String page = httpServletRequest.getParameter("page");
        String rows = httpServletRequest.getParameter("rows");
        return typeService.getMessageType(key, page, rows);
    }

    @RequestMapping(value = "/addType")
    @ResponseBody
    public void addType(MessageType messageType) {
        typeService.addMessageType(messageType);
    }

    @RequestMapping(value = "/modifyType")
    @ResponseBody
    public void modifyType(HttpServletRequest httpServletRequest) {
        String id = httpServletRequest.getParameter("id");
        String name = httpServletRequest.getParameter("name");
        String operation = httpServletRequest.getParameter("operation");
        typeService.modifyType(id, name, operation);
    }
}
