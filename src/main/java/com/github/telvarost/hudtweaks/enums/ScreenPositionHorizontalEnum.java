package com.github.telvarost.hudtweaks.enums;

public enum ScreenPositionHorizontalEnum {
    LEFT("Left"),
    CENTERED("Centered"),
    RIGHT("Right");

    final String stringValue;

    ScreenPositionHorizontalEnum() {
        this.stringValue = "Centered";
    }

    ScreenPositionHorizontalEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}