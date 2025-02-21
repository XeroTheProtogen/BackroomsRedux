package net.keno.backrooms_redux.registry;

import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.worldgen.chunk.TestChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BRCommon {
    public static void init() {
        Registry.register(Registries.CHUNK_GENERATOR, BackroomsRedux.modLoc("test"), TestChunkGenerator.CODEC);
    }
}
