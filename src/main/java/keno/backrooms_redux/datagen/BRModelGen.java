package keno.backrooms_redux.datagen;

import keno.backrooms_redux.registry.BRCommonRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class BRModelGen extends FabricModelProvider {
    public BRModelGen(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        level0BlockModels(blockStateModelGenerator);
        miscBlockModels(blockStateModelGenerator);
    }

    private void miscBlockModels(BlockStateModelGenerator generator) {
        generator.registerSimpleCubeAll(BRCommonRegistry.GLITCHED_STONE);
        generator.registerSimpleCubeAll(BRCommonRegistry.GLITCHED_PLANK);
    }

    //Moist carpet block models
    private void moistCarpetModels(BlockStateModelGenerator generator) {
        BlockStateModelGenerator.BlockTexturePool moist_carpet = generator.registerCubeAllModelTexturePool(
                BRCommonRegistry.MOIST_CARPET);
        BlockStateModelGenerator.BlockTexturePool soggy_carpet = generator.registerCubeAllModelTexturePool(
                BRCommonRegistry.SOGGY_CARPET);

        moist_carpet.stairs(BRCommonRegistry.MOIST_CARPET_STAIRS);
        soggy_carpet.stairs(BRCommonRegistry.SOGGY_CARPET_STAIRS);
    }

    //All custom blocks used in level 0 and associated sublevels have their models generated here
    private void level0BlockModels(BlockStateModelGenerator generator) {
        moistCarpetModels(generator);
        generator.registerSimpleCubeAll(BRCommonRegistry.MONOYELLOW_WALLPAPER);
        generator.registerSimpleCubeAll(BRCommonRegistry.ROOF_TILE);
    }

    private void registerSpawnEggModels(ItemModelGenerator generator, Item... items) {
        for (Item item : items) {
            generator.register(item, new Model(Optional.of(new Identifier("item/template_spawn_egg")), Optional.empty()));
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(BRCommonRegistry.ALMOND_WATER, Models.GENERATED);
        registerSpawnEggModels(itemModelGenerator, BRCommonRegistry.HALLUCINATION_SPAWN_EGG);
    }
}
