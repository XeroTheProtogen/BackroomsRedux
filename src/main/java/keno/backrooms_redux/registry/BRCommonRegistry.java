package keno.backrooms_redux.registry;

import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.block.GlitchingBlock;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.GameRules;

public class BRCommonRegistry {
    private static final FabricBlockSettings MOIST_CARPET_SETTINGS = FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).hardness(-1f);

    //Level 0 blocks
    public static final Block MONOYELLOW_WALLPAPER = registerBlock("monoyellow_wallpaper", new Block(FabricBlockSettings.create().requiresTool()));
    public static final Block ROOF_TILE = registerBlock("roof_tile", new Block(FabricBlockSettings.copyOf(Blocks.STONE).hardness(-1f)));
    public static final Block MOIST_CARPET = registerBlock("moist_carpet", new Block(MOIST_CARPET_SETTINGS));
    public static final Block SOGGY_CARPET = registerBlock("soggy_carpet", new Block(MOIST_CARPET_SETTINGS));
    public static final Block MOIST_CARPET_STAIRS = registerBlock("moist_carpet_stairs",
            new StairsBlock(BRCommonRegistry.MOIST_CARPET.getDefaultState(), MOIST_CARPET_SETTINGS));
    public static final Block SOGGY_CARPET_STAIRS = registerBlock("soggy_carpet_stairs",
            new StairsBlock(BRCommonRegistry.SOGGY_CARPET.getDefaultState(), MOIST_CARPET_SETTINGS));
    public static final Block TEST_GLITCH_BLOCK = registerBlock("test_glitch_block",
            new GlitchingBlock(FabricBlockSettings.copy(Blocks.BEDROCK), BRRegistrar.LEVEL_0_WORLD));

    //Tags
    public static final TagKey<Block> CORRUPTIBLE_BLOCKS = TagKey.of(RegistryKeys.BLOCK, BackroomsRedux.modLoc("corruptible_blocks"));

    //Gamerules
    public static final GameRules.Key<GameRules.BooleanRule> RESPAWN_IN_BACKROOMS =
            GameRuleRegistry.register("spawn_in_backrooms", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

    //Registry methods
    //If you just want a plain ol' item, use this method
    private static Item registerBasicItem(String name) {
        return Registry.register(Registries.ITEM, BackroomsRedux.modLoc(name), new Item(new FabricItemSettings()));
    }

    //If you plan on giving the item different settings or have a custom item subclass (High chance you would), use this method
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, BackroomsRedux.modLoc(name), item);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, BackroomsRedux.modLoc(name), block);
    }


    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, BackroomsRedux.modLoc(name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void init() {

    }
}
