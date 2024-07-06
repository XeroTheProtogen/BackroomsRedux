package keno.backrooms_redux.registry;

import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.block.LampBlock;
import keno.backrooms_redux.block.OverworldGlitchingBlock;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.GameRules;

import static keno.backrooms_redux.item.BRItemGroup.add;

public class BRCommonRegistry {
    private static final FabricBlockSettings MOIST_CARPET_SETTINGS = FabricBlockSettings.copyOf(Blocks.WHITE_WOOL).hardness(-1f);

    //Level 0 blocks
    public static final Block MONOYELLOW_WALLPAPER = registerBlock("monoyellow_wallpaper",
            new Block(FabricBlockSettings.create().requiresTool().hardness(2f)));
    public static final Block ROOF_TILE = registerBlock("roof_tile", new Block(FabricBlockSettings.copyOf(Blocks.STONE).hardness(-1f)));
    public static final Block MOIST_CARPET = registerBlock("moist_carpet", new Block(MOIST_CARPET_SETTINGS));
    public static final Block SOGGY_CARPET = registerBlock("soggy_carpet", new Block(MOIST_CARPET_SETTINGS));
    public static final Block MOIST_CARPET_STAIRS = registerBlock("moist_carpet_stairs",
            new StairsBlock(BRCommonRegistry.MOIST_CARPET.getDefaultState(), MOIST_CARPET_SETTINGS));
    public static final Block SOGGY_CARPET_STAIRS = registerBlock("soggy_carpet_stairs",
            new StairsBlock(BRCommonRegistry.SOGGY_CARPET.getDefaultState(), MOIST_CARPET_SETTINGS));
    public static final Block GLITCHED_PLANK = registerBlock("glitched_planks",
            new OverworldGlitchingBlock(FabricBlockSettings.copy(Blocks.BEDROCK)));
    public static final Block GLITCHED_STONE = registerBlock("glitched_stone",
            new OverworldGlitchingBlock(FabricBlockSettings.copy(Blocks.BEDROCK)));
    public static final Block TILE_LIGHT = registerBlock("tile_light",
            new LampBlock(FabricBlockSettings.create().hardness(-1f).luminance(state ->
                    state.get(LampBlock.LIT) ? 12 : 0)));

    //Game rules
    /**When on, this gamerule forces the player's spawn to be in the backrooms on first entry*/
    public static final GameRules.Key<GameRules.BooleanRule> RESPAWN_IN_BACKROOMS =
            GameRuleRegistry.register("spawn_in_backrooms", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
    /**When on, this gamerule clears the player's inventory (Excluding ender chest inventory) upon entering the backrooms via a glitched block*/
    public static final GameRules.Key<GameRules.BooleanRule> CLEAR_INVENTORY_ON_BACKROOMS_ENTRY =
            GameRuleRegistry.register("clear_inventory_backroom_entry", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

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
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries ->
                add(entries, BRCommonRegistry.GLITCHED_PLANK, BRCommonRegistry.GLITCHED_STONE));
    }
}
