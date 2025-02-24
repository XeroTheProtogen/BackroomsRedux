package net.keno.backrooms_redux.worldgen.chunk;

import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.world.biome.source.BiomeSource;

/** Use this as your chunk generator's superclass if you want to implement dynamic nbt generation without some of the messiness */
public abstract class SimpleDynamicChunkGenerator extends AbstractNbtChunkGenerator implements DynamicNbtUpdater {
    /** @param fallbackGroup The group used if the normal dynamic group can't be acquired */
    public SimpleDynamicChunkGenerator(BiomeSource biomeSource, NbtGroup fallbackGroup) {
        super(biomeSource, fallbackGroup);
    }

    /** Override this to set your dynamic group */
    public abstract NbtGroup getGroup();

    /** Don't override this, override getGroup() instead
     * @see #getGroup() */
    @Override
    public NbtGroup getDynamicGroup() {
        try {
            return getGroup();
        } catch (Exception e) {
            return this.nbtGroup;
        }
    }
}
