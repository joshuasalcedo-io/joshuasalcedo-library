package io.joshuasalcedo.pretty.core.model.time;

import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.utils.DateAndTimeUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Factory class for creating different types of TimeStamps
 */
public class TimeStampFactory {
    
    /**
     * Creates a basic TimeStamp representing the current time
     * 
     * @return a new TimeStamp instance for the current time
     */
    public static TimeStamp createNow() {
        return new TimeStamp();
    }
    
    /**
     * Creates a ModernTimeStamp with 12-hour format including day name
     * 
     * @return a new ModernTimeStamp instance for the current time
     */
    public static ModernTimeStamp createModernTimeStamp() {
        return new ModernTimeStamp();
    }
    
    /**
     * Creates a ModernTimeStamp for a specific instant
     * 
     * @param instant the instant to use
     * @return a new ModernTimeStamp instance
     */
    public static ModernTimeStamp createModernTimeStamp(Instant instant) {
        return new ModernTimeStamp(instant);
    }
    
    /**
     * Creates a ModernTimeStamp with a specific timezone
     * 
     * @param zoneId the timezone to use
     * @return a new ModernTimeStamp instance
     */
    public static ModernTimeStamp createModernTimeStamp(ZoneId zoneId) {
        return new ModernTimeStamp(Instant.now(), zoneId);
    }
    
    /**
     * Creates a colored PrettyTimeStamp with default color (cyan)
     * 
     * @return a new PrettyTimeStamp instance
     */
    public static PrettyTimeStamp createPrettyTimeStamp() {
        return new PrettyTimeStamp(RGBColor.named("green"));
    }
    
    /**
     * Creates a colored PrettyTimeStamp with specified color
     * 
     * @param color the color to use for the timestamp
     * @return a new PrettyTimeStamp instance
     */
    public static PrettyTimeStamp createPrettyTimeStamp(RGBColor color) {
        return new PrettyTimeStamp(color);
    }
    
    /**
     * Creates a colored PrettyTimeStamp with specified color name
     * 
     * @param colorName the name of the color to use
     * @return a new PrettyTimeStamp instance
     */
    public static PrettyTimeStamp createPrettyTimeStamp(String colorName) {
        return new PrettyTimeStamp(RGBColor.named(colorName));
    }


    /**
     * Creates a PrettyTimeStamp for a specific point in time with specified color
     * 
     * @param instant the instant to use
     * @param color the color to use
     * @return a new PrettyTimeStamp instance
     */
    public static PrettyTimeStamp createPrettyTimeStamp(Instant instant, RGBColor color) {
        return new PrettyTimeStamp(instant, color);
    }
}

/**
 * ModernTimeStamp class that formats time in a 12-hour format with day name
 */
class ModernTimeStamp extends TimeStamp {
    private static final DateTimeFormatter MODERN_FORMATTER = 
        DateTimeFormatter.ofPattern("hh:mm:ss a");
    
    /**
     * Creates a ModernTimeStamp with current time
     */
    public ModernTimeStamp() {
        super();
    }
    
    /**
     * Creates a ModernTimeStamp with specified instant
     */
    public ModernTimeStamp(Instant instant) {
        super(instant);
    }
    
    /**
     * Creates a ModernTimeStamp with specified instant and zone
     */
    public ModernTimeStamp(Instant instant, ZoneId zoneId) {
        super(instant, zoneId);
    }
    
    /**
     * Formats the timestamp in modern format: (day name) hh:mm:ss am/pm TimeZone
     * @return formatted timestamp string
     */
    public String formatModern() {
        ZonedDateTime zdt = toZonedDateTime();
        String dayName = zdt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String timeStr = zdt.format(MODERN_FORMATTER);
        String zoneStr = zdt.getZone().getDisplayName(TextStyle.SHORT, Locale.getDefault());
        
        return String.format("%s %s %s", dayName, timeStr, zoneStr);
    }
    
    @Override
    public String toString() {
        return formatModern();
    }
}

