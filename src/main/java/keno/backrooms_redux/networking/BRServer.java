package keno.backrooms_redux.networking;

import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.registry.BRRegistrar;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class BRServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerPlayNetworking.registerGlobalReceiver(BRPackets.TELEPORT_PLAYER_TO_BACKROOMS, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();

            server.execute(() -> {
                Vec3d backrooms_pos = new Vec3d(pos.getX(), 2, pos.getZ());
                if (server.getWorld(BRRegistrar.LEVEL_0_WORLD) != null) {
                    ServerWorld backrooms = server.getWorld(BRRegistrar.LEVEL_0_WORLD);
                    assert backrooms != null;
                    TeleportTarget target = new TeleportTarget(backrooms_pos, player.getVelocity(), player.getYaw(), player.getPitch());
                    LimlibTravelling.travelTo(player, backrooms, target, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, 0.8f, 0.9f);
                    if (server.getGameRules().getBoolean(BRCommonRegistry.RESPAWN_IN_BACKROOMS)) {
                        BlockPos new_spawn = new BlockPos(Math.round((float) backrooms_pos.getX()),
                                Math.round((float) backrooms_pos.getY()), Math.round((float) backrooms_pos.getZ()));
                        player.setSpawnPoint(BRRegistrar.LEVEL_0_WORLD, new_spawn, player.getSpawnAngle(), true, false);
                    }
                }
            });
        });
    }
}
