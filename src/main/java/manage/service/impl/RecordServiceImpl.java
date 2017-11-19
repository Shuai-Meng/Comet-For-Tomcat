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

    @Override public void insertRecord(int userId, List<Integer> messageList) {
        MyUser user = new MyUser();
        user.setId(userId);
        List<Integer> list = getRecord(userId);
        if (list.isEmpty()) {
            list.addAll(messageList);
            user.setMessageList(list.toString());
            recordMapper.insertRecord(user);
        } else {
            list.addAll(messageList);
            user.setMessageList(list.toString());
            recordMapper.updateRecord(user);
        }
    }

    @Override public List<Integer> getRecord(int userId) {
        String messageList = recordMapper.getMessageList(userId);
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
}
