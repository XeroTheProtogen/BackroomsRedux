package net.keno.backrooms_redux.data.holders.pos_determ;

import com.mojang.serialization.Codec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public interface PosDeterminer {
    Codec<PosDeterminer> DETERMINER_CODEC = DeterminerType.REGISTRY.getCodec()
            .dispatch("type", PosDeterminer::getType, DeterminerType::codec);

    default void processPos(ServerPlayerEntity player) {}

    BlockPos getPos();

    DeterminerType<?> getType();
}
