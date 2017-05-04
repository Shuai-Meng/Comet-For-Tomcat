package comet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by m on 17-5-4.
 */
public class Container {
    private static Map<String, Connection> container = new ConcurrentHashMap<String, Connection>();

    private Container() {}

    public static Map<String, Connection> getContainer() {
        return container;
    }
}
