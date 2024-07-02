package keno.backrooms_redux.block;

import net.ludocrypt.limlib.api.LimlibTravelling;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class GlitchingBlock extends Block {
    protected final RegistryKey<World> targetWorld;

    public GlitchingBlock(Settings settings, RegistryKey<World> targetWorld) {
        super(settings);
        this.targetWorld = targetWorld;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                if (serverPlayer.getServer() != null) {
                    MinecraftServer server = serverPlayer.getServer();
                    if (server.getWorld(targetWorld) != null) {
                        ServerWorld level = server.getWorld(targetWorld);
                        BlockPos starting_cords = player.getBlockPos();
                        Vec3d resultant_cords = new Vec3d(starting_cords.getX(), 2, starting_cords.getZ());
                        TeleportTarget target = new TeleportTarget(resultant_cords, serverPlayer.getVelocity(), serverPlayer.getYaw(), serverPlayer.getPitch());
                        LimlibTravelling.travelTo(serverPlayer, level, target, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, 0.8f, 1f);
                        return super.onUse(state, world, pos, player, hand, hit);
                    }
                }
            }
        }

        return ActionResult.success(true);
    }
}
