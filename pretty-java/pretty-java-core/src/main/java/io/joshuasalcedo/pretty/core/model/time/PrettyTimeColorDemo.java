package io.joshuasalcedo.pretty.core.model.time;

import io.joshuasalcedo.pretty.core.model.time.*;
import io.joshuasalcedo.pretty.core.properties.RGBColor;

import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Comprehensive demonstration of TimeStamp and RGBColor capabilities
 */
public class PrettyTimeColorDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== RGBCOLOR BASICS ===");
        demoRGBColorBasics();

        System.out.println("\n=== RGBCOLOR MANIPULATION ===");
        demoRGBColorManipulation();

        System.out.println("\n=== TIMESTAMP WITH COLORS ===");
        demoTimestampWithColors();

        System.out.println("\n=== COLOR GRADIENT TIMESTAMPS ===");
        demoColorGradientTimestamps();

        System.out.println("\n=== TIMEZONE WALL CLOCK ===");
        demoTimezoneWallClock();
        System.out.println("\n");

        System.out.println("DEFAULT timestamp");
        PrettyTimeStamp timestamp = TimeStampFactory.createPrettyTimeStamp();
        System.out.println(timestamp);
    }

    /**
     * Demonstrates basic RGBColor creation and usage
     */
    private static void demoRGBColorBasics() {
        // Creating colors in different ways
        RGBColor customRgb = RGBColor.of(128, 64, 200);
        RGBColor fromHex = RGBColor.fromHex("#FF5500");
        RGBColor namedColor = RGBColor.named("cyan");

        // Displaying color information
        System.out.println("Custom RGB: " + customRgb);
        System.out.println("From hex: " + fromHex);
        System.out.println("Named color: " + namedColor);

        // Applying colors to text
        System.out.println("Applied color: " + customRgb.apply("This text is colored with custom RGB"));
        System.out.println("Applied hex color: " + fromHex.apply("This text uses the hex color"));
        System.out.println("Applied named color: " + namedColor.apply("This text is cyan"));

        // Converting between formats
        System.out.println("Custom RGB as hex: " + customRgb.toHex());
        System.out.println("Hex color components: R=" + fromHex.getRed() +
                ", G=" + fromHex.getGreen() +
                ", B=" + fromHex.getBlue());
    }

    /**
     * Demonstrates color manipulation capabilities
     */
    private static void demoRGBColorManipulation() {
        RGBColor baseColor = RGBColor.named("blue");

        // Brightness adjustments
        RGBColor brighter = baseColor.brighter();
        RGBColor darker = baseColor.darker();
        RGBColor custom = baseColor.brightness(0.5); // 50% brightness

        System.out.println("Base color: " + baseColor.apply("Blue text"));
        System.out.println("Brighter: " + brighter.apply("Brighter blue"));
        System.out.println("Darker: " + darker.apply("Darker blue"));
        System.out.println("Custom brightness (50%): " + custom.apply("Half brightness"));

        // Color blending
        RGBColor red = RGBColor.named("red");
        RGBColor blended25 = red.blend(baseColor, 0.25); // 25% blue, 75% red
        RGBColor blended50 = red.blend(baseColor, 0.5);  // 50% each
        RGBColor blended75 = red.blend(baseColor, 0.75); // 75% blue, 25% red

        System.out.println("Red: " + red.apply("Red text"));
        System.out.println("25% blend: " + blended25.apply("25% blue, 75% red"));
        System.out.println("50% blend: " + blended50.apply("50% blue, 50% red"));
        System.out.println("75% blend: " + blended75.apply("75% blue, 25% red"));

        // Background colors
        RGBColor bgColor = red.asBackground();
        System.out.println(bgColor.apply("White text on red background"));

        // Combined foreground and background (needs a helper method)
        RGBColor fg = RGBColor.named("yellow");
        RGBColor bg = RGBColor.named("navy").asBackground();
        System.out.println(applyBoth(fg, bg, "Yellow text on navy background"));
    }

    /**
     * Helper method to apply both foreground and background colors
     */
    private static String applyBoth(RGBColor fg, RGBColor bg, String text) {
        return bg.apply(fg.apply(text));
    }

    /**
     * Demonstrates all timestamp variants with different colors
     */
    private static void demoTimestampWithColors() {
        // Basic timestamp
        TimeStamp basic = TimeStampFactory.createNow();
        System.out.println("Basic timestamp: " + basic);

        // Modern timestamp
        ModernTimeStamp modern = TimeStampFactory.createModernTimeStamp();
        System.out.println("Modern timestamp: " + modern);

        // Pretty timestamps with various named colors
        String[] colorNames = {"red", "green", "blue", "yellow", "cyan", "magenta",
                "orange", "purple", "pink"};

        for (String colorName : colorNames) {
            PrettyTimeStamp pts = TimeStampFactory.createPrettyTimeStamp(colorName);
            System.out.println(colorName + " timestamp: " + pts);
        }

        // Pretty timestamp with custom RGB color
        PrettyTimeStamp customColor = TimeStampFactory.createPrettyTimeStamp(
                RGBColor.of(100, 149, 237) // Cornflower blue
        );
        System.out.println("Custom color timestamp: " + customColor);

        // Pretty timestamp with hex color
        PrettyTimeStamp hexColor = TimeStampFactory.createPrettyTimeStamp(
                RGBColor.fromHex("#8A2BE2") // Violet
        );
        System.out.println("Hex color timestamp: " + hexColor);

        // Demonstrating the formatISOColored() method
        System.out.println("ISO formatted timestamp: " + hexColor.formatISOColored());
    }

    /**
     * Demonstrates creating a sequence of timestamps with a color gradient
     */
    private static void demoColorGradientTimestamps() {
        // Create start and end colors for the gradient
        RGBColor startColor = RGBColor.named("blue");
        RGBColor endColor = RGBColor.named("red");

        // Number of steps in the gradient
        int steps = 10;

        System.out.println("Color gradient from blue to red:");

        // Create a timestamp for each step with a color along the gradient
        for (int i = 0; i < steps; i++) {
            double ratio = (double) i / (steps - 1);
            RGBColor gradientColor = startColor.blend(endColor, ratio);

            // Create a timestamp with the gradient color
            Instant timePoint = Instant.now().plusMillis(i * 100);
            PrettyTimeStamp gradientTimestamp =
                    TimeStampFactory.createPrettyTimeStamp(timePoint, gradientColor);

            System.out.println("Step " + i + ": " + gradientTimestamp);
        }
    }

    /**
     * Demonstrates a wall clock showing time in different timezones with appropriate colors
     */
    private static void demoTimezoneWallClock() throws InterruptedException {
        // Define some timezones with corresponding colors
        Object[][] zoneInfo = {
                {"UTC", "white"},
                {"America/New_York", "blue"},
                {"Europe/London", "red"},
                {"Asia/Tokyo", "green"},
                {"Australia/Sydney", "magenta"},
                {"Pacific/Auckland", "cyan"}
        };

        System.out.println("Timezone wall clock (updating every second for 5 seconds):");

        // Run the clock for 5 seconds
        for (int tick = 0; tick < 5; tick++) {
            // Clear previous output for cleaner display
            if (tick > 0) {
                System.out.println("\n--- Clock update ---");
            }

            // Show the time in each timezone
            for (Object[] info : zoneInfo) {
                String zoneId = (String) info[0];
                String colorName = (String) info[1];

                // Create a timestamp for this timezone with the specified color
                ZoneId zone = ZoneId.of(zoneId);
                Instant now = Instant.now();
                PrettyTimeStamp zonedTime = new PrettyTimeStamp(
                        now, zone, RGBColor.named(colorName)
                );

                // Display the time
                System.out.println(zoneId + ": " + zonedTime);
            }

            // Wait for a second before the next update
            TimeUnit.SECONDS.sleep(1);
        }

        System.out.println("Clock simulation complete!");
    }

    /**
     * Creates a JSON representation of timeline events with timestamps
     */
    private static void demoTimelineJSON() {
        System.out.println("Timeline events in JSON format:");

        // Create events at different times with different colors
        Instant now = Instant.now();
        String[] events = {"System start", "Connection established", "Data processing",
                "Warning detected", "Process completed"};
        String[] colors = {"green", "blue", "cyan", "yellow", "magenta"};
        long[] offsets = {-3600, -1800, -600, -300, 0}; // seconds from now

        StringBuilder jsonBuilder = new StringBuilder("[\n");

        for (int i = 0; i < events.length; i++) {
            Instant eventTime = now.plusSeconds(offsets[i]);
            PrettyTimeStamp timestamp = TimeStampFactory.createPrettyTimeStamp(
                    eventTime, RGBColor.named(colors[i])
            );

            // Add the event to the JSON array
            if (i > 0) {
                jsonBuilder.append(",\n");
            }

            jsonBuilder.append("  {\n")
                    .append("    \"event\": \"").append(events[i]).append("\",\n")
                    .append("    \"timestamp\": ").append(timestamp.toJSON()).append("\n")
                    .append("  }");
        }

        jsonBuilder.append("\n]");
        System.out.println(jsonBuilder.toString());
    }
}