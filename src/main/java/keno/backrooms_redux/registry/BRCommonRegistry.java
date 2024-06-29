package keno.backrooms_redux.registry;

import keno.backrooms_redux.BackroomsRedux;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BRCommonRegistry {
    public static final Block MONOYELLOW_WALLPAPER = registerBlock("monoyellow_wallpaper", new Block(FabricBlockSettings.create().requiresTool()));
    public static final Block MOIST_CARPER = registerBlock("moist_carpet", new Block(FabricBlockSettings.create().hardness(-1f)));

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
