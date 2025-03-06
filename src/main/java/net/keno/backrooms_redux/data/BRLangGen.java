package net.keno.backrooms_redux.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.keno.backrooms_redux.client.BRClient;
import net.keno.backrooms_redux.registry.BRCommon;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BRLangGen extends FabricLanguageProvider {
    public BRLangGen(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder builder) {
        builder.add(BRCommon.ROOF_TILE, "Roof Tile");
        builder.add(BRCommon.MOIST_CARPET, "Moist Carpet");
        builder.add(BRCommon.YELLOW_WALLPAPER, "Yellow Wallpaper");
        builder.add(BRCommon.ROOF_LIGHT, "Tile Light");
        builder.add(BRCommon.YELLOW_WALLPAPER_STAIRS, "Yellow Wallpaper Stairs");
        builder.add(BRCommon.ROOF_TILE_STAIRS, "Roof Tile Stairs");
        builder.add(BRCommon.WAREHOUSE_CONCRETE, "Warehouse Concrete");
        builder.add(BRCommon.WAREHOUSE_DOOR, "Warehouse Door");


        builder.add("itemGroup.br_wave_1", "BR: The Beginning");
        builder.add("backrooms_redux.key.no_clip", "No-Clip");
        builder.add("backrooms_redux.cannot_noclip", "You somehow realize no-clipping wouldn't work here");
        builder.add("backrooms_redux.fail_noclip", "You get hurt trying to no-clip. Ouch");
        builder.add(BRClient.KEY_CATEGORY, "Backrooms Redux");
    }
}
