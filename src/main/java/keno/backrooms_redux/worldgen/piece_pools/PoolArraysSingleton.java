package keno.backrooms_redux.worldgen.piece_pools;

import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

public class PoolArraysSingleton {
    private static PoolArraysSingleton INSTANCE;
    private Map<Identifier, String[]> POOLS = new HashMap<>();
    private PoolArraysSingleton() {}

    public void addPiecesToPool(Identifier poolId, String... pieces) {
        if (this.POOLS.get(poolId) == null) {
            this.POOLS.put(poolId, pieces);
        } else {
            this.POOLS.put(poolId, ArrayUtils.addAll(this.POOLS.get(poolId), pieces));
        }
    }

    public String[] getPieces(Identifier poolId) {
        return this.POOLS.get(poolId);
    }

    public static synchronized PoolArraysSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PoolArraysSingleton();
        }
        return INSTANCE;
    }
}
