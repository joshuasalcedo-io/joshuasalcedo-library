package io.joshuasalcedo.pretty.core.model.progress;

import io.joshuasalcedo.pretty.core.properties.RGBColor;

/**
 * A progress runner that displays text with animated effects.
 * <p>
 * This class displays a message with various text animation effects,
 * such as fading, blinking, typing, and more.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * // Create a text animation with typing effect
 * TextAnimationRunner textAnim = new TextAnimationRunner("Processing your request...")
 *         .setAnimationType(TextAnimationRunner.AnimationType.TYPING);
 * 
 * // Start the animation
 * textAnim.start();
 * 
 * // Perform the operation
 * // ...
 * 
 * // Stop the animation
 * textAnim.stop();
 * </pre>
 */
public class TextAnimationRunner extends AbstractProgressRunner {
    
    /**
     * Enum defining different text animation types.
     */
    public enum AnimationType {
        /**
         * Typing animation (each character appears one by one)
         */
        TYPING,
        
        /**
         * Wave animation (text moves in a sine wave pattern)
         */
        WAVE,
        
        /**
         * Rainbow animation (text cycles through colors)
         */
        RAINBOW,
        
        /**
         * Fade animation (text fades in and out)
         */
        FADE,
        
        /**
         * Blink animation (text blinks)
         */
        BLINK,
        
        /**
         * Slide animation (text slides in from left)
         */
        SLIDE,
        
        /**
         * Bounce animation (text bounces up and down)
         */
        BOUNCE,
        
        /**
         * Glitch animation (text appears to glitch)
         */
        GLITCH
    }
    
    private AnimationType animationType = AnimationType.TYPING;
    private String baseMessage = "";
    private int maxLength = 80; // Maximum text length for animations
    
    // Rainbow animation colors
    private static final RGBColor[] RAINBOW_COLORS = {
        RGBColor.of(255, 0, 0),     // Red
        RGBColor.of(255, 127, 0),   // Orange
        RGBColor.of(255, 255, 0),   // Yellow
        RGBColor.of(0, 255, 0),     // Green
        RGBColor.of(0, 0, 255),     // Blue
        RGBColor.of(75, 0, 130),    // Indigo
        RGBColor.of(148, 0, 211)    // Violet
    };
    
    /**
     * Creates a text animation progress runner with the given message.
     * 
     * @param message The base message to display
     */
    public TextAnimationRunner(String message) {
        super(message, 50); // Fast updates for smooth animation
        this.baseMessage = message != null ? message : "";
        withIndeterminate(true);
    }
    
    /**
     * Creates a text animation progress runner with the given message and update interval.
     * 
     * @param message The base message to display
     * @param updateIntervalMs The update interval in milliseconds
     */
    public TextAnimationRunner(String message, int updateIntervalMs) {
        super(message, updateIntervalMs);
        this.baseMessage = message != null ? message : "";
       withIndeterminate(true);
    }
    
