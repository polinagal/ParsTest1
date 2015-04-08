/*
 * $Id: LoggingUtils.java 17794 2010-07-12 15:52:18Z mm $
 */
package s2a.util;

import java.util.logging.Logger;

/**
 * Функции-утилиты, связанные с протоколированием.
 *
 * @author Mikhail Glukhikh <glukhikh@mail.ru>
 */
public class LoggingUtils {
    // Логгер
    private final static Logger logger =
            Logger.getLogger(LoggingUtils.class.getName());

    /**
     * Отладочный вывод затрат памяти.
     */
    public static void flushStatistics() {
        final long totalCFGMemory = Runtime.getRuntime().totalMemory() / 1024;
        final long freeJVMMemory = Runtime.getRuntime().freeMemory() / 1024;
        final long maxJVMMemory = Runtime.getRuntime().maxMemory() / 1024;
        logger.info("Общий объем памяти на JVM: " + totalCFGMemory);
        logger.info("Свободная память JVM: " + freeJVMMemory);
        logger.info("Максимум памяти для JVM: " + maxJVMMemory);
    }
}
