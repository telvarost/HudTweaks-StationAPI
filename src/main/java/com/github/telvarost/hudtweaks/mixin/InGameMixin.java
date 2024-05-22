package com.github.telvarost.hudtweaks.mixin;

import com.github.telvarost.hudtweaks.Config;
import com.github.telvarost.hudtweaks.CoordinateDisplayEnum;
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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;
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

    @Unique private int numberOfTurns = 0;
    @Unique private int chatOffset = 0;
    @Unique private int prevSelectedSlot = 0;

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 0
            )
    )
    private void hudTweaks_renderHotbarPosition(InGameHud instance, int i, int j, int k, int l, int m, int n) {
        instance.drawTexture(i, j - Config.config.hotbarYPositionOffset, k, l, m, n);
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(IIIIII)V",
                    ordinal = 1
            )
    )
    private void hudTweaks_renderSelectedItemBorderPosition(InGameHud instance, int i, int j, int k, int l, int m, int n) {

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

        instance.drawTexture(i, j - Config.config.hotbarYPositionOffset, k, l, m, n);
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(intValue = 32, ordinal = 0)
    )
    private int hudTweaks_renderStatusBarPositions0(int value) {
        return value + Config.config.hotbarYPositionOffset;
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(intValue = 32, ordinal = 1)
    )
    private int hudTweaks_renderStatusBarPositions1(int value) {
        return value + Config.config.hotbarYPositionOffset;
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(intValue = 32, ordinal = 2)
    )
    private int hudTweaks_renderStatusBarPositions2(int value) {
        return value + Config.config.hotbarYPositionOffset;
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(intValue = 16, ordinal = 5)
    )
    private int hudTweaks_renderItemPositions(int value) {
        return value + Config.config.hotbarYPositionOffset;
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

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void hudTweaks_renderChatScroll(float f, boolean bl, int i, int j, CallbackInfo ci) {
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
                    ordinal = 2
            )
    )
    private void hudTweaks_renderCursor(InGameHud instance, int i, int j, int k, int l, int m, int n, Operation<Void> original) {
        if (!Config.config.disableCrosshair) {
            original.call(instance, i, j, k, l, m, n);
        } else {
            original.call(instance, 0, 0, 0, 0, 0, 0);
        }
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 2
            )
    )
    public void hudTweaks_renderDrawCoordinate_X(InGameHud instance, TextRenderer textRenderer, String s, int i, int j, int k) {
        if (CoordinateDisplayEnum.HIDE == Config.config.coordinateDisplay) {
            instance.drawTextWithShadow(textRenderer, "", i, j, k);
        } else if (CoordinateDisplayEnum.RANDOMIZE == Config.config.coordinateDisplay) {
            double randomVal = random.nextDouble(-10000, 10000);
            instance.drawTextWithShadow(textRenderer, "x: " + randomVal, i, j, k);
        } else {
            instance.drawTextWithShadow(textRenderer, s, i, j, k);
        }
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 3
            )
    )
    public void hudTweaks_renderDrawCoordinate_Y(InGameHud instance, TextRenderer textRenderer, String s, int i, int j, int k) {
        if (CoordinateDisplayEnum.HIDE == Config.config.coordinateDisplay) {
            instance.drawTextWithShadow(textRenderer, "", i, j, k);
        } else if (CoordinateDisplayEnum.RANDOMIZE == Config.config.coordinateDisplay) {
            double randomVal = random.nextDouble(-10000, 10000);
            instance.drawTextWithShadow(textRenderer, "y: " + randomVal, i, j, k);
        } else {
            instance.drawTextWithShadow(textRenderer, s, i, j, k);
        }
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 4
            )
    )
    public void hudTweaks_renderDrawCoordinate_Z(InGameHud instance, TextRenderer textRenderer, String s, int i, int j, int k) {
        if (CoordinateDisplayEnum.HIDE == Config.config.coordinateDisplay) {
            instance.drawTextWithShadow(textRenderer, "", i, j, k);
        } else if (CoordinateDisplayEnum.RANDOMIZE == Config.config.coordinateDisplay) {
            double randomVal = random.nextDouble(-10000, 10000);
            instance.drawTextWithShadow(textRenderer, "z: " + randomVal, i, j, k);
        } else {
            instance.drawTextWithShadow(textRenderer, s, i, j, k);
        }
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;get(I)Ljava/lang/Object;"
            )
    )
    public Object hudTweaks_renderChatOffset(List instance, int i) {
        if (Config.config.enableChatScroll) {
            return instance.get(i + chatOffset);
        } else {
            return instance.get(i);
        }
    }

    @ModifyConstant(
            method = "addChatMessage",
            constant = @Constant(intValue = 50)
    )
    public int chatLog_addChatMessageLimit(int value) {
        return Config.config.chatHistorySize;
    }
}
