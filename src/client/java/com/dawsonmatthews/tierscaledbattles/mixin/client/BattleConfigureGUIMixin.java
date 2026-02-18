package com.dawsonmatthews.tierscaledbattles.mixin.client;

import com.cobblemon.mod.common.client.gui.interact.battleRequest.BattleConfigureGUI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BattleConfigureGUI.class)
public abstract class BattleConfigureGUIMixin {

    private static final Integer TIER_SCALED_LEVEL_RULESET_OPTION_ID = 2165;

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
}