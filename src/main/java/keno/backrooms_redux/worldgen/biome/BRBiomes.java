package keno.backrooms_redux.worldgen.biome;

import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.registry.BRRegistrar;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class BRBiomes {
    /** Used for things like insanity */
    public static final TagKey<Biome> BACKROOMS_BIOMES = TagKey.of(RegistryKeys.BIOME,
            BackroomsRedux.modLoc("backrooms_biomes"));
    public static final RegistryKey<Biome> LEVEL_0 = get(BRRegistrar.LEVEL_O_ID);

    public static RegistryKey<Biome> get(Identifier id) {
        return RegistryKey.of(RegistryKeys.BIOME, id);
    }

    public static void registerBiomes() {
        BackroomsRedux.LOGGER.info("Registering biomes");
    }
}
