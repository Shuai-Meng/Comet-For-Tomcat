package comet;

import org.apache.catalina.comet.CometEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by m on 17-5-4.
 */
public class Container {
    private static Map<Integer, CometEvent> container = new ConcurrentHashMap<Integer, CometEvent>();

    private Container() {}

    public static Map<Integer, CometEvent> getContainer() {
        return container;
    }
}
