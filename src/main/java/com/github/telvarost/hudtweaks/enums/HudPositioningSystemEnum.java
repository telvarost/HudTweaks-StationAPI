package com.github.telvarost.hudtweaks.enums;

public enum HudPositioningSystemEnum {
    DISABLED("Disabled"),
    SIMPLE("Simple"),
    ADVANCED("Advanced");

    final String stringValue;

    HudPositioningSystemEnum() {
        this.stringValue = "Simple";
    }

    HudPositioningSystemEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}