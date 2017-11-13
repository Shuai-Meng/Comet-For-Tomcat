package manage.controller;

import manage.service.SubscribeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author mengshuai
 */
@Controller
@RequestMapping("/manage")
public class SubscribeController {
    @Resource
    private SubscribeService subscribeService;

    @RequestMapping(value = "/modifySubscribe")
    @ResponseBody
    public void subscribe(HttpServletRequest httpServletRequest) {
        String typeId = httpServletRequest.getParameter("id");
        String operation = httpServletRequest.getParameter("operation");
        subscribeService.subscribe(typeId, operation);
    }

    @RequestMapping(value = "/getSubscribeType")
    @ResponseBody
    public Map<String,Object> getSubscribeType(HttpServletRequest httpServletRequest) {
        String key = httpServletRequest.getParameter("name");
        String page = httpServletRequest.getParameter("page");
        String rows = httpServletRequest.getParameter("rows");
        return subscribeService.getSubscribeType(page, rows, key);
    }
}
