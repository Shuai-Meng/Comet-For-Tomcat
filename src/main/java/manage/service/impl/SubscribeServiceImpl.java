package manage.service.impl;

import manage.mapper.SubscribeMapper;
import manage.service.SubscribeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mengshuai
 */
@Service
public class SubscribeServiceImpl extends BaseService implements SubscribeService {
    @Resource
    private SubscribeMapper subscribeMapper;

    @Override public Map<String, Object> getSubscribeType(String page, String rows, String key) {
        Map<String, Object> param = getPagination(page, rows);
        param.put("name", key);
        param.put("id", getUser().getUserId());

        Map<String,Object> res = new HashMap<String,Object>();
        res.put("rows", subscribeMapper.getSubscribeType(param));
        res.put("total", subscribeMapper.getSubscribeTypeCount(key));
        return res;
    }

    @Override public void subscribe(String typeId, String operation) {
        Map<String, Integer> map = new HashMap<String, Integer>(2);
        map.put("typeId", Integer.valueOf(typeId));
        map.put("userId", getUser().getUserId());

        if(operation.equals("sub")) {
            subscribeMapper.subscribe(map);
        } else {
            subscribeMapper.unSubscribe(map);
        }
    }
}
