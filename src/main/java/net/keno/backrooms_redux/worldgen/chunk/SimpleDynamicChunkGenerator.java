package net.keno.backrooms_redux.worldgen.chunk;

import net.keno.backrooms_redux.BackroomsRedux;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.world.biome.source.BiomeSource;

/** Use this as your chunk generator's superclass if you want to implement dynamic nbt generation without some of the messiness*/
public abstract class SimpleDynamicChunkGenerator extends AbstractNbtChunkGenerator implements DynamicNbtUpdater {
    private static final NbtGroup DUMMY_GROUP = NbtGroup.Builder.create(BackroomsRedux.modLoc("fake")).build();

    public SimpleDynamicChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource, DUMMY_GROUP);
    }

    abstract NbtGroup getGroup();

    @Override
    public NbtGroup getDynamicGroup() {
        try {
            return getGroup();
        } catch (Exception e) {
            return this.nbtGroup;
        }
    }
}
