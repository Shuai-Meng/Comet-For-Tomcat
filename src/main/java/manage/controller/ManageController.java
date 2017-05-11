package manage.controller;

import comet.ImmediateQueue;
import comet.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by m on 17-5-4.
 */
@Controller
@RequestMapping("/manage")
public class ManageController {

    @RequestMapping(value = "/home")
    public ModelAndView getHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

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
        ImmediateQueue immediateQueue = (ImmediateQueue)servletContext.getAttribute("immediateQueue");
        immediateQueue.addMessage(message);
    }
}
