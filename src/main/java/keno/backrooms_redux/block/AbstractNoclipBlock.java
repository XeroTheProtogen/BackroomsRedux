package keno.backrooms_redux.block;

import net.ludocrypt.limlib.api.LimlibTravelling;
import net.ludocrypt.limlib.api.world.LimlibHelper;
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
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Xoroshiro128PlusPlusRandom;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public abstract class AbstractNoclipBlock extends Block {
    protected final RegistryKey<World> targetWorld;
    protected final int yCord;
    public final int positionMultiplier;

    /**
     * <P>An Abstract class used to create noclip block subclasses</P>
     * @param settings Normal {@link net.minecraft.block.AbstractBlock.Settings}
     * @param targetWorld A pair containing the {@link RegistryKey} to a dimension, and the y cord to tp to
     * @param positionMultiplier Integer that is used to make a random int for multiplying the player cords before teleporting them
     */
    public AbstractNoclipBlock(Settings settings, Pair<RegistryKey<World>, Integer> targetWorld, int positionMultiplier) {
        super(settings);
        this.targetWorld = targetWorld.getLeft();
        this.yCord = targetWorld.getRight();
        this.positionMultiplier = positionMultiplier;
    }

    @SuppressWarnings({"deprecation"})
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                if (serverPlayer.getServer() != null) {
                    MinecraftServer server = serverPlayer.getServer();
                    if (server.getWorld(targetWorld) != null) {
                        ServerWorld level = server.getWorld(targetWorld);
                        assert level != null;
                        BlockPos starting_cords = player.getBlockPos();
                        Xoroshiro128PlusPlusRandom random = new Xoroshiro128PlusPlusRandom(LimlibHelper.blockSeed(pos) * (long) 3451.1321);

                        Vec3d resultant_cords = new Vec3d(starting_cords.getX() * random.nextBetween(1, this.positionMultiplier),
                                yCord, starting_cords.getZ() * random.nextBetween(1, this.positionMultiplier));
                        TeleportTarget target = new TeleportTarget(resultant_cords, serverPlayer.getVelocity(), serverPlayer.getYaw(), serverPlayer.getPitch());
                        LimlibTravelling.travelTo(serverPlayer, level, target, SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE, 0.8f, 1f);
                        return ActionResult.success(true);
                    }
                }
            }
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }
}
