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

    private static final String POKEMON_TIERS_PATH = "assets/tierscaledbattles/pokemon-tiers.json";
    private static final String TIER_LEVELS_PATH = "assets/tierscaledbattles/tier-levels.json";

    private static Map<String, Integer> speciesLevel = loadPokemonLevelsFromJsonResources();

    private static Map<String, Integer> loadPokemonLevelsFromJsonResources() {

        Gson gson = new Gson();

        // tierName -> level
        Map<String, Integer> tierToLevel = new HashMap<>();

        // pokemonName -> natDexTier
        Map<String, String> pokemonToTier = new HashMap<>();

        // ---------- Load tier levels ----------
        try (InputStream in = BattleBuilderMixin.class.getClassLoader().getResourceAsStream(TIER_LEVELS_PATH)) {
            if (in == null) {
                throw new RuntimeException("Missing resource: " + TIER_LEVELS_PATH);
            }

            JsonObject tierLevelsJson = gson.fromJson(
                    new InputStreamReader(in, StandardCharsets.UTF_8),
                    JsonObject.class
            );

            for (Map.Entry<String, JsonElement> e : tierLevelsJson.entrySet()) {
                String tier = e.getKey().trim().toLowerCase();
                int level = e.getValue().getAsInt();
                tierToLevel.put(tier, level);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed reading tier levels JSON: " + TIER_LEVELS_PATH, e);
        }

        // ---------- Load pokemon tiers ----------
        try (InputStream in = BattleBuilderMixin.class.getClassLoader().getResourceAsStream(POKEMON_TIERS_PATH)) {
            if (in == null) {
                throw new RuntimeException("Missing resource: " + POKEMON_TIERS_PATH);
            }

            JsonObject pokemonTiersJson = gson.fromJson(
                    new InputStreamReader(in, StandardCharsets.UTF_8),
                    JsonObject.class
            );

            for (Map.Entry<String, JsonElement> e : pokemonTiersJson.entrySet()) {
                String pokemon = e.getKey().trim().toLowerCase();

                JsonObject obj = e.getValue().getAsJsonObject();
                if (!obj.has("natDexTier")) continue;

                String tier = obj.get("natDexTier").getAsString().trim().toLowerCase();
                if (tier.isEmpty()) continue;

                pokemonToTier.put(pokemon, tier);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed reading pokemon tiers JSON: " + POKEMON_TIERS_PATH, e);
        }

        // ---------- Join into pokemon -> level ----------
        Map<String, Integer> pokemonToLevel = new HashMap<>();

        for (Map.Entry<String, String> entry : pokemonToTier.entrySet()) {
            String pokemon = entry.getKey();
            String tier = entry.getValue();

            Integer level = tierToLevel.get(tier);

            if (level == null) {
                // Tier not found in tier levels file — skip or default
                continue;
            }

            pokemonToLevel.put(pokemon, level);
        }

        return pokemonToLevel;
    }


    @Redirect(
        method = {"pvp1v1", "pvp2v2"},
        at = @At(
            value = "INVOKE",
            target = "Lcom/cobblemon/mod/common/pokemon/Pokemon;setLevel(I)V"
        )
    )
    private void redirectSetLevel(Pokemon pokemon, int adjustLevel) {
        if (adjustLevel == 2165) {
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
            TierScaledBattles.LOGGER.info("Form Name: {}", formName);
            TierScaledBattles.LOGGER.info("Held Item Name: {}", heldItemName);

            pokemon.setLevel(speciesLevel.getOrDefault(speciesName, 1));
        } else {
            pokemon.setLevel(adjustLevel);
        }
    }
}
