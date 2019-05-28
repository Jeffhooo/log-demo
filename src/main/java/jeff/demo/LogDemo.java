package jeff.demo;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogDemo {

    private static final Logger log = LoggerFactory.getLogger(LogDemo.class.getName());

    public static void main(String[] args) {

        // add a Sift Appender to the logger
        LogUtil.addSiftingAppender(log);

        new Thread(() -> {
            // add a MDC key value pair
            MDC.put(LogUtil.DISCRIMINATOR_KEY, "thread-1");

            // start log
            log.trace("thread-1: Hello World!");
            log.debug("thread-1: How are you today?");
            log.info("thread-1: I am fine.");
            log.warn("thread-1: I love programming.");
            log.error("thread-1: I am programming.");

            // remove MDC key value and stop file appender
            MDC.remove(LogUtil.DISCRIMINATOR_KEY);
            LogUtil.stopFileAppenderInSiftingAppender("thread-1", log);
        }).start();

        new Thread(() -> {
            // add a MDC key value pair
            LogUtil.addSiftingAppender(log);
            MDC.put(LogUtil.DISCRIMINATOR_KEY, "thread-2");

            // start log
            log.trace("thread-2: Hello World!");
            log.debug("thread-2: How are you today?");
            log.info("thread-2: I am fine.");
            log.warn("thread-2: I love programming.");
            log.error("thread-2: I am programming.");

            // remove MDC key value and stop file appender
            MDC.remove(LogUtil.DISCRIMINATOR_KEY);
            LogUtil.stopFileAppenderInSiftingAppender("thread-2", log);
        }).start();


        // add a MDC key value pair
        MDC.put(LogUtil.DISCRIMINATOR_KEY, "main");

        // start log
        log.trace("main: Hello World!");
        log.debug("main: How are you today?");
        log.info("main: I am fine.");
        log.warn("main: I love programming.");
        log.error("main: I am programming.");

        // remove MDC key value and stop file appender
        MDC.remove(LogUtil.DISCRIMINATOR_KEY);
        LogUtil.stopFileAppenderInSiftingAppender("main", log);

    }
}
