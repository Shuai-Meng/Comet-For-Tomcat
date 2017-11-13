package manage.service.impl;

import manage.mapper.TypeMapper;
import manage.service.TypeService;
import manage.vo.MessageType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mengshuai
 */
@Service
public class TypeServiceImpl extends BaseService implements TypeService {
    @Resource
    private TypeMapper typeMapper;

    @Override public void modifyType(String id, String name, String operation) {
        if("delete".equals(operation)) {
            typeMapper.deleteType(Integer.valueOf(id));
        } else if("update".equals(operation)) {
            MessageType messageType = new MessageType();
            messageType.setId(Integer.valueOf(id));
            messageType.setName(name);
            typeMapper.updateType(messageType);
        } else {
            typeMapper.insertType(name);
        }
    }

    @Override public Map<String, Object> getMessageType(String key, String page, String rows) {
        Map<String,Object> res = new HashMap<String,Object>(2);

        Map<String, Object> param = getPagination(page, rows);
        param.put("name", key);
        res.put("total", typeMapper.getTypeCount(key));
        res.put("rows", typeMapper.getTypeRows(param));
        return res;
    }

    @Override public List<MessageType> getMessageTypes() {
        return typeMapper.getAllTypes();
    }

    @Override public void addMessageType(MessageType messageType) {
        typeMapper.insertType(messageType.getName());
    }
}
