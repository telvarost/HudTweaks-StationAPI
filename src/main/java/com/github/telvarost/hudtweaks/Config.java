package com.github.telvarost.hudtweaks;

import net.glasslauncher.mods.gcapi3.api.*;

public class Config {

    @ConfigRoot(value = "config", visibleName = "HUD Tweaks")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {
        @ConfigEntry(name = "Allow Chat Scroll")
        public Boolean enableChatScroll = true;

        @ConfigEntry(
                name = "Chat History Size",
                description = "50 = vanilla, 100 = default",
                maxLength = 4096
        )
        public Integer chatHistorySize = 100;

        @ConfigEntry(
                name = "Chat Message Fade Time",
                description = "100 = vanilla, 10 = 1 second",
                maxLength = 32000
        )
        public Integer chatFadeTime = 100;

        @ConfigEntry(name = "Coordinate Display For Debug Overlay")
        public CoordinateDisplayEnum coordinateDisplay = CoordinateDisplayEnum.SHOW;

        @ConfigEntry(name = "Disable Crosshair")
        public Boolean disableCrosshair = false;

        @ConfigEntry(name = "Draw Xbox X And Y Buttons")
        public Boolean drawXboxXAndYButtons = false;

        @ConfigEntry(
                name = "Enable Experimental Fix For Raised Hotbar",
                description = "For fixing blocks rendering over chat messages"
        )
        public Boolean enableExperimentalFixForRaisedHotbar = false;

        @ConfigEntry(name = "Enable Hotbar Item Selection Tooltips")
        public Boolean enableHotbarItemSelectionTooltips = false;

        @ConfigEntry(
                name = "Hotbar Item Selection Fade Time",
                description = "40 = default",
                maxLength = 32000
        )
        public Integer hotbarItemSelectionFadeTime = 40;

        @ConfigEntry(
                name = "Hotbar Position",
                description = "0 = vanilla, 32 = xbox, 218 = top",
                maxLength = 32000
        )
        public Integer hotbarYPositionOffset = 0;

        @ConfigEntry(name = "Put Item Selection Tooltip Below Hotbar")
        public Boolean putItemSelectionTooltipBelowHotbar = false;

        @ConfigEntry(name = "Put Status Bar Icons Below Hotbar")
        public Boolean putStatusBarIconsBelowHotbar = false;
    }
}