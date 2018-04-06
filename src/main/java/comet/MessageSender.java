package comet;

import org.apache.catalina.comet.CometEvent;
import org.springframework.stereotype.Component;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author mengshuai
 */
@Component
public class MessageSender {
    public void sendMessage(String message, List<CometEvent> userList) {
        for (CometEvent user : userList) {
            doSend(user, message);
        }
    }

    private void doSend(CometEvent event, String message) {
        try {
            ServletResponse response = event.getHttpServletResponse();
            response.setCharacterEncoding("UTF-8");

            PrintWriter writer = response.getWriter();
            writer.println(message);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
