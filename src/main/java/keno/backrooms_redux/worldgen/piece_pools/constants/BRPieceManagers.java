package keno.backrooms_redux.worldgen.piece_pools.constants;

import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.block.LampBlock;
import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.worldgen.piece_pools.PieceManager;
import keno.backrooms_redux.worldgen.piece_pools.PoolArraysSingleton;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;

import static keno.backrooms_redux.registry.BRCommonRegistry.TILE_LIGHT;

public final class BRPieceManagers {
    public static final Identifier LEVEL_0_MANAGER = BackroomsRedux.modLoc("level_0");

    public static void registerPieceManagers(PoolArraysSingleton singleton) {
        PieceManager level0Manager = new PieceManager(BRPieceManagers.LEVEL_0_MANAGER);
        level0Manager.registerPiecesToPool(BRPiecePools.LEVEL_0_COMMON,
                "level_0_common_1", "level_0_common_2",
                "level_0_common_3", "level_0_common_4");
        level0Manager.registerPiecesToPool(BRPiecePools.LEVEL_0_UNCOMMON,
                "level_0_uncommon_1", "level_0_uncommon_2",
                "level_0_uncommon_3", "level_0_uncommon_4");
        level0Manager.registerPiecesToPool(BRPiecePools.LEVEL_0_RARE,
                "level_0_tent", "level_0_chasm");
        level0Manager.registerPiecesToPool(BRPiecePools.LEVEL_0_MANILLA_ROOM, "level_0_manilla_room");

        PieceManager preppedLevel0Manager = level0PieceProcessor(level0Manager);

        singleton.addManagerToPool(BRPieceManagers.LEVEL_0_MANAGER, preppedLevel0Manager);
    }

    private static PieceManager level0PieceProcessor(PieceManager manager) {
        manager.registerPieceProcessor((region, pos, state, optionalNbtCompound, random) -> {
            if (state.isOf(BRCommonRegistry.MOIST_CARPET)) {
                if (random.nextDouble() <= 0.05) {
                    region.setBlockState(pos, BRCommonRegistry.SOGGY_CARPET.getDefaultState(), Block.NOTIFY_ALL, 1);
                }
            }

            if (state.isOf(BRCommonRegistry.MOIST_CARPET_STAIRS)) {
                if (random.nextDouble() < 0.1) {
                    region.setBlockState(pos, BRCommonRegistry.SOGGY_CARPET_STAIRS.getStateWithProperties(state), Block.NOTIFY_ALL, 1);
                }
            }

            if (state.isOf(Blocks.WHITE_WOOL)) {
                if ((pos.getX() % 6 == 0 && pos.getZ() % 6 == 0) && pos.getY() == 5) {
                    region.setBlockState(pos, TILE_LIGHT.getDefaultState().with(LampBlock.LIT,
                            random.nextDouble() > 0.2), Block.NOTIFY_ALL, 1);
                } else {
                    region.setBlockState(pos, BRCommonRegistry.ROOF_TILE.getDefaultState(), Block.NOTIFY_ALL, 1);
                }
            }

            if (state.isOf(Blocks.DIRT)) {
                if (random.nextBoolean()) {
                    region.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState(), Block.NOTIFY_ALL, 1);
                }
            }
        });
        return manager;
    }
}
