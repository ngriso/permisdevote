package p2v;

import org.junit.ClassRule;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Handler;
import java.util.logging.LogManager;

public class AbstractTest {
    @ClassRule
    public static final EnvironmentRule environment = new EnvironmentRule(true, 18080, "src/main/webapp");

    public static interface URLS {
        final String BASE = "http://localhost:" + environment.getPort() + "/api";
    }

    static {
        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        SLF4JBridgeHandler.install();
    }

}
