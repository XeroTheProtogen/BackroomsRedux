package keno.backrooms_redux.datagen;

import keno.backrooms_redux.registry.BRCommonRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class BRModelGen extends FabricModelProvider {
    public BRModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        level0BlockModels(blockStateModelGenerator);
    }

    //Moist carpet block models
    private void moistCarpetModels(BlockStateModelGenerator generator) {
        BlockStateModelGenerator.BlockTexturePool moist_carpet = generator.registerCubeAllModelTexturePool(
                BRCommonRegistry.MOIST_CARPET);
        moist_carpet.stairs(BRCommonRegistry.MOIST_CARPET_STAIRS);
    }

    //All custom blocks used in level 0 and associated sublevels have their models generated here
    private void level0BlockModels(BlockStateModelGenerator generator) {
        moistCarpetModels(generator);
        generator.registerSimpleCubeAll(BRCommonRegistry.MONOYELLOW_WALLPAPER);
        generator.registerSimpleCubeAll(BRCommonRegistry.ROOF_TILE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
