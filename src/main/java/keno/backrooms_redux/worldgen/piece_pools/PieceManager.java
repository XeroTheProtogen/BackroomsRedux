package keno.backrooms_redux.worldgen.piece_pools;

import net.ludocrypt.limlib.api.world.NbtGroup;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class PieceManager {
    protected Map<Identifier, String[]> pools = new HashMap<>();
    private Map<Identifier, String> uniquePieces = new HashMap<>();
    private List<PieceProcessor<ChunkRegion, BlockPos, BlockState,
            Optional<NbtCompound>, Random>> pieceProcessors = new ArrayList<>();
    private final Identifier managerId;

    public PieceManager(Identifier managerId) {
        this.managerId = managerId;
    }

    public Identifier getManagerId() {
        return managerId;
    }

    public void registerPiecesToPool(Identifier poolId, String... pieces) {
        if (this.pools.get(poolId) == null) {
            if (pieces.length == 1) {
                registerPiece(poolId, pieces[0]);
            }
            else {
                this.pools.put(poolId, pieces);
            }
        } else {
            this.pools.put(poolId, ArrayUtils.addAll(this.pools.get(poolId), pieces));
        }
    }

    public void registerPiece(Identifier pieceId, String piece) {
        this.uniquePieces.putIfAbsent(pieceId, piece);
    }

    public void registerPieceProcessor(PieceProcessor<ChunkRegion, BlockPos, BlockState, Optional<NbtCompound>, Random> processor) {
        this.pieceProcessors.add(processor);
    }

    public String[] getPool(Identifier poolId) {
        if (pools.get(poolId) == null) {
            throw new InvalidIdentifierException("This pool does not exist at the id: " + poolId.toString());
        }
        return this.pools.get(poolId);
    }

    public NbtGroup getGroup() {
        NbtGroup.Builder builder = NbtGroup.Builder.create(getManagerId());
        if (this.pools.isEmpty()) throw new EmptyStackException();

        for (Identifier key : this.pools.keySet()) {
            builder.with(key.getPath(), this.getPool(key));
        }

        for (String piece : this.uniquePieces.values()) {
            builder.with(piece);
        }

        return builder.build();
    }

    public List<PieceProcessor<ChunkRegion, BlockPos, BlockState, Optional<NbtCompound>, Random>> getProcessors() {
        return this.pieceProcessors;
    }

    @FunctionalInterface
    public interface PieceProcessor<R extends ChunkRegion, P extends BlockPos,
            B extends BlockState, N extends Optional<NbtCompound>, D extends Random> {
        void applyProcessor(R chunkRegion, P blockPos,
                            B blockState, N optionalNbtCompound, D random);
    }
}
