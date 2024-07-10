package keno.backrooms_redux.worldgen.piece_pools;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class PoolArraysSingleton {
    private static PoolArraysSingleton INSTANCE;
    private Map<Identifier, PieceManager> pieceManagers = new HashMap<>();
    private PoolArraysSingleton() {}

    public void addManagerToPool(Identifier poolId, PieceManager manager) {
        if (this.getManager(poolId) != null) {
            PieceManager updatedPieceManager = getManager(poolId);
            for (Identifier id : manager.pools.keySet()) {
                updatedPieceManager.registerPiecesToPool(id, manager.getPool(id));
            }
            this.pieceManagers.put(poolId, updatedPieceManager);
        } else {
            this.pieceManagers.put(poolId, manager);
        }
    }

    public PieceManager getManager(Identifier poolId) {
        return this.pieceManagers.get(poolId);
    }

    public static synchronized PoolArraysSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PoolArraysSingleton();
        }
        return INSTANCE;
    }
}
