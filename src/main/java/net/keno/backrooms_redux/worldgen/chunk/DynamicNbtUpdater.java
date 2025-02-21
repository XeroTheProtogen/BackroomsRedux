package net.keno.backrooms_redux.worldgen.chunk;

import net.ludocrypt.limlib.api.world.FunctionMap;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.NbtPlacerUtil;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public interface DynamicNbtUpdater {
    default void cycle(AbstractNbtChunkGenerator chunkGenerator) {
        if (chunkGenerator.nbtGroup != getDynamicGroup()) {
            ((UpdatedNbtContainer)chunkGenerator).setGroup(getDynamicGroup());
            FunctionMap<Identifier, NbtPlacerUtil, ResourceManager> structures = new FunctionMap<>(NbtPlacerUtil::load);
            ((UpdatedNbtContainer)chunkGenerator).getGroup().fill(structures);
            ((UpdatedNbtContainer)chunkGenerator).setStructures(structures);
        }
    }

    NbtGroup getDynamicGroup();
}
