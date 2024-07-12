package keno.backrooms_redux.networking;

import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.registry.BRRegistrar;
import net.ludocrypt.limlib.api.LimlibTravelling;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;
import net.minecraft.world.TeleportTarget;

public class TeleportPlayer {
    public static void teleportPlayerToBackrooms(MinecraftServer server, ServerPlayerEntity player) {
        BlockPos playerPos = player.getBlockPos();
        Random random = new Xoroshiro128PlusPlusRandom( (long) (41313.231 * 3) * LimlibHelper.blockSeed(playerPos));
        Vec3d backroomsPos = new Vec3d(playerPos.getX() * random.nextBetween(1, 5), 2,
                playerPos.getZ() * random.nextBetween(1, 5));
        ServerWorld backrooms = server.getWorld(BRRegistrar.LEVEL_0_WORLD);
        TeleportTarget target = new TeleportTarget(backroomsPos, player.getVelocity(), player.getYaw(), player.getPitch());
        LimlibTravelling.travelTo(player, backrooms, target, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, 0.8f, 0.9f);
        if (server.getGameRules().getBoolean(BRCommonRegistry.CLEAR_INVENTORY_ON_BACKROOMS_ENTRY)) {
            player.getInventory().clear();
            player.getEnderChestInventory().clear();
        }
    }
}
