package jeff.demo;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.sift.MDCBasedDiscriminator;
import ch.qos.logback.classic.sift.SiftingAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class LogUtil {
	private static String SIFTING_APPENDER_NAME = "SIFT";
	public static String DISCRIMINATOR_KEY = "logFileName";

	public synchronized static void addSiftingAppender(org.slf4j.Logger log) {
		// check whether this logger is logback logger
		if (!isLogbackInstance()) {
			return;
		}

		// check whether this logger have Sift Appender
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger logger = lc.getLogger(log.getName());
		if (logger.getAppender(SIFTING_APPENDER_NAME) != null) {
			return;
		}

		// Add new discriminator to Sift Appender
		SiftingAppender sa = new SiftingAppender();
		sa.setName(SIFTING_APPENDER_NAME);
		sa.setContext(lc);
		MDCBasedDiscriminator discriminator = new MDCBasedDiscriminator();
		discriminator.setKey(DISCRIMINATOR_KEY);
		discriminator.setDefaultValue("unknown");
		discriminator.start();
		sa.setDiscriminator(discriminator);

		// Appender Factory will create a new File Appender for this discriminator
		sa.setAppenderFactory((Context context, String discriminatingValue) -> {
				FileAppender<ILoggingEvent> appender = new FileAppender<>();
				appender.setName(discriminatingValue);
				appender.setContext(context);
				appender.setFile("log/" + discriminatingValue + ".log");
				appender.setAppend(true);
				if (appender.isStarted()) {
					appender.stop();
				}
				PatternLayoutEncoder pl = new PatternLayoutEncoder();
				pl.setContext(context);
				pl.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
				pl.setImmediateFlush(true);
				if (pl.isStarted()) {
					pl.stop();
				}
				pl.setCharset(Charset.forName("UTF-8"));
				pl.start();
				appender.setEncoder(pl);
				appender.start();
				return appender;
		});

		// Start this Sift Appender
		if (sa.isStarted()) {
			sa.stop();
		}
		sa.start();
		logger.addAppender(sa);
	}

	public static void stopFileAppenderInSiftingAppender(String key, org.slf4j.Logger logger) {
		// check whether this logger is logback logger
		if (!isLogbackInstance()) {
			return;
		}

		// Get File Appender and stop
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		Logger log = lc.getLogger(logger.getName());
		SiftingAppender sa = (SiftingAppender) log.getAppender(SIFTING_APPENDER_NAME);
		if (sa == null) {
			return;
		}
		FileAppender<?> fa = (FileAppender<?>) sa.getAppenderTracker().find(key);
		if (fa != null) {
			fa.getEncoder().stop();
			fa.stop();
		}
		sa.getAppenderTracker().removeStaleComponents(System.currentTimeMillis());
	}

	public static boolean isLogbackInstance() {
		return LoggerFactory.getILoggerFactory() instanceof LoggerContext;
	}

}
