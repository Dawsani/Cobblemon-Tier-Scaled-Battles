package com.dawsonmatthews.tierscaledbattles;

import com.dawsonmatthews.tierscaledbattles.mixin.BattleBuilderMixin;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TierScaledBattles implements ModInitializer {
    public static final String MOD_ID = "tierscaledbattles";
    public static final Logger LOGGER = LoggerFactory.getLogger("tierscaledbattles");

    private static CheckTierCommand checkTierCommand;

    private static final String POKEMON_TIERS_PATH = "assets/tierscaledbattles/pokemon-tiers.json";
    private static final String TIER_LEVELS_PATH = "assets/tierscaledbattles/tier-levels.json";

    private static final Integer TIER_SCALED_LEVEL_RULESET_OPTION_ID = 2165;

    private static Map<String, Integer> speciesLevel;

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        LOGGER.info("Loaded Tier Scaled Battles mod.");

        speciesLevel = loadPokemonLevelsFromJsonResources();
        CheckTierCommand.registerCommand();
    }

    public static Integer getTierScaledLevelRulesetOptionID() {
        return TIER_SCALED_LEVEL_RULESET_OPTION_ID;
    }

    public static Integer getSpeciesLevel(String speciesName) {
        return speciesLevel.getOrDefault(speciesName, 1);
    }

    private static Map<String, Integer> loadPokemonLevelsFromJsonResources() {

        Gson gson = new Gson();

        // tierName -> level
        Map<String, Integer> tierToLevel = new HashMap<>();

        // pokemonName -> natDexTier
        Map<String, String> pokemonToTier = new HashMap<>();

        // ---------- Load tier levels ----------
        try (InputStream in = TierScaledBattles.class.getClassLoader().getResourceAsStream(TIER_LEVELS_PATH)) {
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
        try (InputStream in = TierScaledBattles.class.getClassLoader().getResourceAsStream(POKEMON_TIERS_PATH)) {
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
}