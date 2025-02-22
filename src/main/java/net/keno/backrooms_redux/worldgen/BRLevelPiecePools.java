package net.keno.backrooms_redux.worldgen;

import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.levels.LevelPool;

import java.util.List;
import java.util.Map;

public class BRLevelPiecePools {
    public static final LevelPool TEST_LEVEL_POOL
            = new LevelPool(BackroomsRedux.modLoc("test"),
            Map.of("common", List.of("0", "1", "2")));

    public static void init() {
        BackroomsRedux.LOGGER.info("Initializing dynamic groups...");
    }
}
