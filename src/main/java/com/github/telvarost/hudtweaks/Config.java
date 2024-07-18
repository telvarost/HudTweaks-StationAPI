package com.github.telvarost.hudtweaks;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.glasslauncher.mods.api.gcapi.api.MaxLength;

public class Config {

    @GConfig(value = "config", visibleName = "HUD Tweaks")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {
        @ConfigName("Allow Chat Scroll")
        public Boolean enableChatScroll = true;

        @ConfigName("Chat History Size")
        @MaxLength(4096)
        @Comment("50 = vanilla, 100 = default")
        public Integer chatHistorySize = 100;

        @ConfigName("Chat Message Fade Time")
        @MaxLength(32000)
        @Comment("100 = vanilla, 10 = 1 second")
        public Integer chatFadeTime = 100;

        @ConfigName("Coordinate Display For Debug Overlay")
        public CoordinateDisplayEnum coordinateDisplay = CoordinateDisplayEnum.SHOW;

        @ConfigName("Disable Crosshair")
        public Boolean disableCrosshair = false;

        @ConfigName("Draw Xbox X And Y Buttons")
        public Boolean drawXboxXAndYButtons = false;

        @ConfigName("Enable Hotbar Item Selection Tooltips")
        public Boolean enableHotbarItemSelectionTooltips = false;

        @ConfigName("Hotbar Item Selection Fade Time")
        @MaxLength(32000)
        @Comment("40 = default")
        public Integer hotbarItemSelectionFadeTime = 40;

        @ConfigName("Hotbar Position")
        @MaxLength(32000)
        @Comment("0 = vanilla, 32 = xbox, 218 = top")
        public Integer hotbarYPositionOffset = 0;

        @ConfigName("Put Item Selection Tooltip Below Hotbar")
        public Boolean putItemSelectionTooltipBelowHotbar = false;

        @ConfigName("Put Status Bar Icons Below Hotbar")
        public Boolean putStatusBarIconsBelowHotbar = false;
    }
}