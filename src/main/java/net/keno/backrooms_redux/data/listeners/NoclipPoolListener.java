package net.keno.backrooms_redux.data.listeners;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.data.holders.noclip.NoClipPool;
import net.keno.backrooms_redux.utils.WeightedIdentifierList;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class NoclipPoolListener implements SimpleResourceReloadListener<Map<Identifier, NoClipPool>> {
    @Override
    public CompletableFuture<Map<Identifier, NoClipPool>> load(ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            HashMap<Identifier, NoClipPool> map = new HashMap<>();

            for (var resource : resourceManager.findResources("noclip_pools",
                    id -> id.getPath().endsWith(".json")).entrySet()) {
                try (var inputStream = resource.getValue().getInputStream()) {
                    var json = BackroomsRedux.GSON.fromJson(new InputStreamReader(inputStream), JsonObject.class);

                    NoClipPool pool = NoClipPool.CODEC.parse(JsonOps.INSTANCE, json).getOrThrow();
                    Identifier id = pool.poolId();
                    if (map.containsKey(id)) {
                        if (pool.override()) {
                            map.put(id, pool);
                        } else {
                            WeightedIdentifierList list = pool.list();
                            HashMap<Identifier, Float> listData = new HashMap<>();
                            for (Pair<Identifier, Float> pair : list.idsAndWeights()) {
                                listData.put(pair.getFirst(), pair.getSecond());
                            }
                            map.get(id).addIdsAndWeights(listData);
                        }
                    } else {
                        map.put(id, pool);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return Map.copyOf(map);
        }, executor);
    }

    @Override
    public CompletableFuture<Void> apply(Map<Identifier, NoClipPool> noClipPoolMap, ResourceManager resourceManager, Executor executor) {
        return CompletableFuture.runAsync(() -> HeardData.loadNoclipPoolData(noClipPoolMap), executor);
    }

    @Override
    public Identifier getFabricId() {
        return BackroomsRedux.modLoc("noclip_pool_loader");
    }
}
