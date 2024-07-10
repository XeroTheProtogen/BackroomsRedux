package keno.backrooms_redux.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class BRCommands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(BRCommands::sanityCommand);
    }

    private static void sanityCommand(CommandDispatcher<ServerCommandSource> dispatcher,
                                      CommandRegistryAccess access, CommandManager.RegistrationEnvironment environment) {
        var root = CommandManager.literal("setSanity").build();
        var sanity = CommandManager.argument("sanity", FloatArgumentType.floatArg(0f, 100f))
                .executes(SetSanityCommand::setSelfSanity).build();

        dispatcher.getRoot().addChild(root);
        root.addChild(sanity);
    }
}
