/*
 * $Id: S2AMemoryHandler.java 14714 2009-12-15 15:15:14Z akhin $
 */
package s2a.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.MemoryHandler;

/**
 * MemoryHandler для s2a.
 *
 * @author Marat Kh. Akhin
 */
public class S2AMemoryHandler extends MemoryHandler {
    private int recordCount = 0;
    private int pushThreshold;

    static {
        addShutdownHooks();
    }

    public S2AMemoryHandler() {
        super();
        setup();
    }

    public S2AMemoryHandler(final Handler target,
                            final int size,
                            final Level pushLevel) {
        super(target, size, pushLevel);
        setup();
    }

    private void setup() {
        final LogManager manager = LogManager.getLogManager();
        final String cname = getClass().getName();

        final String sPushThreshold = manager.getProperty(cname + ".pushThreshold");
        pushThreshold =
                sPushThreshold != null ? Integer.valueOf(sPushThreshold) : 1;
        pushThreshold =
                pushThreshold > 0 ? pushThreshold : 1;
    }

    @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
    @Override
    public synchronized void publish(final LogRecord record) {
        super.publish(record);

        recordCount++;
        if (recordCount >= pushThreshold) {
            push();
        }
    }

    @SuppressWarnings("PMD.AvoidSynchronizedAtMethodLevel")
    @Override
    public synchronized void push() {
        super.push();
        recordCount = 0;
    }

    private static void addShutdownHooks() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (Handler handler : Logger.getLogger("").getHandlers()) {
                    if (handler instanceof MemoryHandler) {
                        ((MemoryHandler) handler).push();
                    }
                }
            }
        });
    }
}
