package keno.backrooms_redux.worldgen.piece_pools;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.util.math.random.Random;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PieceManager {
    protected Map<Identifier, String[]> pools = new HashMap<>();
    protected Map<Identifier, PieceProcessor<ChunkRegion, BlockPos, BlockState, Optional<NbtCompound>, Random> processors = new HashMap<>();

    public void registerPiecesToPool(Identifier poolId, String... pieces) {
        if (this.pools.get(poolId) == null) {
            this.pools.put(poolId, pieces);
        } else {
            this.pools.put(poolId, ArrayUtils.addAll(this.pools.get(poolId), pieces));
        }
    }

    public void registerPieceProcessor(Identifier processorId,
                                       PieceProcessor<ChunkRegion, BlockPos, BlockState, Optional<NbtCompound>, Random> processor) {
        if (this.processors.get(processorId) != null) {
            throw new KeyAlreadyExistsException("There is already a processor here at " + processorId.toString());
        } else {
            this.processors.put(processorId, processor);
        }
    }

    public String[] getPool(Identifier poolId) {
        if (pools.get(poolId) == null) {
            throw new InvalidIdentifierException("This pool does not exist at the id: " + poolId.toString());
        }
        return this.pools.get(poolId);
    }

    public PieceProcessor<ChunkRegion, BlockPos, BlockState, Optional<NbtCompound>, Random> getProcessor(Identifier processorId) {
        if (pools.get(processorId) == null) {
            throw new InvalidIdentifierException("This processor does not exist at the id: " + processorId.toString());
        }
        return this.processors.get(processorId);
    }

    @FunctionalInterface
    public interface PieceProcessor<R extends ChunkRegion, P extends BlockPos,
            B extends BlockState, N extends Optional<NbtCompound>, D extends Random> {
        void applyProcessor(R chunkRegion, P blockPos,
                            B blockState, N optionalNbtCompound, D random);
    }
}
