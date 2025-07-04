package com.github.telvarost.hudtweaks.mixin;

import com.github.telvarost.hudtweaks.Config;
import com.github.telvarost.hudtweaks.ModHelper;
import com.github.telvarost.hudtweaks.enums.CoordinateDisplayEnum;
import com.github.telvarost.hudtweaks.enums.HudPositioningSystemEnum;
import com.github.telvarost.hudtweaks.enums.ScreenPositionHorizontalEnum;
import com.github.telvarost.hudtweaks.enums.ScreenPositionVerticalEnum;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawContext {

    @Shadow private Minecraft minecraft;
    @Shadow private List messages;
    @Shadow private Random random;
    @Shadow private int overlayRemaining;
    @Shadow private String overlayMessage;

    @Shadow protected abstract void renderHotbarItem(int slot, int x, int y, float f);

    @Unique private int numberOfTurns    = 0;
    @Unique private int chatOffset       = 0;
    @Unique private int prevSelectedSlot = 0;
    @Unique private int scaledWidth    = 0;
    @Unique private int scaledHeight   = 0;
    @Unique private boolean isHotbarRaised = false;
    @Unique private boolean hotbarVisibility  = true;
    @Unique private int xOffsetHotbar  = 0;
    @Unique private int yOffsetHotbar  = 0;
    @Unique private boolean heartsVisibility  = true;
    @Unique private int xOffsetHearts  = 0;
    @Unique private int yOffsetHearts  = 0;
    @Unique private boolean armorVisibility   = true;
    @Unique private int xOffsetArmor   = 0;
    @Unique private int yOffsetArmor   = 0;
    @Unique private boolean oxygenVisibility  = true;
    @Unique private int xOffsetOxygen  = 0;
    @Unique private int yOffsetOxygen  = 0;
    @Unique private boolean overlayVisibility = true;
    @Unique private int xOffsetOverlay = 0;
    @Unique private int yOffsetOverlay = 0;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void hudTweaks_render(float tickDelta, boolean screenOpen, int mouseX, int mouseY, CallbackInfo ci) {
        ScreenScaler screenScaler = new ScreenScaler(this.minecraft.options, this.minecraft.displayWidth, this.minecraft.displayHeight);

        if (  (scaledWidth != screenScaler.getScaledWidth())
           || (scaledHeight != screenScaler.getScaledHeight())
           || (ModHelper.configUpdated)
           )
        {
            scaledWidth = screenScaler.getScaledWidth();
            scaledHeight = screenScaler.getScaledHeight();
            ModHelper.configUpdated = false;

            if (HudPositioningSystemEnum.SIMPLE == Config.hudpositions.hudPositioningSystem) {
                if (Config.hudpositions.SIMPLE_HUD_POSITION_CONFIG.enableVisibility) {
                    int xOffset = 0;
                    int yOffset = 0;

                    if (ScreenPositionHorizontalEnum.LEFT == Config.hudpositions.SIMPLE_HUD_POSITION_CONFIG.horizontalPosition) {
                        xOffset = 91 - (scaledWidth / 2);
                    } else if (ScreenPositionHorizontalEnum.RIGHT == Config.hudpositions.SIMPLE_HUD_POSITION_CONFIG.horizontalPosition) {
                        xOffset = -91 + (scaledWidth / 2) + (scaledWidth % 2);
                    }

                    if (ScreenPositionVerticalEnum.TOP == Config.hudpositions.SIMPLE_HUD_POSITION_CONFIG.verticalPosition) {
                        yOffset = 22 - (scaledHeight);
                    } else if (ScreenPositionVerticalEnum.CENTERED == Config.hudpositions.SIMPLE_HUD_POSITION_CONFIG.verticalPosition) {
                        yOffset = 11 - (scaledHeight / 2) - (scaledHeight % 2);
                    }

                    xOffset += Config.hudpositions.SIMPLE_HUD_POSITION_CONFIG.horizontalPositionOffset;
                    yOffset += Config.hudpositions.SIMPLE_HUD_POSITION_CONFIG.verticalPositionOffset;

                    xOffsetHotbar  = xOffset;
                    yOffsetHotbar  = yOffset;
                    xOffsetHearts  = xOffset;
                    yOffsetHearts  = yOffset;
                    xOffsetArmor   = xOffset;
                    yOffsetArmor   = yOffset;
                    xOffsetOxygen  = xOffset;
                    yOffsetOxygen  = yOffset;
                    xOffsetOverlay = xOffset;
                    yOffsetOverlay = yOffset;

                    if (  (ScreenPositionVerticalEnum.TOP == Config.hudpositions.SIMPLE_HUD_POSITION_CONFIG.verticalPosition)
                       || (ScreenPositionVerticalEnum.CENTERED == Config.hudpositions.SIMPLE_HUD_POSITION_CONFIG.verticalPosition)
                       || (0 > yOffsetHotbar)
                    ) {
                        isHotbarRaised = true;
                    } else {
                        isHotbarRaised = false;
                    }

                    if (Config.hudpositions.putStatusBarIconsBelowHotbar) {
                        yOffsetHearts  += 33;
                        yOffsetArmor   += 33;
                        yOffsetOxygen  += 51;
                    }

                    if (Config.hudpositions.putOverlayMessagesBelowHotbar) {
                        yOffsetOverlay += 74;
                    }

                    hotbarVisibility  = true;
                    heartsVisibility  = true;
                    armorVisibility   = true;
                    oxygenVisibility  = true;
                    overlayVisibility = true;
                } else {
                    hotbarVisibility  = false;
                    heartsVisibility  = false;
                    armorVisibility   = false;
                    oxygenVisibility  = false;
                    overlayVisibility = false;

                }
            } else if (HudPositioningSystemEnum.ADVANCED == Config.hudpositions.hudPositioningSystem) {
                xOffsetHotbar  = 0;
                yOffsetHotbar  = 0;
                xOffsetHearts  = 0;
                yOffsetHearts  = 0;
                xOffsetArmor   = 0;
                yOffsetArmor   = 0;
                xOffsetOxygen  = 0;
                yOffsetOxygen  = 0;
                xOffsetOverlay = 0;
                yOffsetOverlay = 0;

                if (Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.enableVisibility) {
                    if (ScreenPositionHorizontalEnum.LEFT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.horizontalPosition) {
                        xOffsetHotbar = 91 - (scaledWidth / 2);
                    } else if (ScreenPositionHorizontalEnum.RIGHT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.horizontalPosition) {
                        xOffsetHotbar = -91 + (scaledWidth / 2) + (scaledWidth % 2);
                    }

                    if (ScreenPositionVerticalEnum.TOP == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPosition) {
                        yOffsetHotbar = 22 - (scaledHeight);
                    } else if (ScreenPositionVerticalEnum.CENTERED == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPosition) {
                        yOffsetHotbar = 11 - (scaledHeight / 2) - (scaledHeight % 2);
                    }

                    xOffsetHotbar += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.horizontalPositionOffset;
                    yOffsetHotbar += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPositionOffset;

                    if (  (ScreenPositionVerticalEnum.TOP == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPosition)
                       || (ScreenPositionVerticalEnum.CENTERED == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPosition)
                       || (0 > yOffsetHotbar)
                    ) {
                        isHotbarRaised = true;
                    } else {
                        isHotbarRaised = false;
                    }

                    hotbarVisibility  = true;
                } else {
                    hotbarVisibility  = false;
                }

                if (Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HEARTS_POSITION_CONFIG.enableVisibility) {
                    if (ScreenPositionHorizontalEnum.LEFT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HEARTS_POSITION_CONFIG.horizontalPosition) {
                        xOffsetHearts = 91 - (scaledWidth / 2);
                    } else if (ScreenPositionHorizontalEnum.RIGHT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HEARTS_POSITION_CONFIG.horizontalPosition) {
                        xOffsetHearts = -91 + (scaledWidth / 2) + (scaledWidth % 2);
                    }

                    if (ScreenPositionVerticalEnum.TOP == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HEARTS_POSITION_CONFIG.verticalPosition) {
                        yOffsetHearts = 22 - (scaledHeight);
                    } else if (ScreenPositionVerticalEnum.CENTERED == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HEARTS_POSITION_CONFIG.verticalPosition) {
                        yOffsetHearts = 11 - (scaledHeight / 2) - (scaledHeight % 2);
                    }

                    xOffsetHearts += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HEARTS_POSITION_CONFIG.horizontalPositionOffset;
                    yOffsetHearts += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.HEARTS_POSITION_CONFIG.verticalPositionOffset;

                    if (Config.hudpositions.putStatusBarIconsBelowHotbar) {
                        yOffsetHearts += 33;
                    }

                    heartsVisibility  = true;
                } else {
                    heartsVisibility  = false;
                }

                if (Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.ARMOR_POSITION_CONFIG.enableVisibility) {
                    if (ScreenPositionHorizontalEnum.LEFT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.ARMOR_POSITION_CONFIG.horizontalPosition) {
                        xOffsetArmor = 91 - (scaledWidth / 2);
                    } else if (ScreenPositionHorizontalEnum.RIGHT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.ARMOR_POSITION_CONFIG.horizontalPosition) {
                        xOffsetArmor = -91 + (scaledWidth / 2) + (scaledWidth % 2);
                    }

                    if (ScreenPositionVerticalEnum.TOP == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.ARMOR_POSITION_CONFIG.verticalPosition) {
                        yOffsetArmor = 22 - (scaledHeight);
                    } else if (ScreenPositionVerticalEnum.CENTERED == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.ARMOR_POSITION_CONFIG.verticalPosition) {
                        yOffsetArmor = 11 - (scaledHeight / 2) - (scaledHeight % 2);
                    }

                    xOffsetArmor += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.ARMOR_POSITION_CONFIG.horizontalPositionOffset;
                    yOffsetArmor += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.ARMOR_POSITION_CONFIG.verticalPositionOffset;

                    if (Config.hudpositions.putStatusBarIconsBelowHotbar) {
                        yOffsetArmor += 33;
                    }

                    armorVisibility  = true;
                } else {
                    armorVisibility  = false;
                }

                if (Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OXYGEN_POSITION_CONFIG.enableVisibility) {
                    if (ScreenPositionHorizontalEnum.LEFT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OXYGEN_POSITION_CONFIG.horizontalPosition) {
                        xOffsetOxygen = 91 - (scaledWidth / 2);
                    } else if (ScreenPositionHorizontalEnum.RIGHT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OXYGEN_POSITION_CONFIG.horizontalPosition) {
                        xOffsetOxygen = -91 + (scaledWidth / 2) + (scaledWidth % 2);
                    }

                    if (ScreenPositionVerticalEnum.TOP == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OXYGEN_POSITION_CONFIG.verticalPosition) {
                        yOffsetOxygen = 22 - (scaledHeight);
                    } else if (ScreenPositionVerticalEnum.CENTERED == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OXYGEN_POSITION_CONFIG.verticalPosition) {
                        yOffsetOxygen = 11 - (scaledHeight / 2) - (scaledHeight % 2);
                    }

                    xOffsetOxygen += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OXYGEN_POSITION_CONFIG.horizontalPositionOffset;
                    yOffsetOxygen += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OXYGEN_POSITION_CONFIG.verticalPositionOffset;

                    if (Config.hudpositions.putStatusBarIconsBelowHotbar) {
                        yOffsetOxygen += 51;
                    }

                    oxygenVisibility  = true;
                } else {
                    oxygenVisibility  = false;
                }

                if (Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OVERLAY_MESSAGE_POSITION_CONFIG.enableVisibility) {
                    if (ScreenPositionHorizontalEnum.LEFT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OVERLAY_MESSAGE_POSITION_CONFIG.horizontalPosition) {
                        xOffsetOverlay = 91 - (scaledWidth / 2);
                    } else if (ScreenPositionHorizontalEnum.RIGHT == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OVERLAY_MESSAGE_POSITION_CONFIG.horizontalPosition) {
                        xOffsetOverlay = -91 + (scaledWidth / 2) + (scaledWidth % 2);
                    }

                    if (ScreenPositionVerticalEnum.TOP == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OVERLAY_MESSAGE_POSITION_CONFIG.verticalPosition) {
                        yOffsetOverlay = 22 - (scaledHeight);
                    } else if (ScreenPositionVerticalEnum.CENTERED == Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OVERLAY_MESSAGE_POSITION_CONFIG.verticalPosition) {
                        yOffsetOverlay = 11 - (scaledHeight / 2) - (scaledHeight % 2);
                    }

                    xOffsetOverlay += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OVERLAY_MESSAGE_POSITION_CONFIG.horizontalPositionOffset;
                    yOffsetOverlay += Config.hudpositions.ADVANCED_HUD_POSITIONS_CONFIG.OVERLAY_MESSAGE_POSITION_CONFIG.verticalPositionOffset;

                    if (Config.hudpositions.putOverlayMessagesBelowHotbar) {
                        yOffsetOverlay += 74;
                    }

                    overlayVisibility  = true;
                } else {
                    overlayVisibility  = false;
                }

            } else {
                // HudPositioningSystemEnum.DISABLED
                isHotbarRaised = false;
                xOffsetHotbar  = 0;
                yOffsetHotbar  = 0;
                xOffsetHearts  = 0;
                yOffsetHearts  = 0;
                xOffsetArmor   = 0;
                yOffsetArmor   = 0;
                xOffsetOxygen  = 0;
                yOffsetOxygen  = 0;
                xOffsetOverlay = 0;
                yOffsetOverlay = 0;
                hotbarVisibility  = true;
                heartsVisibility  = true;
                armorVisibility   = true;
                oxygenVisibility  = true;
                overlayVisibility = true;
            }
        }

        if (Config.config.enableChatScroll) {
            int chatRangeTop = 20;

            if (this.minecraft.currentScreen instanceof ChatScreen) {
                int currentWheelDegrees = Mouse.getDWheel();
                numberOfTurns = Math.round((float)currentWheelDegrees / 120.0f);
                chatOffset = chatOffset + numberOfTurns;

                if (chatOffset < 0) {
                    chatOffset = 0;
                }

                if (chatRangeTop < this.messages.size()) {
                    if (this.messages.size() <= (chatRangeTop + chatOffset)) {
                        chatOffset = this.messages.size() - chatRangeTop;
                    }
                } else {
                    chatOffset = 0;
                }
            } else {
                chatOffset = 0;
            }
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 0
            )
    )
    private void hudTweaks_renderHotbar(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (hotbarVisibility) {
            x += xOffsetHotbar;
            y += yOffsetHotbar;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 1
            )
    )
    private void hudTweaks_renderSelectedItemBorder(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (hotbarVisibility) {
            x += xOffsetHotbar;
            y += yOffsetHotbar;

            if (isHotbarRaised) {
                height += 2;
            }

            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 3
            )
    )
    private void hudTweaks_renderArmor1(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (armorVisibility) {
            x += xOffsetArmor;
            y += yOffsetArmor;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 4
            )
    )
    private void hudTweaks_renderArmor2(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (armorVisibility) {
            x += xOffsetArmor;
            y += yOffsetArmor;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 5
            )
    )
    private void hudTweaks_renderArmor3(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (armorVisibility) {
            x += xOffsetArmor;
            y += yOffsetArmor;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 6
            )
    )
    private void hudTweaks_renderHearts1(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (heartsVisibility) {
            x += xOffsetHearts;
            y += yOffsetHearts;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 7
            )
    )
    private void hudTweaks_renderHearts2(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (heartsVisibility) {
            x += xOffsetHearts;
            y += yOffsetHearts;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 8
            )
    )
    private void hudTweaks_renderHearts3(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (heartsVisibility) {
            x += xOffsetHearts;
            y += yOffsetHearts;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 9
            )
    )
    private void hudTweaks_renderHearts4(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (heartsVisibility) {
            x += xOffsetHearts;
            y += yOffsetHearts;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 10
            )
    )
    private void hudTweaks_renderHearts5(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (heartsVisibility) {
            x += xOffsetHearts;
            y += yOffsetHearts;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 11
            )
    )
    private void hudTweaks_renderOxygen1(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (oxygenVisibility) {
            x += xOffsetOxygen;
            y += yOffsetOxygen;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 12
            )
    )
    private void hudTweaks_renderOxygen2(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (oxygenVisibility) {
            x += xOffsetOxygen;
            y += yOffsetOxygen;
            original.call(instance, x, y, u, v, width, height);
        }
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(intValue = 200)
    )
    private int hudTweaks_renderChatFadeTime(int value) {
        return (Config.config.chatFadeTime * 2);
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(doubleValue = 200.0)
    )
    private double hudTweaks_renderChatFadeTimeDivisor(double value) {
        return (Config.config.chatFadeTime * 2);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 2
            )
    )
    private void hudTweaks_renderCursor(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (!Config.config.disableCrosshair) {
            original.call(instance, x, y, u, v, width, height);
        } else {
            original.call(instance, 0, 0, 0, 0, 0, 0);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 2
            )
    )
    public void hudTweaks_renderDrawCoordinate_X(InGameHud instance, TextRenderer textRenderer, String s, int i, int j, int k, Operation<Void> original) {
        if (CoordinateDisplayEnum.HIDE == Config.config.coordinateDisplay) {
            original.call(instance, textRenderer, "", i, j, k);
        } else if (CoordinateDisplayEnum.RANDOMIZE == Config.config.coordinateDisplay) {
            double randomVal = random.nextDouble(-10000, 10000);
            original.call(instance, textRenderer, "x: " + randomVal, i, j, k);
        } else {
            original.call(instance, textRenderer, s, i, j, k);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 3
            )
    )
    public void hudTweaks_renderDrawCoordinate_Y(InGameHud instance, TextRenderer textRenderer, String s, int i, int j, int k, Operation<Void> original) {
        if (CoordinateDisplayEnum.HIDE == Config.config.coordinateDisplay) {
            original.call(instance, textRenderer, "", i, j, k);
        } else if (CoordinateDisplayEnum.RANDOMIZE == Config.config.coordinateDisplay) {
            double randomVal = random.nextDouble(-10000, 10000);
            original.call(instance, textRenderer, "y: " + randomVal, i, j, k);
        } else {
            original.call(instance, textRenderer, s, i, j, k);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 4
            )
    )
    public void hudTweaks_renderDrawCoordinate_Z(InGameHud instance, TextRenderer textRenderer, String s, int i, int j, int k, Operation<Void> original) {
        if (CoordinateDisplayEnum.HIDE == Config.config.coordinateDisplay) {
            original.call(instance, textRenderer, "", i, j, k);
        } else if (CoordinateDisplayEnum.RANDOMIZE == Config.config.coordinateDisplay) {
            double randomVal = random.nextDouble(-10000, 10000);
            original.call(instance, textRenderer, "z: " + randomVal, i, j, k);
        } else {
            original.call(instance, textRenderer, s, i, j, k);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(IIIF)V"
            )
    )
    public void hudTweaks_renderHotbarItem(InGameHud instance, int slot, int x, int y, float f, Operation<Void> original) {
        if (hotbarVisibility) {
            x += xOffsetHotbar;
            y += yOffsetHotbar;
            original.call(instance, slot, x, y, f);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/platform/Lighting;turnOff()V"
            )
    )
    public void hudTweaks_glClearAndItemSelectedTooltip(Operation<Void> original) {
        original.call();

        if (Config.config.enableHotbarBlockRenderingFix) {
            GL11.glClear(256);
        }

        if (Config.config.enableHotbarItemSelectionTooltips) {
            PlayerInventory playerInventory = this.minecraft.player.inventory;
            if (prevSelectedSlot != playerInventory.selectedSlot) {
                prevSelectedSlot = playerInventory.selectedSlot;
                ItemStack selectedItemStack = playerInventory.getSelectedItem();
                if (null != selectedItemStack) {
                    TranslationStorage translationStorage = TranslationStorage.getInstance();
                    this.overlayMessage = translationStorage.get(selectedItemStack.getTranslationKey() + ".name");
                    this.overlayRemaining = Config.config.hotbarItemSelectionFadeTime;
                }
            }
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V",
                    ordinal = 1
            )
    )
    public void hudTweaks_overlayMesssagePosition(float x, float y, float z, Operation<Void> original) {
        x += xOffsetOverlay;
        y += yOffsetOverlay;
        original.call(x, y, z);
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;III)V"
            )
    )
    public void hudTweaks_overlayMesssagePosition(TextRenderer instance, String text, int x, int y, int color, Operation<Void> original) {
        if (overlayVisibility) {
            original.call(instance, text, x, y, color);
        }
    }

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;get(I)Ljava/lang/Object;"
            )
    )
    public Object hudTweaks_renderChatOffset(List instance, int i, Operation<Void> original) {
        if (Config.config.enableChatScroll) {
            return original.call(instance, i + chatOffset);
        } else {
            return original.call(instance, i);
        }
    }

    @ModifyConstant(
            method = "addChatMessage",
            constant = @Constant(intValue = 50)
    )
    public int chatLog_addChatMessageLimit(int value) {
        return Config.config.chatHistorySize;
    }

    @Inject(method = "render", at = @At("RETURN"), cancellable = true)
    public void hudTweaks_renderXboxButtons(float f, boolean bl, int i, int j, CallbackInfo ci) {
        if (Config.config.drawXboxXAndYButtons) {
            ScreenScaler var5 = new ScreenScaler(this.minecraft.options, this.minecraft.displayWidth, this.minecraft.displayHeight);
            //int var6 = var5.method_1857();
            int var7 = var5.getScaledHeight();
            GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, minecraft.textureManager.getTextureId("/assets/hudtweaks/button_icons.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexture(10, var7 - 30, (2 % 12) * 20, (2 / 12) * 20, 20, 20);
            TextRenderer var8 = this.minecraft.textRenderer;
            var8.drawWithShadow("Crafting", 31, var7 - 24, 16777215);
            GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, minecraft.textureManager.getTextureId("/assets/hudtweaks/button_icons.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexture(78, var7 - 30, (3 % 12) * 20, (3 / 12) * 20, 20, 20);
            var8.drawWithShadow("Inventory", 99, var7 - 24, 16777215);
        }
    }
}
