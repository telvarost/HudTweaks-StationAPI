package com.github.telvarost.hudtweaks;

import com.github.telvarost.hudtweaks.enums.CoordinateDisplayEnum;
import com.github.telvarost.hudtweaks.enums.ScreenPositionHorizontalEnum;
import com.github.telvarost.hudtweaks.enums.ScreenPositionVerticalEnum;
import net.glasslauncher.mods.gcapi3.api.*;

public class Config {

    @ConfigRoot(value = "config", visibleName = "HUD Tweaks")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {
        @ConfigCategory(
                name = "In-Game UI Element Positions Config"
        )
        public InGameUIPositionsConfig UI_POSITIONS_CONFIG = new InGameUIPositionsConfig();

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

    public static class InGameUIPositionsConfig {
        @ConfigCategory(
                name = "Item Hotbar Position Config"
        )
        public ItemHotbarPositionConfig HOTBAR_POSITION_CONFIG = new ItemHotbarPositionConfig();

        @ConfigCategory(
                name = "Hearts Position Config"
        )
        public HeartsPositionConfig HEARTS_POSITION_CONFIG = new HeartsPositionConfig();

        @ConfigCategory(
                name = "Armor Position Config"
        )
        public ArmorPositionConfig ARMOR_POSITION_CONFIG = new ArmorPositionConfig();

        @ConfigCategory(
                name = "Oxygen Position Config"
        )
        public OxygenPositionConfig OXYGEN_POSITION_CONFIG = new OxygenPositionConfig();

        @ConfigCategory(
                name = "Overlay Message Position Config"
        )
        public OverlayMessagePositionConfig OVERLAY_MESSAGE_POSITION_CONFIG = new OverlayMessagePositionConfig();

        @ConfigEntry(name = "Put Overlay Messages Below Hotbar")
        public Boolean putOverlayMessagesBelowHotbar = false;

        @ConfigEntry(name = "Put Status Bar Icons Below Hotbar")
        public Boolean putStatusBarIconsBelowHotbar = false;
    }

    public static class ItemHotbarPositionConfig {
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