package manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import utils.SpringSecurityUtil;

import static constants.Constants.ROLES;

/**
 *
 * @author shuaimeng
 * @date 17-5-4
 */
@Controller
@RequestMapping("/manage")
public class ManageController {
    @RequestMapping(value = "/home")
    public ModelAndView getHomePage() {
        if (ROLES.contains(SpringSecurityUtil.getRole())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("admin");
            modelAndView.addObject("userName", SpringSecurityUtil.getUserName());
            modelAndView.addObject("userRole", SpringSecurityUtil.getRole());
            return modelAndView;
        } else {
            return null;
        }
    }
}
