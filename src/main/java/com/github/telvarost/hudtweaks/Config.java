package com.github.telvarost.hudtweaks;

import com.github.telvarost.hudtweaks.enums.CoordinateDisplayEnum;
import com.github.telvarost.hudtweaks.enums.HudPositioningSystemEnum;
import com.github.telvarost.hudtweaks.enums.ScreenPositionHorizontalEnum;
import com.github.telvarost.hudtweaks.enums.ScreenPositionVerticalEnum;
import net.glasslauncher.mods.gcapi3.api.*;

public class Config {

    @ConfigRoot(value = "config", visibleName = "HUD Tweaks", index = 0)
    public static final Config.ConfigFields config = new Config.ConfigFields();

    @ConfigRoot(value = "hudpositions", visibleName = "HUD Positions", index = 1)
    public static final Config.HudPositionsFields hudpositions = new Config.HudPositionsFields();

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

        @ConfigEntry(name = "Disable Vignette")
        public Boolean disableVignette = false;

        @ConfigEntry(name = "Draw Xbox X And Y Buttons")
        public Boolean drawXboxXAndYButtons = false;

        @ConfigEntry(
                name = "Enable Hotbar Block Rendering Fix",
                description = "For fixing blocks rendering over messages"
        )
        public Boolean enableHotbarBlockRenderingFix = true;

        @ConfigEntry(name = "Enable Hotbar Item Selection Tooltips")
        public Boolean enableHotbarItemSelectionTooltips = false;

