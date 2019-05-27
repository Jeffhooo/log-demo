package jeff.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogDemo {

    private static final Logger logger = LoggerFactory.getLogger(LogDemo.class);

    public static void main(String[] args) {

        new Thread(() -> {
            LogUtil.addSiftingAppender(logger);
            MDC.put(LogUtil.DISCRIMINATOR_KEY, "thread-0");
            logger.trace("thread-0: Hello World!");
            logger.debug("thread-0: How are you today?");
            logger.info("thread-0: I am fine.");
            logger.warn("thread-0: I love programming.");
            logger.error("thread-0: I am programming.");
            MDC.remove(LogUtil.DISCRIMINATOR_KEY);
            LogUtil.stopFileAppenderInSiftingAppender("thread-0", logger);
        }).start();

        new Thread(() -> {
            LogUtil.addSiftingAppender(logger);
            MDC.put(LogUtil.DISCRIMINATOR_KEY, "thread-1");
            logger.trace("thread-1: Hello World!");
            logger.debug("thread-1: How are you today?");
            logger.info("thread-1: I am fine.");
            logger.warn("thread-1: I love programming.");
            logger.error("thread-1: I am programming.");
            MDC.remove(LogUtil.DISCRIMINATOR_KEY);
            LogUtil.stopFileAppenderInSiftingAppender("thread-1", logger);
        }).start();

        LogUtil.addSiftingAppender(logger);
        MDC.put(LogUtil.DISCRIMINATOR_KEY, "main");
        logger.trace("main: Hello World!");
        logger.debug("main: How are you today?");
        logger.info("main: I am fine.");
        logger.warn("main: I love programming.");
        logger.error("main: I am programming.");
        MDC.remove(LogUtil.DISCRIMINATOR_KEY);
        LogUtil.stopFileAppenderInSiftingAppender("main", logger);

    }
}
