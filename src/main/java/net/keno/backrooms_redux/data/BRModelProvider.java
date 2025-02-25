package net.keno.backrooms_redux.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.keno.backrooms_redux.registry.BRCommon;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class BRModelProvider extends FabricModelProvider {
    public BRModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Texture Pools
        BlockStateModelGenerator.BlockTexturePool yellowWallpaperPool
                = blockStateModelGenerator.registerCubeAllModelTexturePool(BRCommon.YELLOW_WALLPAPER);
        BlockStateModelGenerator.BlockTexturePool roofTilePool
                = blockStateModelGenerator.registerCubeAllModelTexturePool(BRCommon.ROOF_TILE);

        blockStateModelGenerator.registerSimpleCubeAll(BRCommon.MOIST_CARPET);

        // Stairs
        yellowWallpaperPool.stairs(BRCommon.YELLOW_WALLPAPER_STAIRS);
        roofTilePool.stairs(BRCommon.ROOF_TILE_STAIRS);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