    /**
     * Sets the animation type.
     * 
     * @param animationType The animation type to use
     * @return This instance for method chaining
     */
    public TextAnimationRunner setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
        return this;
    }
    
    /**
     * Sets the maximum text length for animations.
     * 
     * @param maxLength The maximum text length
     * @return This instance for method chaining
     */
    public TextAnimationRunner setMaxLength(int maxLength) {
        this.maxLength = Math.max(10, maxLength);
        return this;
    }
    
    @Override
    protected void render(StringBuilder display, String message, double progress, int animationStep) {
        // Use the current message if available, otherwise use base message
        String textToAnimate = message != null && !message.isEmpty() ? message : baseMessage;
        
        switch (animationType) {
            case TYPING:
                renderTypingAnimation(display, textToAnimate, animationStep);
                break;
            case WAVE:
                renderWaveAnimation(display, textToAnimate, animationStep);
                break;
            case RAINBOW:
                renderRainbowAnimation(display, textToAnimate, animationStep);
                break;
            case FADE:
                renderFadeAnimation(display, textToAnimate, animationStep);
                break;
            case BLINK:
                renderBlinkAnimation(display, textToAnimate, animationStep);
                break;
            case SLIDE:
                renderSlideAnimation(display, textToAnimate, animationStep);
                break;
            case BOUNCE:
                renderBounceAnimation(display, textToAnimate, animationStep);
                break;
            case GLITCH:
                renderGlitchAnimation(display, textToAnimate, animationStep);
                break;
            default:
                display.append(applyColor(textToAnimate, getMessageColor()));
                break;
        }
    }
    
    /**
     * Renders a typing animation where characters appear one by one.
     */
    private void renderTypingAnimation(StringBuilder display, String text, int animationStep) {
        int length = Math.min(text.length(), maxLength);
        int visibleChars = animationStep % (length + 15);
        
        if (visibleChars > length) {
            // Display full text with a blinking cursor for a while
            display.append(applyColor(text, getMessageColor()));
            
            // Blink the cursor
            if ((animationStep / 5) % 2 == 0) {
                display.append(applyColor("_", getProgressColor()));
            }
        } else {
            // Display partial text with a cursor
            String visibleText = text.substring(0, visibleChars);
            display.append(applyColor(visibleText, getMessageColor()));
            
            // Always show cursor during typing
            display.append(applyColor("_", getProgressColor()));
        }
    }
    
    /**
     * Renders text with a wave animation.
     */
    private void renderWaveAnimation(StringBuilder display, String text, int animationStep) {
        int length = Math.min(text.length(), maxLength);
        
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            
            // Calculate vertical position in wave
            double phase = (i * 0.5) + (animationStep * 0.2);
            int offset = (int) (Math.sin(phase) * 1.5);
            
            // Create vertical offset using spaces
            String padding = "";
            if (offset > 0) {
                padding = " ".repeat(offset);
                display.append(padding);
            }
            
            // Apply color based on position in wave
            RGBColor color = getMessageColor();
            if (offset > 0) {
                // Lighter color for characters at the top of the wave
                color = getProgressColor();
            } else if (offset < 0) {
                // Darker color for characters at the bottom of the wave
                color = getRemainingColor();
            }
            
            display.append(applyColor(String.valueOf(c), color));
            
            // Restore position for next character
            if (offset > 0) {
                display.append("\b".repeat(padding.length() + 1));
            }
        }
    }
    
    /**
     * Renders text with a rainbow color animation.
     */
    private void renderRainbowAnimation(StringBuilder display, String text, int animationStep) {
        int length = Math.min(text.length(), maxLength);
        int colorCount = RAINBOW_COLORS.length;
        
        for (int i = 0; i < length; i++) {
            // Calculate color index with animation shift
            int colorIndex = (i + animationStep) % colorCount;
            RGBColor color = RAINBOW_COLORS[colorIndex];
            
            display.append(color.apply(String.valueOf(text.charAt(i))));
        }
    }
    
    /**
     * Renders text that fades in and out.
     */
    private void renderFadeAnimation(StringBuilder display, String text, int animationStep) {
        int length = Math.min(text.length(), maxLength);
        
        // Calculate fade level (0-1)
        double fadeLevel = Math.abs(Math.sin(animationStep * 0.1));
        
        // Calculate color based on fade level
        int colorValue = (int) (fadeLevel * 255);
        RGBColor fadeColor = RGBColor.of(colorValue, colorValue, colorValue);
        
        display.append(fadeColor.apply(text.substring(0, length)));
    }
    
    /**
     * Renders text with a blinking animation.
     */
    private void renderBlinkAnimation(StringBuilder display, String text, int animationStep) {
        int length = Math.min(text.length(), maxLength);
        
        // Blink every 10 steps
        if ((animationStep / 10) % 2 == 0) {
            display.append(applyColor(text.substring(0, length), getMessageColor()));
        } else {
            // Don't display text during off phase
            display.append(" ".repeat(length));
        }
    }
    
    /**
     * Renders text that slides in from the left.
     */
    private void renderSlideAnimation(StringBuilder display, String text, int animationStep) {
        int length = Math.min(text.length(), maxLength);
        int totalWidth = length + 10; // Add padding for sliding
        
        // Calculate slide position
        int slidePos = animationStep % (totalWidth * 2);
        if (slidePos >= totalWidth) {
            slidePos = totalWidth * 2 - slidePos; // Reverse direction
        }
        
        // Determine visible portion of text
        int start = Math.max(0, length - slidePos);
        int visibleLength = Math.min(length, slidePos);
        
        if (start < length && visibleLength > 0) {
            display.append(applyColor(text.substring(start, start + visibleLength), getMessageColor()));
        }
    }
    
    /**
     * Renders text that bounces up and down.
     */
    private void renderBounceAnimation(StringBuilder display, String text, int animationStep) {
        int length = Math.min(text.length(), maxLength);
        
        // Calculate bounce offset (0-3)
        int bounceOffset = (int) (Math.abs(Math.sin(animationStep * 0.2) * 3));
        String padding = " ".repeat(bounceOffset);
        
        // Add vertical padding
        display.append(padding);
        
        // Display the text
        display.append(applyColor(text.substring(0, length), getMessageColor()));
    }
    
    /**
     * Renders text with a glitch effect.
     */
    private void renderGlitchAnimation(StringBuilder display, String text, int animationStep) {
        int length = Math.min(text.length(), maxLength);
        
        // Only glitch occasionally
        boolean glitchActive = animationStep % 20 < 3;
        
        if (glitchActive) {
            // Replace some characters with "glitched" versions
            StringBuilder glitchedText = new StringBuilder(text);
            
            // Number of characters to glitch
            int glitchCount = Math.min(3, length / 4);
            
            for (int i = 0; i < glitchCount; i++) {
                int pos = (animationStep + i * 7) % length;
                char replacement = "!@#$%^&*+_-=~`|\\/<>[]{}".charAt(animationStep % 22);
                glitchedText.setCharAt(pos, replacement);
            }
            
            // Display with a glitched color
            RGBColor glitchColor = RGBColor.of(255, 50, 50); // Reddish
            display.append(glitchColor.apply(glitchedText.substring(0, length)));
        } else {
            // Display normal text
            display.append(applyColor(text.substring(0, length), getMessageColor()));
        }
    }
}