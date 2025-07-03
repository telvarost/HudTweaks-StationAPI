package com.github.telvarost.hudtweaks.enums;

public enum ScreenPositionVerticalEnum {
    BOTTOM("Bottom"),
    CENTERED("Centered"),
    TOP("Top");

    final String stringValue;

    ScreenPositionVerticalEnum() {
        this.stringValue = "Bottom";
    }

    ScreenPositionVerticalEnum(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}