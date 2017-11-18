package manage.controller;

import manage.vo.SecurityUser;
import org.springframework.security.core.GrantedAuthority;
import utils.SpringSecurityUtil;

/**
 * @author mengshuai
 */
public class BaseController {
    protected SecurityUser getUser() {
        return SpringSecurityUtil.getCurrentUser();
    }

    protected String getRole() {
        Object[] roles = getUser().getAuthorities().toArray();
        return ((GrantedAuthority) roles[0]).getAuthority();
    }

    protected String getUserName() {
        return getUser().getUsername();
    }

    protected int getUserId() {
        return getUser().getUserId();
    }
}
