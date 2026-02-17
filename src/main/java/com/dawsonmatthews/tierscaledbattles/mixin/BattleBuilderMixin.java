package com.dawsonmatthews.tierscaledbattles.mixin;

import com.cobblemon.mod.common.battles.BattleBuilder;
import com.cobblemon.mod.common.pokemon.Pokemon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BattleBuilder.class)
public abstract class BattleBuilderMixin {
    @Redirect(
        method = {"pvp1v1", "pvp2v2"},
        at = @At(
            value = "INVOKE",
            target = "Lcom/cobblemon/mod/common/pokemon/Pokemon;setLevel(I)V"
        )
    )
    private void redirectSetLevel(Pokemon pokemon, int adjustLevel) {
        if (adjustLevel == 2165) {
            pokemon.setLevel(58);
        } else {
            pokemon.setLevel(adjustLevel);
        }
    }

}
