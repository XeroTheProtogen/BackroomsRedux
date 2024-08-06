package keno.backrooms_redux.server.events;

import com.mojang.datafixers.util.Pair;
import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.mixin.StructurePoolAccessor;
import keno.backrooms_redux.worldgen.processors.BRProcessorLists;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class AddVillageBuildingsCallback implements ServerLifecycleEvents.ServerStarting {
    @Override
    public void onServerStarting(final MinecraftServer server) {
        addNewVillagerBuildings(server);
    }

    public void addNewVillagerBuildings(final MinecraftServer server) {
        Registry<StructurePool> templatePoolRegistry = server.getRegistryManager().get(RegistryKeys.TEMPLATE_POOL);
        Registry<StructureProcessorList> processorListRegistry = server.getRegistryManager().get(RegistryKeys.PROCESSOR_LIST);

        addBuildingToPool(StructureType.LEGACY,
                templatePoolRegistry,
                processorListRegistry, BRProcessorLists.DECAYING_HOUSE,
                BackroomsRedux.mcLoc("village/plains/houses"),
                "backrooms_redux:decaying/florist_shop.nbt",
                4);

        /*
        addBuildingToPool(StructureType.LEGACY,
                templatePoolRegistry,
                processorListRegistry, BRProcessorLists.DECAYING_HOUSE,
                BackroomsRedux.mcLoc("village/desert/houses"),
                "backrooms_redux:decaying/clothes_shop",
                2);

        addBuildingToPool(StructureType.LEGACY,
                templatePoolRegistry,
                processorListRegistry, BRProcessorLists.DECAYING_HOUSE,
                BackroomsRedux.mcLoc("village/savanna/houses"),
                "backrooms_redux:decaying/bakery",
                2);

        addBuildingToPool(StructureType.LEGACY,
                templatePoolRegistry,
                processorListRegistry, BRProcessorLists.DECAYING_HOUSE,
                BackroomsRedux.mcLoc("village/snowy/houses"),
                "backrooms_redux:decaying/unknown",
                1);

        addBuildingToPool(StructureType.LEGACY,
                templatePoolRegistry,
                processorListRegistry, BRProcessorLists.DECAYING_HOUSE,
                BackroomsRedux.mcLoc("village/taiga/houses"),
                "backrooms_redux:decaying/furniture_store",
                2); */
    }

    private static void addBuildingToPool(StructureType type,
                                          Registry<StructurePool> templatePoolRegistry,
                                          Registry<StructureProcessorList> processorListRegistry,
                                          RegistryKey<StructureProcessorList> processorKey,
                                          Identifier poolRL,
                                          String nbtPieceRL,
                                          int weight) {
        RegistryEntry<StructureProcessorList> structureProcessorList = processorListRegistry.getEntry(processorKey).get();

        StructurePool pool = templatePoolRegistry.get(poolRL);
        if (pool == null) return;

        // Grabs the nbt piece and creates a SinglePoolElement of it that we can add to a structure's pool.
        // Use .ofProcessedLegacySingle( for villages/outposts and .ofProcessedSingle( for everything else
        SinglePoolElement piece = type == StructureType.LEGACY ?
                SinglePoolElement.ofProcessedLegacySingle(nbtPieceRL, structureProcessorList).apply(StructurePool.Projection.RIGID)
                : SinglePoolElement.ofProcessedSingle(nbtPieceRL, structureProcessorList).apply(StructurePool.Projection.RIGID);

        // Use AccessWideners or Accessor Mixin to make StructurePool's templates field public for us to see.
        // Weight is handled by how many times the entry appears in this list.
        // We do not need to worry about immutability as this field is created using Lists.newArrayList(); which makes a mutable list.
        for (int i = 0; i < weight; i++) {
            ((StructurePoolAccessor)pool).backrooms_redux$getElements().add(piece);
        }

        // Use AccessWideners or Accessor Mixin to make StructurePool's elementCounts field public for us to see.
        // This list of pairs of pieces and weights is not used by vanilla by default but another mod may need it for efficiency.
        // So let's add to this list for completeness. We need to make a copy of the array as it can be an immutable list.
        //   NOTE: This is a com.mojang.datafixers.util.Pair. It is NOT a fastUtil pair class. Use the mojang class.
        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.elementCounts);
        listOfPieceEntries.add(new Pair<>(piece, weight));
        pool.elementCounts = listOfPieceEntries;
    }

    private enum StructureType {
        NORMAL,
        LEGACY
    }
}
