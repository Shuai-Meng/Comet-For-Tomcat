package manage.vo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * Created by shuaimeng2 on 2017/5/31.
 */
public class SecurityUser extends User{
    private int id;

    @SuppressWarnings("deprecation")
    public SecurityUser(String username, String password, List<GrantedAuthority> authorities)
            throws IllegalArgumentException {
        super(username,password, authorities);
    }

    public int getUserId() {
        return id;
    }

    public void setUserId(int userId) {
        this.id = userId;
    }
}
