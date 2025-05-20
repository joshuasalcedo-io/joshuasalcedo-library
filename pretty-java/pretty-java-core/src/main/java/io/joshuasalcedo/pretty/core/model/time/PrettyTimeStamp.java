package io.joshuasalcedo.pretty.core.model.time;

import io.joshuasalcedo.pretty.core.properties.RGBColor;
import io.joshuasalcedo.pretty.core.utils.DateAndTimeUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter; /**
 * PrettyTimeStamp class that extends TimeStamp with color formatting
 */
public class PrettyTimeStamp extends ModernTimeStamp {
    private final RGBColor color;
    
    /**
     * Creates a PrettyTimeStamp with current time and specified color
     */
    public PrettyTimeStamp(RGBColor color) {
        super();
        this.color = color != null ? color : RGBColor.named("cyan");
    }

    public PrettyTimeStamp() {
        super();
        this.color = RGBColor.named("green");
    }
    
    /**
     * Creates a PrettyTimeStamp with specified instant and color
     */
    public PrettyTimeStamp(Instant instant, RGBColor color) {
        super(instant);
        this.color = color != null ? color : RGBColor.named("cyan");
    }
    
    /**
     * Creates a PrettyTimeStamp with specified instant, zone and color
     */
    public PrettyTimeStamp(Instant instant, ZoneId zoneId, RGBColor color) {
        super(instant, zoneId);
        this.color = color != null ? color : RGBColor.named("cyan");
    }
    
    /**
     * Gets the color used for this timestamp
     */
    public RGBColor getColor() {
        return color;
    }
    
    /**
     * Creates a new PrettyTimeStamp with a different color
     */
    public PrettyTimeStamp withColor(RGBColor newColor) {
        return new PrettyTimeStamp(getInstant(), getZoneId(), newColor);
    }
    
    /**
     * Creates a new PrettyTimeStamp with a different color by name
     */
    public PrettyTimeStamp withColor(String colorName) {
        return withColor(RGBColor.named(colorName));
    }
    
    /**
     * Formats the timestamp with color
     */
    public String formatColored() {
        return color.apply(formatModern());
    }
    
    /**
     * Formats the timestamp using DateAndTimeUtils with color
     */
    public String formatWithUtils() {
        String timestamp = DateAndTimeUtils.getFormattedDateAndTime(toLocalDateTime());
        return color.apply(timestamp);
    }

    /**
     * Formats the timestamp in ISO format
     * @return formatted timestamp string in ISO format
     */
    private String formatISO() {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_DATE_TIME;
        return isoFormatter.format(toZonedDateTime());
    }
    
    /**
     * Formats the timestamp with ISO format and color
     */
    public String formatISOColored() {
        return color.apply(formatISO());
    }
    
    @Override
    public String toString() {
        return formatColored();
    }
    
    /**
     * Creates a JSON representation of the timestamp
     */
    public String toJSON() {
        return String.format(
            "{\"timestamp\":\"%s\",\"color\":\"%s\",\"timezone\":\"%s\"}",
            toEpochMillis(),
            color.toHex(),
            getZoneId().getId()
        );
    }
}
