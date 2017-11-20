package manage.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import manage.mapper.RecordMapper;
import manage.service.RecordService;
import manage.vo.MyUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * @author mengshuai
 */
@Service
public class RecordServiceImpl implements RecordService {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private RecordMapper recordMapper;

    @Override public void insertSubRecord(int userId, List<Integer> messageList) {
        MyUser user = new MyUser();
        user.setId(userId);
        List<Integer> list = getSubRecord(userId);
        if (list.isEmpty()) {
            list.addAll(messageList);
            user.setMessageList(list.toString());
            recordMapper.insertSubRecord(user);
        } else {
            list.addAll(messageList);
            user.setMessageList(list.toString());
            recordMapper.updateSubRecord(user);
        }
    }

    @Override public List<Integer> getSubRecord(int userId) {
        String messageList = recordMapper.getSubMessageList(userId);
        if (messageList == null || "".equals(messageList)) {
            return new ArrayList<Integer>(1);
        }

        try {
            return objectMapper.readValue(messageList, ArrayList.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override public void insertPubRecord(int userId, int messageId) {
        Map<String, Integer> param = new HashMap<String, Integer>(2);
        param.put("userId", userId);
        param.put("messageId", messageId);
        recordMapper.insertPubRecord(param);
    }

    @Override public void deletePubRecord(int userId, int messageId) {
        Map<String, Integer> param = new HashMap<String, Integer>(2);
        param.put("userId", userId);
        param.put("messageId", messageId);
        recordMapper.deletePubRecord(param);
    }
}
