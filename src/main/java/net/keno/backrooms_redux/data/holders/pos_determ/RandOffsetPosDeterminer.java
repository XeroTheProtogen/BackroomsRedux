package net.keno.backrooms_redux.data.holders.pos_determ;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.keno.backrooms_redux.registry.BRCommon;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class RandOffsetPosDeterminer implements PosDeterminer {
    public static final MapCodec<RandOffsetPosDeterminer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        BlockPos.CODEC.stable().fieldOf("min_pos").forGetter(RandOffsetPosDeterminer::getMinPos),
        BlockPos.CODEC.stable().fieldOf("max_pos").forGetter(RandOffsetPosDeterminer::getMaxPos),
            Codec.BOOL.stable().fieldOf("offset_y").forGetter(RandOffsetPosDeterminer::shouldOffsetY)
    ).apply(instance, instance.stable(RandOffsetPosDeterminer::new)));

    private final BlockPos minPos;
    private final BlockPos maxPos;
    private final boolean offsetY;

    private BlockPos manipulatedPos;

    public RandOffsetPosDeterminer(BlockPos minPos, BlockPos maxPos, boolean offsetY) {
        this.minPos = minPos;
        this.maxPos = maxPos;
        this.offsetY = offsetY;
    }

    public BlockPos getMinPos() {
        return minPos;
    }

    public BlockPos getMaxPos() {
        return maxPos;
    }

    public boolean shouldOffsetY() {
        return offsetY;
    }

    @Override
    public void processPos(ServerPlayerEntity player) {
        Random random = Random.create(LimlibHelper.blockSeed(player.getBlockPos()));
        int x = random.nextBetween(minPos.getX(), maxPos.getX());
        int y = offsetY ? random.nextBetween(minPos.getY(), maxPos.getY()) : 0;
        int z = random.nextBetween(minPos.getZ(), maxPos.getZ());

        this.manipulatedPos = player.getBlockPos();
        manipulatedPos.add(x, y, z);
    }

    @Override
    public BlockPos getPos() {
        return manipulatedPos;
    }

    @Override
    public DeterminerType<?> getType() {
        return BRCommon.RAND_OFFSET_POS;
    }
}
