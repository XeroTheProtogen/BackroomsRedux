package keno.backrooms_redux.datagen;

import keno.backrooms_redux.client.screens.NoclipWarningScreen;
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
        guiText(translationBuilder);
        gameRuleText(translationBuilder);
        miscText(translationBuilder);
    }

    private void itemText(TranslationBuilder builder) {
        builder.add(BRCommonRegistry.ALMOND_WATER, "Almond Water");
    }

    private void level0RelatedText(TranslationBuilder builder) {
        builder.add(BRCommonRegistry.MOIST_CARPET, "Moist Carpet");
        builder.add(BRCommonRegistry.MOIST_CARPET_STAIRS, "Moist Carpet Stairs");
        builder.add(BRCommonRegistry.MONOYELLOW_WALLPAPER, "Monoyellow Wallpaper");
        builder.add(BRCommonRegistry.ROOF_TILE, "Roof Tile");
        builder.add(BRItemGroup.LEVEL_0_GROUP_KEY, "Level 0");
        builder.add(BRCommonRegistry.SOGGY_CARPET_STAIRS, "Soggy Carpet Stairs");
        builder.add(BRCommonRegistry.SOGGY_CARPET, "Soggy Carpet");
        builder.add(BRCommonRegistry.TILE_LIGHT, "Tile Light");
        builder.add(BRRegistrar.LEVEL_0_WORLD.getValue().toTranslationKey(), "Level 0: The Tutorial");
    }

    private void guiText(TranslationBuilder builder) {
        builder.add(NoclipWarningScreen.AGREE_KEY, "Yes");
        builder.add(NoclipWarningScreen.WARNING_KEY, "(Interacting with it will have dire consequences, do you touch the block?)");
        builder.add(NoclipWarningScreen.OMEN_KEY, "Reality unfolds around you...");
    }

    private void gameRuleText(TranslationBuilder builder) {
        builder.add(BRCommonRegistry.CLEAR_INVENTORY_ON_BACKROOMS_ENTRY.getTranslationKey(), "clearInvOnBackroomsEntry");
        builder.add(BRCommonRegistry.RESPAWN_IN_BACKROOMS.getTranslationKey(), "respawnInBackrooms");
    }

    private void miscText(TranslationBuilder builder) {
        builder.add(BRCommonRegistry.GLITCHED_PLANK.getTranslationKey(), "Glitched Planks");
        builder.add(BRCommonRegistry.GLITCHED_STONE.getTranslationKey(), "Glitched Stone");
        builder.add("backrooms_redux.sanity.beginning", "Grasping the situation, you become nervous...");
    }
}
