package comet;

import manage.dao.MessageMapper;
import manage.vo.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import utils.SpringUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by m on 17-8-22.
 */
@Component
public class DelayedMessageHandler {
    @Resource
    private MessageMapper messageMapper;
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    /*private DelayedMessageHandler() {
        messageMapper = (MessageMapper) SpringUtil.getBean("messageMapper");
    }

    public static DelayedMessageHandler getSingleInstance(ExecutorService executorService) {
        DelayedMessageHandler.executorService = executorService;
        return delayedMessageHandler;
    }*/

    private Date getNextMinute() {
        Date res = new Date();
        long nextMin = res.getTime() / (1000 * 60) + 1;
        res.setTime(nextMin * 1000 * 60);
        System.out.println("getting msg from database: " + new Date() + " - " + res);
        return res;
    }

    private List<Message> getMessageFromDataBase(Date nextMin) {
        return messageMapper.getMessagesOfNextMin(nextMin);
    }

    @Scheduled(cron = "0 * * * * ?")
    private void pushMessageToQueue() throws InterruptedException {
        Date nextMin = getNextMinute();
        executorService.execute(new Pusher(nextMin, getMessageFromDataBase(nextMin)));
    }

    class Pusher implements Runnable {
        private Date nextMin;
        private List<Message> list;

        public Pusher(Date nextMin, List<Message> list) {
            this.nextMin = nextMin;
            this.list = list;
        }

        public void run() {
            try {
                System.out.println("pushing:" + new Date());
                long interval = nextMin.getTime() - new Date().getTime();
                if (interval > 0) {
                    Thread.sleep(interval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MessageQueue messageQueue = MessageQueue.getSingleInstance();
            for (Message message : list) {
                messageQueue.addMessage(message);
            }
            list.clear();
        }
    }
}
