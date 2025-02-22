package net.keno.backrooms_redux.registry;

import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.worldgen.chunk.TestChunkGenerator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BRCommon {
    private static void registerChunkGenerators() {
        if (BackroomsRedux.isDevEnvironment) {
            Registry.register(Registries.CHUNK_GENERATOR, BackroomsRedux.modLoc("test"), TestChunkGenerator.CODEC);
        }
    }

    public static void init() {
        registerChunkGenerators();
    }
}
