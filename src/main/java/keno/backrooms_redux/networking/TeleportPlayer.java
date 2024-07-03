package keno.backrooms_redux.networking;

import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.registry.BRRegistrar;
import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class TeleportPlayer {
    public static void teleportPlayerToBackrooms(MinecraftServer server, ServerPlayerEntity player) {
        BlockPos playerPos = player.getBlockPos();
        Vec3d backroomsPos = new Vec3d(playerPos.getX(), 2, playerPos.getZ());
        ServerWorld backrooms = server.getWorld(BRRegistrar.LEVEL_0_WORLD);
        TeleportTarget target = new TeleportTarget(backroomsPos, player.getVelocity(), player.getYaw(), player.getPitch());
        LimlibTravelling.travelTo(player, backrooms, target, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, 0.8f, 0.9f);
        if (server.getGameRules().getBoolean(BRCommonRegistry.RESPAWN_IN_BACKROOMS)) {
            BlockPos newSpawn = new BlockPos(Math.round((float) backroomsPos.getX()),
                    Math.round((float) backroomsPos.getY()), Math.round((float) backroomsPos.getZ()));
            player.setSpawnPoint(BRRegistrar.LEVEL_0_WORLD, newSpawn, player.getSpawnAngle(), true, false);
        }
        if (server.getGameRules().getBoolean(BRCommonRegistry.CLEAR_INVENTORY_ON_BACKROOMS_ENTRY)) {
            player.getInventory().clear();
            player.getEnderChestInventory().clear();
        }
    }
}
