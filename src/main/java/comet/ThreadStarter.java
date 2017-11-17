package comet;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.*;

import static constants.Constants.N_CPU;

/**
 * @author mengshuai
 */
public class ThreadStarter implements ApplicationListener<ContextRefreshedEvent> {
    public static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(N_CPU,
            N_CPU * 2,
            1, TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<Runnable>());

    @Override public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if ("Root WebApplicationContext".equals(contextRefreshedEvent.getApplicationContext().getDisplayName())) {
            THREAD_POOL.execute(MessageQueue.getSingleInstance());
        }
    }
}
