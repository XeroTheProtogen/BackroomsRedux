package net.keno.backrooms_redux.listeners;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.worldgen.levels.LevelPool;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class LevelPoolListener implements SimpleResourceReloadListener<Map<Identifier, LevelPool>> {
    @Override
    public CompletableFuture<Map<Identifier, LevelPool>> load(ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            HashMap<Identifier, LevelPool> map = new HashMap<>();
            for (var resource : resourceManager.findResources("worldgen/level_pools", id
                    -> id.getPath().endsWith(".json")).entrySet()) {
                try (var inputStream = resource.getValue().getInputStream()) {
                    var json = BackroomsRedux.GSON.fromJson(new InputStreamReader(inputStream), JsonObject.class);
                    LevelPool pool = LevelPool.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();
                    if (map.containsKey(pool.getBasePool()) && !pool.shouldOverride) {
                        Identifier id = pool.getBasePool();
                        for (String subPool : pool.getSubPools().keySet()) {
                            if (map.get(id).hasSubPool(subPool)) {
                                map.get(id).addPiecesToSubPool(subPool, pool.getSubPools().get(subPool).toArray(String[]::new));
                            } else {
                                map.get(id).addSubPool(subPool, pool.getSubPools().get(subPool).toArray(String[]::new));
                            }
                        }
                    } else {
                        map.put(pool.getBasePool(), pool);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Map.copyOf(map);
        }, executor);
    }

    @Override
    public CompletableFuture<Void> apply(Map<Identifier, LevelPool> levelPool, ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.runAsync(() -> HeardData.loadData(levelPool), executor);
    }

    @Override
    public Identifier getFabricId() {
        return BackroomsRedux.modLoc("level_pool_listener");
    }
}
