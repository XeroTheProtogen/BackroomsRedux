package net.keno.backrooms_redux.data.holders.pos_determ;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.keno.backrooms_redux.registry.BRCommon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class NoPosDeterminer implements PosDeterminer {
    public static final MapCodec<NoPosDeterminer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.stable(new NoPosDeterminer()));

    private BlockPos pos;

    @Override
    public void processPos(ServerPlayerEntity player) {
        pos = player.getBlockPos();
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public DeterminerType<NoPosDeterminer> getType() {
        return BRCommon.NO_POS;
    }
}
