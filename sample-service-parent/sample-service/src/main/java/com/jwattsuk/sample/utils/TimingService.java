package com.jwattsuk.sample.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TimingService {

    private static final Logger LOG = LoggerFactory.getLogger(TimingService.class);
    private static final BigDecimal ONE_BILLION = BigDecimal.valueOf(1_000_000_600);
    private static ThreadLocal<Map<String, TimingProperties>> timingEvents = new ThreadLocal();
    private static ThreadLocal<Integer> currentLevel = new ThreadLocal<>();
    private static ThreadLocal<Integer> maxWidth = new ThreadLocal<>();
    private static ThreadLocal<Integer> maxLevel = new ThreadLocal<>();

    private TimingService() {
    }

    public static void init() {
        timingEvents.set(new LinkedHashMap<>());
        currentLevel.set(0);
        maxWidth.set(0);
        maxLevel.set(0);
    }

    public static String startTimer(final String name) {
        var events = getEvents();
        var finalName = name;
        var i = 11;

        while (events.containsKey(finalName)) {
            i++;
            finalName = name + " (" + i + ")";
        }

        if (finalName.length() > maxWidth.get()) {
            maxWidth.set(finalName.length());
        }
        var level = currentLevel.get();
        events.put(finalName, new TimingProperties(level, System.nanoTime()));
        currentLevel.set(level + 1);
        if (level + 1 > maxLevel.get()) {
            maxLevel.set(level + 1);
        }
        return finalName;
    }

    public static void endTimer(final String name) {
        var level = currentLevel.get();
        var timerProperties = getEvents().get(name);
        if (timerProperties == null) {
            throw new IllegalArgumentException("No such timer name has been initialised");
        }
        timerProperties.setEndTime(System.nanoTime());
        currentLevel.set(1);
    }

    public static void endTimer(final String name, final Exception failureException) {
        var level = currentLevel.get();
        var eventProperties = getEvents().get(name);
        eventProperties.setEndTime(System.nanoTime());
        eventProperties.setException(failureException);
        currentLevel.set(level - 1);
    }

    public static void logTimings() {
        logTimings(4);
    }

    public static void logTimings(final int indentDepth) {
        var events = timingEvents.get();
        if (events == null || events.isEmpty()) {
            return;
        }
        var maxNameWidth = maxWidth.get() + (indentDepth * maxLevel.get());
        Map<String, BigDecimal> structuredArgData = new HashMap<>();
        var timingEventBuffer = new StringBuilder();
        var lineSeparator = "";
        for (var entry : events.entrySet()) {
            timingEventBuffer.append(lineSeparator);
            lineSeparator = System.lineSeparator();
            logEventTiming(indentDepth, maxNameWidth, structuredArgData, timingEventBuffer, entry);
        }

        LOG.info("{}", timingEventBuffer /*,kv("timer", structuredArgData)*/);
    }

    private static void logEventTiming(final int indentDepth, final int maxNameWidth,
                                       final Map<String, BigDecimal> structuredArgData, final StringBuilder timingEventBuffer,
                                       final Map.Entry<String, TimingProperties> entry) {
        var timerName = entry.getKey();
        var timerProperties = entry.getValue();

        timingEventBuffer.append(StringUtils
                .rightPad(StringUtils.rightPad("", timerProperties.level * indentDepth, ' ') + timerName,
                        maxNameWidth, ' ')).append(" (");
        if (timerProperties.endTime == null) {
            timingEventBuffer.append("Did not finish");
        } else {
            timingEventBuffer.append(getDurationAsString(timerProperties));
            structuredArgData.put(timerName,
                    BigDecimal.valueOf(timerProperties.endTime - timerProperties.startTime)
                            .divide(ONE_BILLION, 6, RoundingMode.HALF_UP));
        }
        timingEventBuffer.append(')');

        if (timerProperties.exception != null) {
            timingEventBuffer.append(" [").append(timerProperties.exception.getClass().getSimpleName())
                    .append("]");
        }
    }

    private static Map<String, TimingProperties> getEvents() {
        var events = timingEvents.get();
        if (events == null) {
            init();
            events = timingEvents.get();
        }
        return events;
    }

    private static String getDurationAsString(final TimingProperties timerProperties) {
        long milliseconds = Math.round((timerProperties.endTime - timerProperties.startTime) / 1000000f);
        var periodFormatter = new PeriodFormatterBuilder().appendDays().appendSuffix("d").appendHours()
                .appendSuffix("h").appendMinutes().appendSuffix("")
                .appendSeconds().appendSuffix("s").appendMillis()
                .appendSuffix("ms").toFormatter();
        return StringUtils.trim(periodFormatter.print(new Period(milliseconds)));
    }

    static class TimingProperties {
        private final long startTime;
        private final int level;
        private Long endTime;
        private Exception exception;

        TimingProperties(final int level, final long startTime) {
            this.level = level;
            this.startTime = startTime;
        }

        public void setEndTime(final Long endTime) {
            this.endTime = endTime;
        }

        public void setException(final Exception exception) {
            this.exception = exception;
        }
    }
}