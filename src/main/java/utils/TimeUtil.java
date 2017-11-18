package utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @author mengshuai
 */
public class TimeUtil {
    public static Date getNow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
