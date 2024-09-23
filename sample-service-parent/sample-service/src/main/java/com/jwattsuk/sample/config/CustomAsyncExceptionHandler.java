package com.jwattsuk.sample.config;

import com.codahale.metrics.SlidingTimeWindowReservoir;
import com.jwattsuk.sample.services.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * This class handles thread crashes, and will attempt to restart the crashed threads up to a limit defined by
 * the system property thread.crash-limit
 */
public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);
    private static SlidingTimeWindowReservoir messageListenerCrashes =
            new SlidingTimeWindowReservoir(1, TimeUnit.MINUTES);
    private final Integer crashLimit;
    private ApplicationContext applicationContext;

    public CustomAsyncExceptionHandler(final ApplicationContext applicationContext, Integer crashLimit) {
        this.applicationContext = applicationContext;
        this.crashLimit = crashLimit;
    }

    void exitSystem() {
        System.exit(-1);
    }

    /**
     * Handles an uncaught exception within an @Async call and will attempt a thread restart
     *
     * @param throwable the exception that caused the thread death
     * @param method    the @Async method that failed
     * @param objects   any parameters that were used to start the thread
     */
    @Override
    public void handleUncaughtException(final Throwable throwable, final Method method,
                                        final Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.getDeclaringClass().getSimpleName()).append(".").append(method.getName());
        stringBuilder.append("(");
        if (objects != null) {
            for (Object param : objects) {
                stringBuilder.append(param).append(", ");
            }
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        }
        stringBuilder.append(")");
        LOG.error("{} thread died", stringBuilder, throwable);

        if (method.getDeclaringClass() == MessageListener.class && objects != null) {
            restartMessageListener(objects);
        }
    }

    /**
     * Restart the Message Listener Thread if possible.  If restart is not possible due to either the
     * maximum crashes per minute being exceeded or an unhandled error, then the whol process will exit
     */
    private void restartMessageListener(final Object... objects) {
        try {
            messageListenerCrashes.update(1);
            int crashesInLastMinute = messageListenerCrashes.getSnapshot().getValues().length;
            LOG.info("{} crashes detected in last minute", crashesInLastMinute);

            if (crashLimit == null || crashesInLastMinute > crashLimit) {
                LOG.error("Too many crashes, terminating process");
                exitSystem();
            }

            int threadNumber = (Integer) (objects)[0];
            LOG.info("Restarting crashed Message Listener");
            MessageListener messageListener = applicationContext.getBean(MessageListener.class);
            messageListener.processTransactions(threadNumber);
        } catch (Exception e) {
            LOG.error("Unable to restart thread.  Terminating process.", e);
            exitSystem();
        }
    }
}
