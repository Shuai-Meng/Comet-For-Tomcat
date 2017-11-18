package manage.controller;

import manage.vo.SecurityUser;
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
public class ManageController extends BaseController {
    @RequestMapping(value = "/home")
    public ModelAndView getHomePage() {
        SecurityUser user = SpringSecurityUtil.getCurrentUser();
        if (ROLES.contains(user.getAuthorities())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("admin");
            modelAndView.addObject("userName", getUser().getUsername());
            modelAndView.addObject("userRole", getUser().getAuthorities());
            return modelAndView;
        } else {
            return null;
        }
    }
}
