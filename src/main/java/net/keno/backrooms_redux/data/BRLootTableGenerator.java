package net.keno.backrooms_redux.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.keno.backrooms_redux.registry.BRCommon;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BRLootTableGenerator extends FabricBlockLootTableProvider {
    public BRLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(BRCommon.WAREHOUSE_CONCRETE);
        addDrop(BRCommon.WAREHOUSE_DOOR);
        addDrop(BRCommon.YELLOW_WALLPAPER);
        addDrop(BRCommon.YELLOW_WALLPAPER_STAIRS);
    }
}
