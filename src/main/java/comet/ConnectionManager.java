package comet;

import org.apache.catalina.comet.CometEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by m on 17-5-4.
 */
public class ConnectionManager {
    private static Map<Integer, CometEvent> container = new ConcurrentHashMap<Integer, CometEvent>();

    private ConnectionManager() {}

    public static Map<Integer, CometEvent> getContainer() {
        return container;
    }
}
