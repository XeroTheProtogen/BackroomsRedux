package net.keno.backrooms_redux.registry;

import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.blocks.LampBlock;
import net.keno.backrooms_redux.items.BRItemGroups;
import net.keno.backrooms_redux.worldgen.chunk.TestChunkGenerator;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class BRCommon {
    // Items

    // Blocks
    public static final Block YELLOW_WALLPAPER = registerBlock("yellow_wallpaper",
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOD)
                    .requiresTool().strength(2f, 2f)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, BackroomsRedux.modLoc("yellow_wallpaper")))), new Item.Settings());

    public static final Block MOIST_CARPET = registerBlock("moist_carpet",
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.WOOL)
                    .strength(-1f, 3600f)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, BackroomsRedux.modLoc("moist_carpet")))), new Item.Settings());

    public static final Block ROOF_TILE = registerBlock("roof_tile",
            new Block(AbstractBlock.Settings.create().sounds(BlockSoundGroup.STONE)
                    .strength(-1f, 3600f)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, BackroomsRedux.modLoc("roof_tile")))), new Item.Settings());

    public static final Block ROOF_LIGHT = registerBlock("tile_light",
            new LampBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.LANTERN)
                    .strength(-1f, 3600f)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, BackroomsRedux.modLoc("tile_light"))),
                    11, 0), new Item.Settings());

    private static void registerChunkGenerators() {
        if (BackroomsRedux.isDevEnvironment) {
            Registry.register(Registries.CHUNK_GENERATOR, BackroomsRedux.modLoc("test"), TestChunkGenerator.CODEC);
        }
    }

    public static Block registerBlock(String name, Block block, Item.Settings itemSettings) {
        final Identifier id = BackroomsRedux.modLoc(name);
        final RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        Block toReturn = Registry.register(Registries.BLOCK, blockKey, block);
        final RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);

        Registry.register(Registries.ITEM, itemKey,
                new BlockItem(block, itemSettings.useBlockPrefixedTranslationKey().registryKey(itemKey)));
        return toReturn;
    }

    public static void init() {
        registerChunkGenerators();
        BRItemGroups.init();
    }
}
