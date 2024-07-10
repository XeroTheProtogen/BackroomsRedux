package keno.backrooms_redux.worldgen.chunk;

import keno.backrooms_redux.BackroomsRedux;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BRChunkGenerators {
    public static void init() {
        Registry.register(Registries.CHUNK_GENERATOR, BackroomsRedux.modLoc("level_0_chunk_generator"), Level0ChunkGenerator.CODEC);
    }
}
