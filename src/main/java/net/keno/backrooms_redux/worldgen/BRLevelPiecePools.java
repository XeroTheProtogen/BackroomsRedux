package net.keno.backrooms_redux.worldgen;

import net.keno.backrooms_redux.BackroomsRedux;
import net.ludocrypt.limlib.api.world.NbtGroup;

public class BRLevelPiecePools {
    public static final NbtGroup TEST_DYNAMIC_GROUP = NbtGroup.Builder
            .create(BackroomsRedux.modLoc("test"))
            .with("common", "0", "1", "2")
            .build();

    public static void init() {
        BackroomsRedux.LOGGER.info("Initializing dynamic groups...");
    }
}
