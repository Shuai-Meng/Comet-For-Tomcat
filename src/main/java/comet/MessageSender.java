package comet;

import com.fasterxml.jackson.databind.ObjectMapper;
import manage.service.MessageService;
import manage.service.TypeService;
import manage.vo.MyMessage;
import org.apache.catalina.comet.CometEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.*;

@Component
public class MessageSender {

    private static Map<Integer, List<CometEvent>> cometContainer = ConnectionManager.getContainer();
    private static ObjectMapper                   objectMapper   = new ObjectMapper();
    @Resource
    private TypeService    typeService;
    @Resource
    private MessageService messageService;

    public void sendMessage(MyMessage myMessage) {
        if (myMessage == null) {
            return;
        }

        List<Integer> userList = typeService.getSubscribed(myMessage.getType());

        for (int user : userList) {
            List<CometEvent> connectionList = cometContainer.get(user);

            if (connectionList == null) {
//                messageService.insertReceive(user, myMessage, false);
            } else {
                send(connectionList, myMessage);
            }

//            messageService.insertReceive(userId, messageList, true);
        }

        messageService.markAsPublished(myMessage.getId());
    }

    private void send(List<CometEvent> connectionList, MyMessage myMessage) {
        try {
            for (CometEvent event : connectionList) {
                ServletResponse response = event.getHttpServletResponse();
                response.setCharacterEncoding("UTF-8");

                PrintWriter writer = response.getWriter();
                writer.println(objectMapper.writeValueAsString(myMessage));
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
