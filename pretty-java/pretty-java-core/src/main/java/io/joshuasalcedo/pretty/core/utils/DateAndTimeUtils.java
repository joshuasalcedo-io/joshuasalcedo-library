package io.joshuasalcedo.pretty.core.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateAndTimeUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_ZONE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
    private static final DateTimeFormatter DATE_TIME_ZONE_OFFSET_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss VV");

    /**
     * Returns the current date and time formatted as "yyyy-MM-dd HH:mm:ss"
     * @return formatted current date and time
     */
    public static String getCurrentFormattedDateTime() {
        return getFormattedDateAndTime(LocalDateTime.now());
    }

    /**
     * Formats a LocalDateTime object as "yyyy-MM-dd HH:mm:ss"
     * @param dateTime the LocalDateTime to format
     * @return formatted date and time string
     */
    public static String getFormattedDateAndTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Formats a LocalDate object as "yyyy-MM-dd"
     * @param date the LocalDate to format
     * @return formatted date string
     */
    public static String getFormattedDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * Returns the current time formatted as "HH:mm:ss"
     * @return formatted current time
     */
    public String getFormattedTime() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    /**
     * Returns the current date and time formatted as "yyyy-MM-dd HH:mm:ss"
     * @return formatted current date and time
     */
    public String getFormattedDateTime() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    /**
     * Returns the current date and time with timezone formatted as "yyyy-MM-dd HH:mm:ss z"
     * @return formatted current date and time with timezone
     */
    public String getFormattedDateTimeWithTimeZone() {
        return ZonedDateTime.now().format(DATE_TIME_ZONE_FORMATTER);
    }

    /**
     * Returns the current date and time with timezone and offset formatted as "yyyy-MM-dd HH:mm:ss VV"
     * @return formatted current date and time with timezone and offset
     */
    public String getFormattedDateTimeWithTimeZoneAndOffset() {
        return ZonedDateTime.now().format(DATE_TIME_ZONE_OFFSET_FORMATTER);
    }

    /**
     * Returns the current timestamp in the format "yyyy-MM-dd HH:mm:ss VV"
     * @return formatted timestamp
     */
    public String getFormattedTimestamp() {
        return getFormattedDateTimeWithTimeZoneAndOffset();
    }

    /**
     * Converts milliseconds to a LocalDateTime object
     * @param milliseconds the milliseconds since epoch
     * @return the corresponding LocalDateTime
     */
    public static LocalDateTime millisecondsToDateTime(long milliseconds) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneId.systemDefault());
    }

    /**
     * Converts a LocalDateTime to milliseconds since epoch
     * @param dateTime the LocalDateTime to convert
     * @return milliseconds since epoch
     */
    public static long dateTimeToMilliseconds(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Returns the current month as an integer (1-12)
     * @return current month number
     */
    public static int getCurrentMonth() {
        return LocalDate.now().getMonthValue();
    }

    /**
     * Returns the current month name
     * @return current month name
     */
    public static String getCurrentMonthName() {
        return LocalDate.now().getMonth().toString();
    }

    /**
     * Returns the current year
     * @return current year
     */
    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    /**
     * Returns the current day of month
     * @return current day of month
     */
    public static int getCurrentDayOfMonth() {
        return LocalDate.now().getDayOfMonth();
    }

    /**
     * Calculates the difference in days between two LocalDate objects
     * @param date1 the first date
     * @param date2 the second date
     * @return the difference in days
     */
    public static long getDaysBetween(LocalDate date1, LocalDate date2) {
        return ChronoUnit.DAYS.between(date1, date2);
    }

    /**
     * Checks if a given year is a leap year
     * @param year the year to check
     * @return true if it's a leap year, false otherwise
     */
    public static boolean isLeapYear(int year) {
        return Year.isLeap(year);
    }

    /**
     * Adds a specified number of days to a date
     * @param date the original date
     * @param days the number of days to add (can be negative)
     * @return the resulting date
     */
    public static LocalDate addDays(LocalDate date, long days) {
        return date.plusDays(days);
    }

    /**
     * Gets a LocalDateTime with a specific timezone
     * @param zoneId the timezone ID (e.g., "America/New_York")
     * @return the current LocalDateTime in the specified timezone
     */
    public static ZonedDateTime getCurrentDateTimeInZone(String zoneId) {
        return ZonedDateTime.now(ZoneId.of(zoneId));
    }

    /**
     * Formats a date with a custom pattern
     * @param dateTime the date time to format
     * @param pattern the pattern to use
     * @return formatted date string
     */
    public static String formatWithPattern(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Parses a date string into a LocalDate
     * @param dateString the date string to parse
     * @param pattern the pattern of the date string
     * @return the parsed LocalDate
     */
    public static LocalDate parseDate(String dateString, String pattern) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Parses a date time string into a LocalDateTime
     * @param dateTimeString the date time string to parse
     * @param pattern the pattern of the date time string
     * @return the parsed LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeString, String pattern) {
        return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Returns the start of the day (00:00:00) for the given date
     * @param date the date to get the start of day for
     * @return LocalDateTime representing the start of the day
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    /**
     * Returns the end of the day (23:59:59.999999999) for the given date
     * @param date the date to get the end of day for
     * @return LocalDateTime representing the end of the day
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        return date.atTime(LocalTime.MAX);
    }

    /**
     * Returns the start of the month (first day, 00:00:00) for the given date
     * @param yearMonth the year and month
     * @return LocalDateTime representing the start of the month
     */
    public static LocalDateTime startOfMonth(YearMonth yearMonth) {
        return yearMonth.atDay(1).atStartOfDay();
    }

    /**
     * Returns the end of the month (last day, 23:59:59.999999999) for the given date
     * @param yearMonth the year and month
     * @return LocalDateTime representing the end of the month
     */
    public static LocalDateTime endOfMonth(YearMonth yearMonth) {
        return yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
    }

    /**
     * Checks if a date is between two other dates
     * @param date the date to check
     * @param startDate the start date
     * @param endDate the end date
     * @return true if the date is between startDate and endDate (inclusive)
     */
    public static boolean isBetween(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}