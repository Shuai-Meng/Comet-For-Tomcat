package manage.controller;

import comet.Connection;
import comet.Container;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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

        Map<String, Connection> container = Container.getContainer();
        for(Connection connection : container.values()) {
            System.out.println(msg);
            connection.returnResponse(msg);
        }
    }
}
