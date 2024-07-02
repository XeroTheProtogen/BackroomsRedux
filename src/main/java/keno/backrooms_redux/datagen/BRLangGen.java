package keno.backrooms_redux.datagen;

import keno.backrooms_redux.item.BRItemGroup;
import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.registry.BRRegistrar;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class BRLangGen extends FabricLanguageProvider {
    public BRLangGen(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        level0RelatedText(translationBuilder);
    }

    private void level0RelatedText(TranslationBuilder builder) {
        builder.add(BRCommonRegistry.MOIST_CARPET, "Moist Carpet");
        builder.add(BRCommonRegistry.MOIST_CARPET_STAIRS, "Moist Carpet Stairs");
        builder.add(BRCommonRegistry.MONOYELLOW_WALLPAPER, "Monoyellow Wallpaper");
        builder.add(BRCommonRegistry.ROOF_TILE, "Roof Tile");
        builder.add(BRItemGroup.LEVEL_0_GROUP_KEY, "Level 0");
        builder.add(BRCommonRegistry.SOGGY_CARPET_STAIRS, "Soggy Carpet Stairs");
        builder.add(BRCommonRegistry.SOGGY_CARPET, "Soggy Carpet");
        builder.add(BRRegistrar.LEVEL_0_WORLD.getValue().toTranslationKey(), "Level 0: The Tutorial");
        builder.add(BRCommonRegistry.RESPAWN_IN_BACKROOMS.getTranslationKey(), "Respawn In The Backrooms");
    }
}
