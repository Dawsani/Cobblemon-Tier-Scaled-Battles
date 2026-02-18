package com.dawsonmatthews.tierscaledbattles;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import com.mojang.brigadier.arguments.StringArgumentType;

public class CheckTierCommand {

    private static String checkTier(String name) {
        return TierScaledBattles.getSpeciesLevel(name).toString();
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
