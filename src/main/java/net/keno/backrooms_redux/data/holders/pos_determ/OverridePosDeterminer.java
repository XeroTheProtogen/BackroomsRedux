package net.keno.backrooms_redux.data.holders.pos_determ;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.keno.backrooms_redux.registry.BRCommon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class OverridePosDeterminer implements PosDeterminer {
    public static final MapCodec<OverridePosDeterminer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
       Codec.INT.stable().optionalFieldOf("x").forGetter(OverridePosDeterminer::getX),
       Codec.INT.stable().optionalFieldOf("y").forGetter(OverridePosDeterminer::getY),
       Codec.INT.stable().optionalFieldOf("z").forGetter(OverridePosDeterminer::getZ)
    ).apply(instance, instance.stable(OverridePosDeterminer::new)));

    private final Optional<Integer> x;
    private final Optional<Integer> y;
    private final Optional<Integer> z;
    private BlockPos resultPos;

    public OverridePosDeterminer(Optional<Integer> x, Optional<Integer> y, Optional<Integer> z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void processPos(ServerPlayerEntity player) {
        BlockPos playerPos = player.getBlockPos();
        boolean overrideX = x.isPresent();
        boolean overrideY = y.isPresent();
        boolean overrideZ = z.isPresent();

        resultPos = new BlockPos(overrideX ? x.get() : playerPos.getX(),
                overrideY ? y.get() : playerPos.getY(),
                overrideZ ? z.get() : playerPos.getZ());
    }

    @Override
    public BlockPos getPos() {
        return resultPos;
    }

    @Override
    public DeterminerType<?> getType() {
        return BRCommon.OVERRIDE_POS;
    }

    public Optional<Integer> getX() {
        return x;
    }

    public Optional<Integer> getY() {
        return y;
    }

    public Optional<Integer> getZ() {
        return z;
    }
}
