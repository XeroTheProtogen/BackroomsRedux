package net.keno.backrooms_redux.worldgen.levels;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelPool {
    private final Identifier basePool;
    protected final HashMap<String, List<String>> subPools;
    public final boolean shouldOverride;

    public static final Codec<LevelPool> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("base_pool").stable().forGetter(LevelPool::getBasePool),
                    Codec.unboundedMap(Codec.STRING, Codec.list(Codec.STRING)).fieldOf("sub_pools").forGetter(LevelPool::getSubPools),
                    Codec.BOOL.optionalFieldOf("override", false)
                            .stable().forGetter(levelPool -> levelPool.shouldOverride)
            ).apply(instance, LevelPool::new));

    public LevelPool(Identifier basePool, Map<String, List<String>> subPools, boolean shouldOverride) {
        this.basePool = basePool;
        this.subPools = new HashMap<>(subPools);
        this.shouldOverride = shouldOverride;
    }

    public Identifier getBasePool() {
        return basePool;
    }

    public Map<String, List<String>> getSubPools() {
        return Map.copyOf(subPools);
    }

    public boolean hasSubPool(String subPool) {
        return subPools.containsKey(subPool);
    }

    public void addSubPool(String subPool, String... pieces) {
        if (!subPools.containsKey(subPool)) {
            subPools.put(subPool, List.of(pieces));
        }
    }

    public void addPiecesToSubPool(String subPool, String... pieces) {
        if (subPools.containsKey(subPool)) {
            List<String> filteredList = new ArrayList<>();
            for (String piece : pieces) {
                if (!subPools.get(subPool).contains(piece)) {
                    filteredList.add(piece);
                }
            }
            subPools.get(subPool).addAll(filteredList);
        }
    }

    public void removeSubPool(String subPool) {
        subPools.remove(subPool);
    }

    public void removePiecesFromSubPool(String subPool, String... pieces) {
        if (subPools.containsKey(subPool)) {
            List<String> filteredList = new ArrayList<>();
            for (String piece : pieces) {
                if (subPools.get(subPool).contains(piece)) {
                    filteredList.add(piece);
                }
            }
            subPools.get(subPool).removeAll(filteredList);
        }
    }

    public NbtGroup convertToGroup() {
        NbtGroup.Builder builder = NbtGroup.Builder
                .create(basePool);

        for (String subPool : subPools.keySet()) {
            builder = builder.with(subPool, subPools.get(subPool).toArray(String[]::new));
        }

        return builder.build();
    }
}
