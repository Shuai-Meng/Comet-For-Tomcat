package comet;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ThreadStarter implements ApplicationListener<ContextRefreshedEvent> {

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(MessageQueue.getSingleInstance()).start();
        new Thread(DelayedMessageHandler.getSingleInstance()).start();
    }
}
