package com.dawsonmatthews.tierscaledbattles.mixin;

import com.cobblemon.mod.common.battles.BattleBuilder;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.dawsonmatthews.tierscaledbattles.TierScaledBattles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
        if (adjustLevel == TierScaledBattles.getTierScaledLevelRulesetOptionID()) {
            String speciesName = pokemon.getSpecies().getName().toLowerCase();
            String formName = pokemon.getForm().getName().toLowerCase();
            String heldItemName = pokemon.getHeldItem$common().getItem().toString().toLowerCase();

            speciesName = speciesName.replace("-", "");

            if (!formName.equals("normal")) {
                speciesName = speciesName + formName;
            }

            String[] parts = heldItemName.split(":");
            String namespace = parts[0];
            String itemName = parts[1];
            if ((namespace.equals("mega_showdown") || namespace.equals("zamega")) && itemName.contains("ite")) {
                speciesName = speciesName + "mega";

                char lastChar = itemName.charAt(itemName.length() -1);
                if (lastChar == 'x') {
                    speciesName = speciesName + "x";
                } else if (lastChar == 'y') {
                    speciesName = speciesName + "y";
                } else if (lastChar == 'z') {
                    speciesName = speciesName + "z";
                }
            }

            TierScaledBattles.LOGGER.info("Species Name: {}", speciesName);

            pokemon.setLevel(TierScaledBattles.getSpeciesLevel(speciesName));
        } else {
            pokemon.setLevel(adjustLevel);
        }
    }
}
