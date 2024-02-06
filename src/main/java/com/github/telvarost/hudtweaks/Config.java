package com.github.telvarost.hudtweaks;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.glasslauncher.mods.api.gcapi.api.MaxLength;

public class Config {

    @GConfig(value = "config", visibleName = "AnnoyanceFix Config")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {
        @ConfigName("Boat Drop Fixes Enabled")
        public static Boolean boatDropFixesEnabled = true;

        @ConfigName("Boat Speed Collision Behavior")
        @MaxLength(3)
        @Comment("0 = vanilla, 1 = drop boat, 2 = invincible")
        public static Integer boatCollisionBehavior = 2;
    }
}