package com.github.telvarost.hudtweaks;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.glasslauncher.mods.api.gcapi.api.MaxLength;

public class Config {

    @GConfig(value = "config", visibleName = "AnnoyanceFix Config")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {
        @ConfigName("Chat Scroll Enabled")
        public static Boolean enableChatScroll = true;

        @ConfigName("Chat History Size")
        @MaxLength(4096)
        @Comment("50 = vanilla, 200 = default")
        public static Integer chatHistorySize = 200;

        @ConfigName("Hotbar Position")
        @MaxLength(200)
        @Comment("0 = vanilla, 32 = xbox, 200 = top")
        public static Integer hotbarYPositionOffset = 0;
    }
}