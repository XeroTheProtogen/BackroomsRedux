package net.keno.backrooms_redux.data.holders.pos_determ;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.keno.backrooms_redux.registry.BRCommon;
import net.minecraft.util.math.BlockPos;

public class ExactPosDeterminer implements PosDeterminer {
    public static final MapCodec<ExactPosDeterminer> CODEC = RecordCodecBuilder
            .mapCodec(instance -> instance.group(
                    BlockPos.CODEC.stable().fieldOf("pos").forGetter(ExactPosDeterminer::getPos)
            ).apply(instance, instance.stable(ExactPosDeterminer::new)));

    private final BlockPos pos;

    public ExactPosDeterminer(BlockPos pos) {
        this.pos = pos;
    }

    public ExactPosDeterminer(int x, int y, int z) {
        this(new BlockPos(x, y, z));
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public DeterminerType<ExactPosDeterminer> getType() {
        return BRCommon.EXACT_POS;
    }
}
