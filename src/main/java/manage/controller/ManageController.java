package manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static constants.Constants.ROLES;

/**
 *
 * @author shuaimeng
 * @date 17-5-4
 */
@Controller
@RequestMapping("/manage")
public class ManageController extends BaseController {
    @RequestMapping(value = "/home")
    public ModelAndView getHomePage() {
        if (ROLES.contains(getRole())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("admin");
            modelAndView.addObject("userName", getUserName());
            modelAndView.addObject("userRole", getRole());
            return modelAndView;
        } else {
            return null;
        }
    }
}
