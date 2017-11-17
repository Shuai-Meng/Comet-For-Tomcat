package comet;

import manage.vo.Message;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author mengshuai
 */
class DistributeMessage implements Callable<Map<Integer, List<Message>>> {
    private List<Integer> userList;
    private Message message;

    DistributeMessage(List<Integer> userList, Message message) {
        this.userList = userList;
        this.message = message;
    }

    @Override public Map<Integer, List<Message>> call() {
        Map<Integer, List<Message>> result = new HashMap<Integer, List<Message>>(userList.size());
        for(int userId : userList) {
            List<Message> tmp = new ArrayList<Message>();
            tmp.add(message);
            result.put(userId, tmp);
        }
        return result;
    }
}