        @ConfigEntry(
                name = "Hotbar Item Selection Fade Time",
                description = "40 = default",
                maxLength = 32000
        )
        public Integer hotbarItemSelectionFadeTime = 40;
    }

    public static class HudPositionsFields {
        @ConfigCategory(
                name = "Advanced HUD Positions Config"
        )
        public AdvancedHudPositionsConfig ADVANCED_HUD_POSITIONS_CONFIG = new AdvancedHudPositionsConfig();

        @ConfigCategory(
                name = "Simple HUD Position Config"
        )
        public SimpleHudPositionConfig SIMPLE_HUD_POSITION_CONFIG = new SimpleHudPositionConfig();

        @ConfigEntry(
                name = "HUD Positioning System",
                description = "Which configs or defaults to use for the HUD"
        )
        public HudPositioningSystemEnum hudPositioningSystem = HudPositioningSystemEnum.SIMPLE;

        @ConfigEntry(
                name = "Put Overlay Messages Below Hotbar",
                description = "Adjusts overlay text vertical offset only"
        )
        public Boolean putOverlayMessagesBelowHotbar = false;

        @ConfigEntry(
                name = "Put Status Bar Icons Below Hotbar",
                description = "Adjusts status bar vertical offsets only"
        )
        public Boolean putStatusBarIconsBelowHotbar = false;
    }

    public static class AdvancedHudPositionsConfig {
        @ConfigCategory(
                name = "Advanced Item Hotbar Position Config"
        )
        public HotbarPositionConfig HOTBAR_POSITION_CONFIG = new HotbarPositionConfig();

        @ConfigCategory(
                name = "Advanced Health Bar Position Config"
        )
        public HeartsPositionConfig HEARTS_POSITION_CONFIG = new HeartsPositionConfig();

        @ConfigCategory(
                name = "Advanced Armor Bar Position Config"
        )
        public ArmorPositionConfig ARMOR_POSITION_CONFIG = new ArmorPositionConfig();

        @ConfigCategory(
                name = "Advanced Oxygen Bar Position Config"
        )
        public OxygenPositionConfig OXYGEN_POSITION_CONFIG = new OxygenPositionConfig();

        @ConfigCategory(
                name = "Advanced Overlay Message Position Config"
        )
        public OverlayMessagePositionConfig OVERLAY_MESSAGE_POSITION_CONFIG = new OverlayMessagePositionConfig();
    }

    public static class SimpleHudPositionConfig {
        @ConfigEntry(
                name = "Enable Visibility"
        )
        public Boolean enableVisibility = true;

        @ConfigEntry(
                name = "Horizontal Position"
        )
        public ScreenPositionHorizontalEnum horizontalPosition = ScreenPositionHorizontalEnum.CENTERED;

        @ConfigEntry(
                name = "Horizontal Position Offset",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer horizontalPositionOffset = 0;

        @ConfigEntry(
                name = "Vertical Position"
        )
        public ScreenPositionVerticalEnum verticalPosition = ScreenPositionVerticalEnum.BOTTOM;

        @ConfigEntry(
                name = "Vertical Position Offset",
                description = "Use position bottom with offset -32 for Xbox",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer verticalPositionOffset = 0;
    }

    public static class HotbarPositionConfig {
        @ConfigEntry(
                name = "Enable Visibility"
        )
        public Boolean enableVisibility = true;

        @ConfigEntry(
                name = "Horizontal Position"
        )
        public ScreenPositionHorizontalEnum horizontalPosition = ScreenPositionHorizontalEnum.CENTERED;

        @ConfigEntry(
                name = "Horizontal Position Offset",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer horizontalPositionOffset = 0;

        @ConfigEntry(
                name = "Vertical Position"
        )
        public ScreenPositionVerticalEnum verticalPosition = ScreenPositionVerticalEnum.BOTTOM;

        @ConfigEntry(
                name = "Vertical Position Offset",
                description = "Use position bottom with offset -32 for Xbox",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer verticalPositionOffset = 0;
    }

    public static class HeartsPositionConfig {
        @ConfigEntry(
                name = "Enable Visibility"
        )
        public Boolean enableVisibility = true;

        @ConfigEntry(
                name = "Horizontal Position"
        )
        public ScreenPositionHorizontalEnum horizontalPosition = ScreenPositionHorizontalEnum.CENTERED;

        @ConfigEntry(
                name = "Horizontal Position Offset",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer horizontalPositionOffset = 0;

        @ConfigEntry(
                name = "Vertical Position"
        )
        public ScreenPositionVerticalEnum verticalPosition = ScreenPositionVerticalEnum.BOTTOM;

        @ConfigEntry(
                name = "Vertical Position Offset",
                description = "Use position bottom with offset -32 for Xbox",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer verticalPositionOffset = 0;
    }

    public static class ArmorPositionConfig {
        @ConfigEntry(
                name = "Enable Visibility"
        )
        public Boolean enableVisibility = true;

        @ConfigEntry(
                name = "Horizontal Position"
        )
        public ScreenPositionHorizontalEnum horizontalPosition = ScreenPositionHorizontalEnum.CENTERED;

        @ConfigEntry(
                name = "Horizontal Position Offset",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer horizontalPositionOffset = 0;

        @ConfigEntry(
                name = "Vertical Position"
        )
        public ScreenPositionVerticalEnum verticalPosition = ScreenPositionVerticalEnum.BOTTOM;

        @ConfigEntry(
                name = "Vertical Position Offset",
                description = "Use position bottom with offset -32 for Xbox",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer verticalPositionOffset = 0;
    }

    public static class OxygenPositionConfig {
        @ConfigEntry(
                name = "Enable Visibility"
        )
        public Boolean enableVisibility = true;

        @ConfigEntry(
                name = "Horizontal Position"
        )
        public ScreenPositionHorizontalEnum horizontalPosition = ScreenPositionHorizontalEnum.CENTERED;

        @ConfigEntry(
                name = "Horizontal Position Offset",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer horizontalPositionOffset = 0;

        @ConfigEntry(
                name = "Vertical Position"
        )
        public ScreenPositionVerticalEnum verticalPosition = ScreenPositionVerticalEnum.BOTTOM;

        @ConfigEntry(
                name = "Vertical Position Offset",
                description = "Use position bottom with offset -32 for Xbox",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer verticalPositionOffset = 0;
    }

    public static class OverlayMessagePositionConfig {
        @ConfigEntry(
                name = "Enable Visibility"
        )
        public Boolean enableVisibility = true;

        @ConfigEntry(
                name = "Horizontal Position"
        )
        public ScreenPositionHorizontalEnum horizontalPosition = ScreenPositionHorizontalEnum.CENTERED;

        @ConfigEntry(
                name = "Horizontal Position Offset",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer horizontalPositionOffset = 0;

        @ConfigEntry(
                name = "Vertical Position"
        )
        public ScreenPositionVerticalEnum verticalPosition = ScreenPositionVerticalEnum.BOTTOM;

        @ConfigEntry(
                name = "Vertical Position Offset",
                description = "Use position bottom with offset -32 for Xbox",
                minLength = -32000,
                maxLength = 32000
        )
        public Integer verticalPositionOffset = 0;
    }
}