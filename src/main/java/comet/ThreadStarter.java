package comet;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadStarter implements ApplicationListener<ContextRefreshedEvent> {
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            executorService.execute(MessageQueue.getSingleInstance());
        }
    }
}
