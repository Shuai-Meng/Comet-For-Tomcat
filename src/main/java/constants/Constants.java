package constants;

import java.util.*;

/**
 *
 * @author mengshuai
 * @date 2017/10/17
 */
public class Constants {
    public static final String       ROLE_ADMIN = "ROLE_ADMIN";
    public static final String       ROLE_PUB   = "ROLE_PUB";
    public static final String       ROLE_SUB   = "ROLE_SUB";
    public static final List<String> ROLES      = new ArrayList<String>(3);

    public static final String SENDING_LIST = "sending";
    public static final int LIST_VOLUME = 3;
    public static final int N_CPU = Runtime.getRuntime().availableProcessors();

    static {
        ROLES.add(ROLE_ADMIN);
        ROLES.add(ROLE_PUB);
        ROLES.add(ROLE_SUB);
    }
}
