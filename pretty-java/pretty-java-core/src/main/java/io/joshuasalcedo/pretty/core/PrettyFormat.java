package io.joshuasalcedo.pretty.core;

// Core formatting interface
public interface PrettyFormat {
    String format(String text);
    boolean isSupported();
}



