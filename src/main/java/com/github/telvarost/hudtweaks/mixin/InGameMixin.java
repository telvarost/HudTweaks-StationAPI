package com.github.telvarost.hudtweaks.mixin;

import com.github.telvarost.hudtweaks.Config;
import com.github.telvarost.hudtweaks.enums.CoordinateDisplayEnum;
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

@Mixin(InGameHud.class)
@Environment(value= EnvType.CLIENT)
public abstract class InGameMixin extends DrawContext {

    @Shadow private Minecraft minecraft;
    @Shadow private List messages;
    @Shadow private Random random;
    @Shadow private int overlayRemaining;
    @Shadow private String overlayMessage;

    @Shadow protected abstract void renderHotbarItem(int slot, int x, int y, float f);

    @Unique private int numberOfTurns = 0;
    @Unique private int chatOffset = 0;
    @Unique private int prevSelectedSlot = 0;
    @Unique private int scaledWidth = 0;
    @Unique private int scaledHeight = 0;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void hudTweaks_renderChatScroll(float tickDelta, boolean screenOpen, int mouseX, int mouseY, CallbackInfo ci) {
        ScreenScaler var5 = new ScreenScaler(this.minecraft.options, this.minecraft.displayWidth, this.minecraft.displayHeight);
        scaledWidth = var5.getScaledWidth();
        scaledHeight = var5.getScaledHeight();

        if (!Config.config.enableChatScroll) {
            return;
        }

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

    @WrapOperation(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 0
            )
    )
    private void hudTweaks_renderHotbarPosition(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.enableVisibility) {
            if (ScreenPositionHorizontalEnum.LEFT == Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.horizontalPosition) {
                x = 0;
            } else if (ScreenPositionHorizontalEnum.RIGHT == Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.horizontalPosition) {
                x = scaledWidth - 182;
            }

            if (ScreenPositionVerticalEnum.TOP == Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPosition) {
                y = 0;
            } else if (ScreenPositionVerticalEnum.CENTERED == Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPosition) {
                y = scaledHeight / 2 - 22;
            }

            x += Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.horizontalPositionOffset;
            y += Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPositionOffset;

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
    private void hudTweaks_renderSelectedItemBorderPosition(InGameHud instance, int x, int y, int u, int v, int width, int height, Operation<Void> original) {

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

        if (Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.enableVisibility) {
            if (ScreenPositionHorizontalEnum.LEFT == Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.horizontalPosition) {
                x = -1 + this.minecraft.player.inventory.selectedSlot * 20;
            } else if (ScreenPositionHorizontalEnum.RIGHT == Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.horizontalPosition) {
                x = scaledWidth - 183 + this.minecraft.player.inventory.selectedSlot * 20;
            }

            if (ScreenPositionVerticalEnum.TOP == Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPosition) {
                y = -1;
                height += 2;
            } else if (ScreenPositionVerticalEnum.CENTERED == Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPosition) {
                y = scaledHeight / 2 - 23;
                height += 2;
            }

            x += Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.horizontalPositionOffset;
            y += Config.config.UI_POSITIONS_CONFIG.HOTBAR_POSITION_CONFIG.verticalPositionOffset;

            original.call(instance, x, y, u, v, width, height);
        }
    }
//
//    @ModifyConstant(
//            method = "render",
//            constant = @Constant(intValue = 32, ordinal = 0)
//    )
//    private int hudTweaks_renderStatusBarPositions0(int value) {
//        if (Config.config.putStatusBarIconsBelowHotbar) {
//            return (value + Config.config.hotbarYPositionOffset) - 33;
//        } else {
//            return value + Config.config.hotbarYPositionOffset;
//        }
//    }
//
//    @ModifyConstant(
//            method = "render",
//            constant = @Constant(intValue = 32, ordinal = 1)
//    )
//    private int hudTweaks_renderStatusBarPositions1(int value) {
//        if (Config.config.putStatusBarIconsBelowHotbar) {
//            return (value + Config.config.hotbarYPositionOffset) - 50;
//        } else {
//            return value + Config.config.hotbarYPositionOffset;
//        }
//    }
//
//    @ModifyConstant(
//            method = "render",
//            constant = @Constant(intValue = 32, ordinal = 2)
//    )
//    private int hudTweaks_renderStatusBarPositions2(int value) {
//        if (Config.config.putStatusBarIconsBelowHotbar) {
//            return (value + Config.config.hotbarYPositionOffset) - 50;
//        } else {
//            return value + Config.config.hotbarYPositionOffset;
//        }
//    }
//
//    @ModifyConstant(
//            method = "render",
//            constant = @Constant(intValue = 16, ordinal = 5)
//    )
//    private int hudTweaks_renderItemPositions(int value) {
//        return value + Config.config.hotbarYPositionOffset;
//    }
//
//    @ModifyConstant(
//            method = "render",
//            constant = @Constant(intValue = -4)
//    )
//    private int hudTweaks_renderOverlayMessagePosition(int value) {
//        if (Config.config.putItemSelectionTooltipBelowHotbar) {
//            return (value - Config.config.hotbarYPositionOffset) + 74;
//        } else {
//            return value - Config.config.hotbarYPositionOffset;
//        }
//    }

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
    public void hudTweaks_hotbarBlockRendering(InGameHud instance, int slot, int x, int y, float f, Operation<Void> original) {
        original.call(instance, slot, x, y, f);

        if (Config.config.enableHotbarBlockRenderingFix) {
            GL11.glClear(256);
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
    public void hudTweaks_render(float f, boolean bl, int i, int j, CallbackInfo ci) {
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
