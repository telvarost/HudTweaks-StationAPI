package com.github.telvarost.hudtweaks.enums;

public enum CoordinateDisplayEnum {
    SHOW("Show"),
    HIDE("Hide"),
    RANDOMIZE("Randomize");

    final String stringValue;

    CoordinateDisplayEnum() {
        this.stringValue = "Show";
    }

    CoordinateDisplayEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}