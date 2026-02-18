package com.dawsonmatthews.tierscaledbattles.mixin.client;

import com.cobblemon.mod.common.client.gui.interact.battleRequest.BattleConfigureGUI;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At;

import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

@Mixin(BattleConfigureGUI.class)
public abstract class BattleConfigureGUIMixin {

    @Unique
    private static final Integer TIER_SCALED_LEVEL_RULESET_OPTION_ID = 2165;

    @Final
    @Shadow(remap = false)
    private static List<Integer> levelRulesetOption;

    @Shadow
    private int levelRulesetOptionIndex;

    @Inject(method = "init", at = @At("RETURN"), remap = false)
    private void addTierScaledLevelRulesetOption(CallbackInfo ci) {
        if (levelRulesetOption.contains(TIER_SCALED_LEVEL_RULESET_OPTION_ID)) {
            return;
        }
        levelRulesetOption.add(TIER_SCALED_LEVEL_RULESET_OPTION_ID);
    }


    @Redirect(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/util/LocalizationUtilsKt;lang(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/network/chat/MutableComponent;"
            )
    )
    private MutableComponent tierScaledBattlesRedirectLang(String key, Object[] args) {

        if ("challenge.rule.level".equals(key)) {

            int adjustLevel = -1;
            if (args != null && args.length > 0 && args[0] instanceof Integer) {
                adjustLevel = (Integer) args[0];
            }

            if (adjustLevel == TIER_SCALED_LEVEL_RULESET_OPTION_ID) {
                return Component.translatable("challenge.rule.tier_scaled");
            }
        }

        return com.cobblemon.mod.common.util.LocalizationUtilsKt.lang(key, args);
    }
}