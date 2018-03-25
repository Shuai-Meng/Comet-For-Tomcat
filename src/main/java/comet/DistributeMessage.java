package comet;

import manage.vo.MyMessage;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author mengshuai
 */
class DistributeMessage implements Callable<Map<Integer, List<MyMessage>>> {
    private List<Integer> userList;
    private MyMessage     message;

    DistributeMessage(List<Integer> userList, MyMessage message) {
        this.userList = userList;
        this.message = message;
    }

    @Override public Map<Integer, List<MyMessage>> call() {
        Map<Integer, List<MyMessage>> result = new HashMap<Integer, List<MyMessage>>(userList.size());
        for(int userId : userList) {
            List<MyMessage> tmp = new ArrayList<MyMessage>();
            tmp.add(message);
            result.put(userId, tmp);
        }
        return result;
    }
}
