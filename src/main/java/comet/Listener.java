package comet;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class Listener implements ApplicationListener<ContextRefreshedEvent> {

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(MessageQueue.getOnlyInstance()).start();
    }
}
