package net.keno.backrooms_redux.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.keno.backrooms_redux.registry.BRCommon;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class BRBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public BRBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(BRCommon.YELLOW_WALLPAPER, BRCommon.YELLOW_WALLPAPER_STAIRS);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(BRCommon.WAREHOUSE_CONCRETE, BRCommon.WAREHOUSE_DOOR);
        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(BRCommon.WAREHOUSE_CONCRETE, BRCommon.WAREHOUSE_DOOR);
        getOrCreateTagBuilder(BlockTags.WITHER_IMMUNE).add(BRCommon.MOIST_CARPET,
                BRCommon.ROOF_LIGHT, BRCommon.ROOF_TILE, BRCommon.ROOF_TILE_STAIRS);
    }
}
