package comet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import manage.service.*;
import manage.vo.*;
import org.apache.catalina.comet.CometEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.*;

/**
 * @author mengshuai
 */
@Component
public class MyMessageListener implements MessageListener{
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Map<Integer, CometEvent> cometContainer = ConnectionManager.getContainer();
    @Autowired
    private MessageSender  messageSender;
    @Resource
    private MessageService messageService;
    @Resource
    private UserService    userService;

    @Override public void onMessage(Message message) {
        String messageStr = null;
        MyMessage myMessage = null;

        try {
            ObjectMessage objectMessage = (ObjectMessage) message;
            myMessage = (MyMessage) objectMessage.getObject();
            messageStr = objectMapper.writeValueAsString(myMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (myMessage != null) {
            List<MyUser> userList = userService.getDepartmentUsers(myMessage.getRange());

            messageSender.sendMessage(messageStr, getOnlineUsers(userList));

            messageService.markAsPublished(myMessage.getId());
            for (MyUser user : userList) {
                messageService.insertReceive(myMessage.getId(), user);
            }
        }
    }

    private List<CometEvent> getOnlineUsers(List<MyUser> list) {
        List<CometEvent> result = new ArrayList<CometEvent>();

        for (MyUser user : list) {
            CometEvent connection = cometContainer.get(user.getId());
            if (connection != null) {
                user.setOnline(true);
                result.add(connection);
            }
        }

        return result;
    }
}
