package manage.controller;

import manage.vo.SecurityUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import utils.SpringSecurityUtil;

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
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");

        SecurityUser user = SpringSecurityUtil.getCurrentUser();
        modelAndView.addObject("userName", user.getUsername());
        modelAndView.addObject("userRole", user.getAuthorities());

        return modelAndView;
    }
}
