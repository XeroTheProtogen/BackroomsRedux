package net.keno.backrooms_redux.data.listeners;

import net.keno.backrooms_redux.data.holders.noclip.NoClipPool;
import net.keno.backrooms_redux.worldgen.levels.LevelPool;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class HeardData {
    private static Map<Identifier, LevelPool> LEVEL_POOLS = new HashMap<>();
    private static Map<Identifier, NoClipPool> NO_CLIP_POOLS = new HashMap<>();

    protected static void loadLevelPoolData(Map<Identifier, LevelPool> newPoolMap) {
        LEVEL_POOLS = newPoolMap;
    }

    protected static void loadNoclipPoolData(Map<Identifier, NoClipPool> newPoolMap) {
        NO_CLIP_POOLS = newPoolMap;
    }

    public static NoClipPool getNoclipPool(Identifier id) {
        return NO_CLIP_POOLS.get(id);
    }

    public static LevelPool getLevelPool(Identifier id) {
        return LEVEL_POOLS.get(id);
    }
}
