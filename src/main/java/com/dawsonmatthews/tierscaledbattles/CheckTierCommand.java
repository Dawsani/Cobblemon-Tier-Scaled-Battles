package com.dawsonmatthews.tierscaledbattles;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.arguments.StringArgumentType;

public class CheckTierCommand {

    private static String checkTier(String name) {
        name = name.toLowerCase();
        String capitalizedName = name.substring(0, 1).toUpperCase() + name.substring(1);
        String tier = TierScaledBattles.getSpeciesTier(name);
        String level = TierScaledBattles.getTierLevel(tier).toString();
        String returnString = "Unknown species name: " + name;
        if (tier != null) {
            returnString = capitalizedName + " is " + tier.toUpperCase() + " tier and will be set to level " + level + ".";
        }
        return returnString;
    }

    public static void registerCommand() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    Commands.literal("checktier")
                            .then(
                                    Commands.argument("pokemon name", StringArgumentType.word())
                                            .executes(ctx -> {
                                                String speciesName = StringArgumentType.getString(ctx, "pokemon name");
                                                ctx.getSource().sendSuccess(
                                                        () -> Component.literal(checkTier(speciesName)), false
                                                );
                                                return 1;
                                            })
                            )
            );
        });
    }

}
