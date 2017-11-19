package comet;

import com.fasterxml.jackson.databind.ObjectMapper;
import manage.mapper.MessageMapper;
import manage.service.RecordService;
import manage.vo.Message;
import org.apache.catalina.comet.CometEvent;
import utils.SpringUtil;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author mengshuai
 */
public class SendMessage implements Runnable {
    private static Map<Integer, List<CometEvent>> cometContainer = ConnectionManager.getContainer();
    private static ObjectMapper objectMapper = new ObjectMapper();
    private List<Integer> userList;
    private Map<Integer, List<Message>> map;
    private MessageMapper messageMapper;
    private RecordService recordService;

    SendMessage(List<Integer> userList, Map<Integer, List<Message>> map,
                MessageMapper messageMapper, RecordService recordService) {
        this.userList = userList;
        this.map = map;
        this.messageMapper = messageMapper;
        this.recordService = recordService;
    }

    private void storeUnreadMessage(int userId, List<Message> list) {
        for (Message message : list) {
            Map<String, Integer> map = new HashMap<String, Integer>(2);
            map.put("userId", userId);
            map.put("messageId", message.getId());
            //TODO batch
            messageMapper.insertUnread(map);
        }
    }

    @Override public void run() {
        for (int userId : userList) {
            List<Message> messageList = map.get(userId);

            List<CometEvent> connectionList = cometContainer.get(userId);
            if (connectionList == null) {
                storeUnreadMessage(userId, messageList);
            } else {
                send(connectionList, messageList);
            }

            recordService.insertRecord(userId, getMessageId(messageList));
        }
    }

    private List<Integer> getMessageId(List<Message> messageList) {
        List<Integer> list = new ArrayList<Integer>();
        for (Message message : messageList) {
            list.add(message.getId());
        }
        return list;
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
