package keno.backrooms_redux.commands;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.sanity.SanityComponent;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class SetSanityCommand {
    protected static int setSelfSanity(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        float sanity = FloatArgumentType.getFloat(context, "sanity");
        if (source.getEntity() instanceof ServerPlayerEntity player) {
            if (BRComponentRegistry.SANITY.isProvidedBy(player)) {
                SanityComponent component = BRComponentRegistry.SANITY.get(player);
                component.setValue(sanity);
                BRComponentRegistry.SANITY.sync(player);
            }
        }
        return 1;
    }

    protected static int setPlayerSanity(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        float sanity = FloatArgumentType.getFloat(context, "sanity");
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "target");
        if (BRComponentRegistry.SANITY.isProvidedBy(player)) {
            SanityComponent component = BRComponentRegistry.SANITY.get(player);
            component.setValue(sanity);
            BRComponentRegistry.SANITY.sync(player);
        }
        return 1;
    }
}
