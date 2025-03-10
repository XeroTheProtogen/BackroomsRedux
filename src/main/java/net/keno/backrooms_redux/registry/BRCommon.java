package net.keno.backrooms_redux.registry;

import com.mojang.serialization.MapCodec;
import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.blocks.LampBlock;
import net.keno.backrooms_redux.data.holders.pos_determ.*;
import net.keno.backrooms_redux.items.BRItemGroups;
import net.keno.backrooms_redux.worldgen.chunk.TestChunkGenerator;
import net.keno.backrooms_redux.worldgen.chunk.levels.Level0ChunkGenerator;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class BRCommon {
    public static final BlockSetType WAREHOUSE = new BlockSetType("warehouse", true, true, true,
                     BlockSetType.ActivationRule.MOBS, BlockSoundGroup.METAL,
                     SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundEvents.BLOCK_IRON_DOOR_OPEN,
            SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN,
            SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, SoundEvents.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON,
            SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON);

    // Pos Determiners
    public static final DeterminerType<ExactPosDeterminer> EXACT_POS =
            DeterminerType.register(BackroomsRedux.modLoc("exact"), ExactPosDeterminer.CODEC);
    public static final DeterminerType<RandOffsetPosDeterminer> RAND_OFFSET_POS =
            DeterminerType.register(BackroomsRedux.modLoc("rand_offset"), RandOffsetPosDeterminer.CODEC);
    public static final DeterminerType<NoPosDeterminer> NO_POS =
            DeterminerType.register(BackroomsRedux.modLoc("no_pos"), NoPosDeterminer.CODEC);
    public static final DeterminerType<OverridePosDeterminer> OVERRIDE_POS =
            DeterminerType.register(BackroomsRedux.modLoc("override_pos"), OverridePosDeterminer.CODEC);

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
                    10, 0), new Item.Settings());

    public static final Block YELLOW_WALLPAPER_STAIRS = registerBlock("yellow_wallpaper_stairs",
            new StairsBlock(YELLOW_WALLPAPER.getDefaultState(),
                    AbstractBlock.Settings.copy(YELLOW_WALLPAPER).registryKey(RegistryKey.of(RegistryKeys.BLOCK, BackroomsRedux.modLoc("yellow_wallpaper_stairs")))),
            new Item.Settings());

    public static final Block ROOF_TILE_STAIRS = registerBlock("roof_tile_stairs",
            new StairsBlock(ROOF_TILE.getDefaultState(), AbstractBlock.Settings.copy(ROOF_TILE)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK,
                            BackroomsRedux.modLoc("roof_tile_stairs")))),
            new Item.Settings());

    public static final Block WAREHOUSE_CONCRETE = registerBlock("warehouse_concrete",
            new Block(AbstractBlock.Settings.copy(Blocks.BLACK_CONCRETE)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK,
                            BackroomsRedux.modLoc("warehouse_concrete")))),
            new Item.Settings());

    public static final Block WAREHOUSE_DOOR = registerBlock("warehouse_door",
            new DoorBlock(WAREHOUSE, AbstractBlock.Settings
                    .copy(Blocks.IRON_DOOR).registryKey(RegistryKey.of(RegistryKeys.BLOCK,
                            BackroomsRedux.modLoc("warehouse_door")))),
            new Item.Settings());



    private static void registerChunkGenerators() {
        if (BackroomsRedux.isDevEnvironment) {
            Registry.register(Registries.CHUNK_GENERATOR, BackroomsRedux.modLoc("test"), TestChunkGenerator.CODEC);
        }

        registerChunkGenCodec("level_0", Level0ChunkGenerator.CODEC);
    }

    public static <e extends ChunkGenerator> void registerChunkGenCodec(String name, MapCodec<e> codec) {
        Registry.register(Registries.CHUNK_GENERATOR, BackroomsRedux.modLoc(name), codec);
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
        BRSounds.init();
    }
}
