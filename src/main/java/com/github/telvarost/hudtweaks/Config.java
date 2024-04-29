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

        @ConfigName("Hotbar Position")
        @MaxLength(200)
        @Comment("0 = vanilla, 32 = xbox, 200 = top")
        public Integer hotbarYPositionOffset = 0;
    }
}