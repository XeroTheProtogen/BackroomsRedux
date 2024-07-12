package keno.backrooms_redux.mixin.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.spawn_check.EnteredBackroomsComponent;
import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.registry.BRRegistrar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.SERVER)
@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin implements PlayerManagerAccessor {

    @ModifyExpressionValue(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    public ServerWorld backrooms_redux$respawnPlayer(ServerWorld original, @Local(argsOnly = true) ServerPlayerEntity player) {
        if (!player.isCreative() && !player.isSpectator()) {
            EnteredBackroomsComponent component = BRComponentRegistry.ENTERED_BACKROOMS.get(player);
            MinecraftServer server = ((PlayerManagerAccessor) this).getServer();
            if (server.getGameRules().getBoolean(BRCommonRegistry.RESPAWN_IN_BACKROOMS) && component.getBool()) {
                if (server.getWorld(BRRegistrar.LEVEL_0_WORLD) != null) {
                    return server.getWorld(BRRegistrar.LEVEL_0_WORLD);
                }
            }
        }
        return original;
    }
}
