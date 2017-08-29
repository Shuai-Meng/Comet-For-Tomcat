package comet;

/**
 * Created by mengshuai on 2017/8/28.
 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext*.xml", "classpath:spring/applicationContext-mybatis.xml"})
public class TestHandler {
    @Test
    public void testTime()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class c = DelayedMessageHandler.class;
        Method method = c.getDeclaredMethod("getNextMinute", null);
        method.setAccessible(true);
        Date date = (Date) method.invoke(DelayedMessageHandler.getSingleInstance(), new Object[]{});
        System.out.println(date);
        method.setAccessible(false);
    }
}
