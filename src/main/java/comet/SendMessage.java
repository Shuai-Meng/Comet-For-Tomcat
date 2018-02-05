package comet;

import com.fasterxml.jackson.databind.ObjectMapper;
import manage.service.MessageService;
import manage.vo.Message;
import org.apache.catalina.comet.CometEvent;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author mengshuai
 */
public class SendMessage implements Runnable {
    private static Map<Integer, List<CometEvent>> cometContainer = ConnectionManager.getContainer();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private List<Integer>               userList;
    private Map<Integer, List<Message>> map;
    private MessageService              messageService;

    SendMessage(List<Integer> userList, Map<Integer, List<Message>> map, MessageService messageService) {
        this.userList = userList;
        this.map = map;
        this.messageService = messageService;
    }

    @Override public void run() {
        for (int userId : userList) {
            List<Message> messageList = map.get(userId);

            List<CometEvent> connectionList = cometContainer.get(userId);
            if (connectionList == null) {
                messageService.insertReceive(userId, messageList, false);
            } else {
                send(connectionList, messageList);
            }

            messageService.insertReceive(userId, messageList, true);
        }
    }

    private void send(List<CometEvent> connectionList, List<Message> messageList) {
        try {
            for (CometEvent event : connectionList) {
                ServletResponse response = event.getHttpServletResponse();
                response.setCharacterEncoding("UTF-8");

                PrintWriter writer = response.getWriter();
                writer.println(objectMapper.writeValueAsString(messageList));
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
