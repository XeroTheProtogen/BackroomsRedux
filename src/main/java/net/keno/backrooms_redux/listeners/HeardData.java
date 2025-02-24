package net.keno.backrooms_redux.listeners;

import net.keno.backrooms_redux.worldgen.levels.LevelPool;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class HeardData {
    protected static Map<Identifier, LevelPool> LEVEL_POOLS = new HashMap<>();

    protected static void loadData(Map<Identifier, LevelPool> newPoolMap) {
        LEVEL_POOLS = newPoolMap;
    }

    public static LevelPool getLevelPool(Identifier id) {
        return LEVEL_POOLS.get(id);
    }
}
