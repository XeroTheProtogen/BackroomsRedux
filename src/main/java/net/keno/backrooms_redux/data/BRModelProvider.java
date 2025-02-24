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
        blockStateModelGenerator.registerSimpleCubeAll(BRCommon.ROOF_TILE);
        blockStateModelGenerator.registerSimpleCubeAll(BRCommon.MOIST_CARPET);
        blockStateModelGenerator.registerSimpleCubeAll(BRCommon.YELLOW_WALLPAPER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
