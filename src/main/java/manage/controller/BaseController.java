package manage.controller;

import manage.vo.SecurityUser;
import utils.SpringSecurityUtil;

/**
 * @author mengshuai
 */
public class BaseController {
    protected SecurityUser getUser() {
        return SpringSecurityUtil.getCurrentUser();
    }
}
