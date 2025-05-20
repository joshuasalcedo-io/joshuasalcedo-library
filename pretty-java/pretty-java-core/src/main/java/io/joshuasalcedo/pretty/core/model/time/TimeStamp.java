package io.joshuasalcedo.pretty.core.model.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TimeStamp implements Comparable<TimeStamp> {

    // Enum definitions
    public enum ClockSystem {
        TwelveHour("hh:mm:ss a"),
        TwentyFourHour("HH:mm:ss"),
        Military("HHmm"),
        Custom(null),
        SystemDefault(null);

        private final String pattern;

        ClockSystem(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }

    public enum TimeFormat {
        Simple("HH:mm:ss"),
        ISO(DateTimeFormatter.ISO_TIME.toString()),
        HumanReadable("h:mm:ss a"),
        Custom(null);

        private final String pattern;

        TimeFormat(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }

    public enum TimeZone {
        SystemDefault(ZoneId.systemDefault()),
        UTC(ZoneId.of("UTC")),
        Custom(null);

        private final ZoneId zoneId;

        TimeZone(ZoneId zoneId) {
            this.zoneId = zoneId;
        }

        public ZoneId getZoneId() {
            return zoneId;
        }
    }

    public enum DateFormat {
        Simple("yyyy-MM-dd"),
        ISO(DateTimeFormatter.ISO_DATE.toString()),
        Custom(null);

        private final String pattern;

        DateFormat(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }

    // Core properties
    private final Instant instant;
    private ZoneId zoneId;
    private ClockSystem clockSystem;
    private TimeFormat timeFormat;
    private DateFormat dateFormat;

    // Custom format patterns storage
    private final Map<String, String> customPatterns = new HashMap<>();

    // Cached formatters
    private final Map<String, DateTimeFormatter> formatterCache = new HashMap<>();

    /**
     * Creates a timestamp with current time and default settings
     */
    public TimeStamp() {
        this.instant = Instant.now();
        this.zoneId = TimeZone.SystemDefault.getZoneId();
        this.clockSystem = ClockSystem.SystemDefault;
        this.timeFormat = TimeFormat.Simple;
        this.dateFormat = DateFormat.Simple;
    }

    /**
     * Creates a timestamp with specified instant and default settings
     * @param instant the instant to use
     */
    public TimeStamp(Instant instant) {
        this.instant = instant;
        this.zoneId = TimeZone.SystemDefault.getZoneId();
        this.clockSystem = ClockSystem.SystemDefault;
        this.timeFormat = TimeFormat.Simple;
        this.dateFormat = DateFormat.Simple;
    }

    /**
     * Creates a timestamp with specified instant and zone
     * @param instant the instant to use
     * @param zoneId the timezone
     */
    public TimeStamp(Instant instant, ZoneId zoneId) {
        this.instant = instant;
        this.zoneId = zoneId;
        this.clockSystem = ClockSystem.SystemDefault;
        this.timeFormat = TimeFormat.Simple;
        this.dateFormat = DateFormat.Simple;
    }

    /**
     * Creates a fully configured timestamp
     * @param instant the instant
     * @param timeZone the timezone enum
     * @param clockSystem the clock system enum
     * @param timeFormat the time format enum
     * @param dateFormat the date format enum
     */
    public TimeStamp(Instant instant, TimeZone timeZone, ClockSystem clockSystem,
                     TimeFormat timeFormat, DateFormat dateFormat) {
        this.instant = instant;
        this.zoneId = timeZone == TimeZone.Custom ? ZoneId.systemDefault() : timeZone.getZoneId();
        this.clockSystem = clockSystem;
        this.timeFormat = timeFormat;
        this.dateFormat = dateFormat;
    }

    /**
     * Creates a timestamp from epoch milliseconds
     * @param epochMillis milliseconds since epoch
     * @return a new TimeStamp
     */
    public static TimeStamp fromEpochMillis(long epochMillis) {
        return new TimeStamp(Instant.ofEpochMilli(epochMillis));
    }

    /**
     * Creates a timestamp from a LocalDateTime
     * @param localDateTime the LocalDateTime
     * @return a new TimeStamp
     */
    public static TimeStamp fromLocalDateTime(LocalDateTime localDateTime) {
        return new TimeStamp(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Creates a timestamp from a string using a specified pattern
     * @param timestampStr the timestamp string
     * @param pattern the format pattern
     * @param zoneId the timezone
     * @return a new TimeStamp
     */
    public static TimeStamp parse(String timestampStr, String pattern, ZoneId zoneId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(zoneId);
        Instant instant = ZonedDateTime.parse(timestampStr, formatter).toInstant();
        return new TimeStamp(instant, zoneId);
    }

    /**
     * Sets a custom pattern for a custom enum value
     * @param enumName the name of the enum (e.g., "ClockSystem", "TimeFormat")
     * @param pattern the pattern to use
     */
    public void setCustomPattern(String enumName, String pattern) {
        customPatterns.put(enumName, pattern);
        // Clear formatter cache when patterns change
        formatterCache.clear();
    }

    /**
     * Gets a timestamp representing the current moment
     * @return a new TimeStamp for now
     */
    public static TimeStamp now() {
        return new TimeStamp();
    }

    // Getter methods
    public Instant getInstant() {
        return instant;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public ClockSystem getClockSystem() {
        return clockSystem;
    }

    public TimeFormat getTimeFormat() {
        return timeFormat;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    // Builder-style setters that return new instances
    public TimeStamp withZoneId(ZoneId zoneId) {
        TimeStamp result = new TimeStamp(this.instant, zoneId);
        result.clockSystem = this.clockSystem;
        result.timeFormat = this.timeFormat;
        result.dateFormat = this.dateFormat;
        result.customPatterns.putAll(this.customPatterns);
        return result;
    }

    public TimeStamp withTimeZone(TimeZone timeZone) {
        if (timeZone == TimeZone.Custom) {
            throw new IllegalArgumentException("Cannot use TimeZone.Custom without specifying a custom ZoneId");
        }
        return withZoneId(timeZone.getZoneId());
    }

    public TimeStamp withCustomTimeZone(String zoneIdStr) {
        return withZoneId(ZoneId.of(zoneIdStr));
    }

    public TimeStamp withClockSystem(ClockSystem clockSystem) {
        TimeStamp result = new TimeStamp(this.instant, this.zoneId);
        result.clockSystem = clockSystem;
        result.timeFormat = this.timeFormat;
        result.dateFormat = this.dateFormat;
        result.customPatterns.putAll(this.customPatterns);
        return result;
    }

    public TimeStamp withTimeFormat(TimeFormat timeFormat) {
        TimeStamp result = new TimeStamp(this.instant, this.zoneId);
        result.clockSystem = this.clockSystem;
        result.timeFormat = timeFormat;
        result.dateFormat = this.dateFormat;
        result.customPatterns.putAll(this.customPatterns);
        return result;
    }

    public TimeStamp withDateFormat(DateFormat dateFormat) {
        TimeStamp result = new TimeStamp(this.instant, this.zoneId);
        result.clockSystem = this.clockSystem;
        result.timeFormat = this.timeFormat;
        result.dateFormat = dateFormat;
        result.customPatterns.putAll(this.customPatterns);
        return result;
    }

    // Conversion methods
    public long toEpochMillis() {
        return instant.toEpochMilli();
    }

    public long toEpochSeconds() {
        return instant.getEpochSecond();
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.ofInstant(instant, zoneId);
    }

    public ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.ofInstant(instant, zoneId);
    }

    // Formatting methods
    private DateTimeFormatter getFormatter(String key, String defaultPattern) {
        if (formatterCache.containsKey(key)) {
            return formatterCache.get(key);
        }

        String pattern;
        if (customPatterns.containsKey(key)) {
            pattern = customPatterns.get(key);
        } else {
            pattern = defaultPattern;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(zoneId);
        formatterCache.put(key, formatter);
        return formatter;
    }

    /**
     * Gets the effective time pattern based on current settings
     * @return the time pattern to use
     */
    private String getEffectiveTimePattern() {
        if (timeFormat == TimeFormat.Custom && customPatterns.containsKey("TimeFormat")) {
            return customPatterns.get("TimeFormat");
        }

        if (clockSystem == ClockSystem.Custom && customPatterns.containsKey("ClockSystem")) {
            return customPatterns.get("ClockSystem");
        }

        if (clockSystem != ClockSystem.SystemDefault && clockSystem.getPattern() != null) {
            return clockSystem.getPattern();
        }

        return timeFormat.getPattern();
    }

    /**
     * Gets the effective date pattern based on current settings
     * @return the date pattern to use
     */
    private String getEffectiveDatePattern() {
        if (dateFormat == DateFormat.Custom && customPatterns.containsKey("DateFormat")) {
            return customPatterns.get("DateFormat");
        }

        return dateFormat.getPattern();
    }

    /**
     * Formats just the time component
     * @return formatted time string
     */
    public String formatTime() {
        String pattern = getEffectiveTimePattern();
        return getFormatter("time", pattern).format(instant);
    }

    /**
     * Formats just the date component
     * @return formatted date string
     */
    public String formatDate() {
        String pattern = getEffectiveDatePattern();
        return getFormatter("date", pattern).format(instant);
    }

    /**
     * Formats both date and time together
     * @return formatted date and time string
     */
    public String formatDateTime() {
        String datePattern = getEffectiveDatePattern();
        String timePattern = getEffectiveTimePattern();
        String fullPattern = datePattern + " " + timePattern;
        return getFormatter("datetime", fullPattern).format(instant);
    }

    /**
     * Formats the timestamp with the specified pattern
     * @param pattern the pattern to use
     * @return formatted string
     */
    public String format(String pattern) {
        return DateTimeFormatter.ofPattern(pattern).withZone(zoneId).format(instant);
    }

    // Manipulation methods
    public TimeStamp plus(long amount, ChronoUnit unit) {
        TimeStamp result = new TimeStamp(instant.plus(amount, unit), zoneId);
        result.clockSystem = this.clockSystem;
        result.timeFormat = this.timeFormat;
        result.dateFormat = this.dateFormat;
        result.customPatterns.putAll(this.customPatterns);
        return result;
    }

    public TimeStamp minus(long amount, ChronoUnit unit) {
        TimeStamp result = new TimeStamp(instant.minus(amount, unit), zoneId);
        result.clockSystem = this.clockSystem;
        result.timeFormat = this.timeFormat;
        result.dateFormat = this.dateFormat;
        result.customPatterns.putAll(this.customPatterns);
        return result;
    }

    // Comparison methods
    public long difference(TimeStamp other, ChronoUnit unit) {
        return unit.between(other.instant, this.instant);
    }

    public boolean isBefore(TimeStamp other) {
        return instant.isBefore(other.instant);
    }

    public boolean isAfter(TimeStamp other) {
        return instant.isAfter(other.instant);
    }

    // Object methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeStamp timeStamp = (TimeStamp) o;
        return Objects.equals(instant, timeStamp.instant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instant);
    }

    @Override
    public String toString() {
        return formatDateTime();
    }

    @Override
    public int compareTo(TimeStamp other) {
        return instant.compareTo(other.instant);
    }
}