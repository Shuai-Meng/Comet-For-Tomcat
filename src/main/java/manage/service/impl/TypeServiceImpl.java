package manage.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import manage.mapper.SubscribeMapper;
import manage.mapper.TypeMapper;
import manage.service.TypeService;
import manage.vo.MessageType;
import manage.vo.Subscribe;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author mengshuai
 */
@Service
public class TypeServiceImpl implements TypeService {
    @Resource
    private TypeMapper      typeMapper;
    @Resource
    private SubscribeMapper subscribeMapper;

    @Override public void modifyType(String id, String name, String operation) {
        MessageType messageType = new MessageType();
        messageType.setName(name);

        if("delete".equals(operation)) {
            typeMapper.deleteByPrimaryKey(Integer.valueOf(id));
        } else if("update".equals(operation)) {
            Example example = new Example(MessageType.class);
            example.createCriteria().andEqualTo("id", Integer.valueOf(id));

            typeMapper.updateByExampleSelective(messageType, example);
        } else {
            typeMapper.insertSelective(messageType);
        }
    }

    @Override public List<MessageType> getAllTypes() {
        return typeMapper.selectAll();
    }

    @Override public Map<String, Object> getMessageType(Map<String, String> param) {
        Example example = new Example(MessageType.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(param.get("name"))) {
            criteria.andEqualTo("name", param.get("name"));
        }

        List<Integer> subscribed = getSubscribed(Integer.parseInt(param.get("userId")));
        String status = param.get("status");
        if ("subscribed".equals(status)) {//TODO
            criteria.andIn("id", subscribed);
        } else if ("unsubscribed".equals(status)) {
            criteria.andNotIn("id", subscribed);
        }

        PageHelper.startPage(Integer.parseInt(param.get("page")), Integer.parseInt(param.get("rows")));
        List<MessageType> list = typeMapper.selectByExample(example);
        for (MessageType messageType : list) {
            if (subscribed.contains(messageType.getId())) {
                messageType.setStatus((byte) 1);//TODO
            } else {
                messageType.setStatus((byte) 0);
            }
        }

        Map<String,Object> res = new HashMap<String,Object>(2);
        res.put("total", ((Page)list).getTotal());
        res.put("rows", list);
        return res;
    }

    @Override public List<Integer> getSubscribed(int userId) {
        Example example = new Example(Subscribe.class);
        example.createCriteria().andEqualTo("userId", userId);

        List<Integer> subscribed = new ArrayList<Integer>();
        for (Subscribe subscribe : subscribeMapper.selectByExample(example)) {
            subscribed.add(subscribe.getTypeId());
        }

        return subscribed;
    }

    @Override public void addMessageType(MessageType messageType) {
        typeMapper.insertSelective(messageType);
    }

    @Override public void modifySubscribe(int userId, String typeId, String operation) {
        if(operation.equals("sub")) {//TODO
            Subscribe subscribe = new Subscribe();
            subscribe.setUserId(userId);
            subscribe.setTypeId(Integer.parseInt(typeId));
            subscribeMapper.insertSelective(subscribe);
        } else if ("pub".equals(operation)){
            Example example = new Example(Subscribe.class);
            example.createCriteria().andEqualTo("userId", userId)
                    .andEqualTo("type_id", typeId);
            subscribeMapper.deleteByExample(example);
        }
    }
}
