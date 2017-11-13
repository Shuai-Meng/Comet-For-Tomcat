package manage.service.impl;

import manage.vo.SecurityUser;
import utils.SpringSecurityUtil;

import java.util.*;

/**
 *
 * @author shuaimeng
 * @date 17-5-20
 */
public class BaseService {
    protected Map<String, Object> getPagination(String page, String rows) {
        int pageInt = page == null ? 1 : Integer.parseInt(page);
        int rowsInt = rows == null ? 10 : Integer.parseInt(rows);
        int offset = (pageInt - 1) * rowsInt;

        Map<String,Object> param = new HashMap<String,Object>(2);
        param.put("offset", offset);
        param.put("size", rowsInt);
        return param;
    }

    protected SecurityUser getUser() {
        return SpringSecurityUtil.getCurrentUser();
    }
}
