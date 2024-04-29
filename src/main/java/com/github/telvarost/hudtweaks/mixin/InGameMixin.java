package com.github.telvarost.hudtweaks.mixin;

import com.github.telvarost.hudtweaks.Config;
import com.github.telvarost.hudtweaks.CoordinateDisplayEnum;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.InGame;
import net.minecraft.client.gui.screen.ingame.Chat;
import net.minecraft.client.render.TextRenderer;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(InGame.class)
@Environment(value= EnvType.CLIENT)
public abstract class InGameMixin extends DrawableHelper {

    @Shadow private Minecraft minecraft;

    @Shadow private List chatMessages;

    @Shadow private Random rand;
    @Unique private Integer numberOfTurns = 0;

    @Unique private Integer chatOffset = 0;

    @Redirect(
            method = "renderHud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/InGame;blit(IIIIII)V",
                    ordinal = 0
            )
    )
    private void hudTweaks_renderHudHotbarPosition(InGame instance, int i, int j, int k, int l, int m, int n) {
        instance.blit(i, j - Config.config.hotbarYPositionOffset, k, l, m, n);
    }

    @Redirect(
            method = "renderHud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/InGame;blit(IIIIII)V",
                    ordinal = 1
            )
    )
    private void hudTweaks_renderHudSelectedItemBorderPosition(InGame instance, int i, int j, int k, int l, int m, int n) {
        instance.blit(i, j - Config.config.hotbarYPositionOffset, k, l, m, n);
    }

    @ModifyConstant(
            method = "renderHud",
            constant = @Constant(intValue = 32, ordinal = 0)
    )
    private int hudTweaks_renderHudStatusBarPositions0(int value) {
        return value + Config.config.hotbarYPositionOffset;
    }

    @ModifyConstant(
            method = "renderHud",
            constant = @Constant(intValue = 32, ordinal = 1)
    )
    private int hudTweaks_renderHudStatusBarPositions1(int value) {
        return value + Config.config.hotbarYPositionOffset;
    }

    @ModifyConstant(
            method = "renderHud",
            constant = @Constant(intValue = 32, ordinal = 2)
    )
    private int hudTweaks_renderHudStatusBarPositions2(int value) {
        return value + Config.config.hotbarYPositionOffset;
    }

    @ModifyConstant(
            method = "renderHud",
            constant = @Constant(intValue = 16, ordinal = 5)
    )
    private int hudTweaks_renderHudItemPositions(int value) {
        return value + Config.config.hotbarYPositionOffset;
    }

    @ModifyConstant(
            method = "renderHud",
            constant = @Constant(intValue = 200)
    )
    private int hudTweaks_renderHudChatFadeTime(int value) {
        return (Config.config.chatFadeTime * 2);
    }

    @ModifyConstant(
            method = "renderHud",
            constant = @Constant(doubleValue = 200.0)
    )
    private double hudTweaks_renderHudChatFadeTimeDivisor(double value) {
        return (Config.config.chatFadeTime * 2);
    }

    @Inject(method = "renderHud", at = @At("HEAD"), cancellable = true)
    public void hudTweaks_renderHudChatScroll(float f, boolean bl, int i, int j, CallbackInfo ci) {
        if (!Config.config.enableChatScroll) {
            return;
        }

        int chatRangeTop = 20;

        if (this.minecraft.currentScreen instanceof Chat) {
            int currentWheelDegrees = Mouse.getDWheel();
            numberOfTurns = Math.round((float)currentWheelDegrees / 120.0f);
            chatOffset = chatOffset + numberOfTurns;

            if (chatOffset < 0) {
                chatOffset = 0;
            }

            if (chatRangeTop < this.chatMessages.size()) {
                if (this.chatMessages.size() <= (chatRangeTop + chatOffset)) {
                    chatOffset = this.chatMessages.size() - chatRangeTop;
                }
            } else {
                chatOffset = 0;
            }
        } else {
            chatOffset = 0;
        }
    }

    @Redirect(
            method = "renderHud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/InGame;blit(IIIIII)V",
                    ordinal = 2
            )
    )
    public void hudTweaks_renderCursor(InGame instance, int i, int j, int k, int l, int m, int n) {
        if (!Config.config.disableCrosshair) {
            instance.blit(i, j, k, l, m, n);
        }
    }

    @Redirect(
            method = "renderHud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/InGame;drawTextWithShadow(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 2
            )
    )
    public void hudTweaks_renderDrawCoordinate_X(InGame instance, TextRenderer textRenderer, String s, int i, int j, int k) {
        if (CoordinateDisplayEnum.HIDE == Config.config.coordinateDisplay) {
            instance.drawTextWithShadow(textRenderer, "", i, j, k);
        } else if (CoordinateDisplayEnum.RANDOMIZE == Config.config.coordinateDisplay) {
            double randVal = rand.nextDouble(-10000, 10000);
            instance.drawTextWithShadow(textRenderer, "x: " + randVal, i, j, k);
        } else {
            instance.drawTextWithShadow(textRenderer, s, i, j, k);
        }
    }

    @Redirect(
            method = "renderHud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/InGame;drawTextWithShadow(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 3
            )
    )
    public void hudTweaks_renderDrawCoordinate_Y(InGame instance, TextRenderer textRenderer, String s, int i, int j, int k) {
        if (CoordinateDisplayEnum.HIDE == Config.config.coordinateDisplay) {
            instance.drawTextWithShadow(textRenderer, "", i, j, k);
        } else if (CoordinateDisplayEnum.RANDOMIZE == Config.config.coordinateDisplay) {
            double randVal = rand.nextDouble(-10000, 10000);
            instance.drawTextWithShadow(textRenderer, "y: " + randVal, i, j, k);
        } else {
            instance.drawTextWithShadow(textRenderer, s, i, j, k);
        }
    }

    @Redirect(
            method = "renderHud",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/InGame;drawTextWithShadow(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V",
                    ordinal = 4
            )
    )
    public void hudTweaks_renderDrawCoordinate_Z(InGame instance, TextRenderer textRenderer, String s, int i, int j, int k) {
        if (CoordinateDisplayEnum.HIDE == Config.config.coordinateDisplay) {
            instance.drawTextWithShadow(textRenderer, "", i, j, k);
        } else if (CoordinateDisplayEnum.RANDOMIZE == Config.config.coordinateDisplay) {
            double randVal = rand.nextDouble(-10000, 10000);
            instance.drawTextWithShadow(textRenderer, "z: " + randVal, i, j, k);
        } else {
            instance.drawTextWithShadow(textRenderer, s, i, j, k);
        }
    }

    @Redirect(
            method = "renderHud",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;get(I)Ljava/lang/Object;"
            )
    )
    public Object hudTweaks_renderHudChatOffset(List instance, int i) {
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
