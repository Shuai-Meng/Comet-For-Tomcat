package comet;

import org.apache.catalina.comet.CometEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by m on 17-5-4.
 */
public class ConnectionManager {
    private static Map<Integer, List<CometEvent>> container
            = new ConcurrentHashMap<Integer, List<CometEvent>>();

    private ConnectionManager() {}

    public static Map<Integer, List<CometEvent>> getContainer() {
        return container;
    }
}
