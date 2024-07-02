package keno.backrooms_redux.registry;

import com.mojang.serialization.Lifecycle;
import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.world.biome.BRBiomes;
import keno.backrooms_redux.world.biome.Level0Biome;
import keno.backrooms_redux.world.chunk.Level0ChunkGenerator;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.OptionalLong;

public class BRRegistrar implements LimlibRegistrar {
    public static final Identifier LEVEL_O_ID = BackroomsRedux.modLoc("level_0");

    public static final LimlibWorld LEVEL_0 = new LimlibWorld(() -> new DimensionType(OptionalLong.empty(),
            false, false, false, false, 8.0d,
            true, true, 0, 80, 0, BlockTags.INFINIBURN_OVERWORLD,
            Identifier.of("minecraft", "end"), 0f,
            new DimensionType.MonsterSettings(true, false, ConstantIntProvider.ZERO, 0)),
            (registry) ->
                    new DimensionOptions(
                            registry.get(RegistryKeys.DIMENSION_TYPE)
                                    .getOptional(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, BackroomsRedux.modLoc("level_0")))
                                    .get(),
                            new Level0ChunkGenerator(
                                    new FixedBiomeSource(registry.get(RegistryKeys.BIOME).getOptional(BRBiomes.LEVEL_0).get()))));

    public static final RegistryKey<World> LEVEL_0_WORLD = RegistryKey.of(RegistryKeys.WORLD, LEVEL_O_ID);


    @Override
    public void registerHooks() {
        Registry.register(LimlibWorld.LIMLIB_WORLD, LEVEL_O_ID, LEVEL_0);

        LimlibRegistryHooks.hook(RegistryKeys.BIOME, ((infoLookup, registryKey, registry) -> {
            RegistryEntryLookup<PlacedFeature> features = infoLookup.getRegistryInfo(RegistryKeys.PLACED_FEATURE).get().entryLookup();
            RegistryEntryLookup<ConfiguredCarver<?>> carvers = infoLookup.getRegistryInfo(RegistryKeys.CONFIGURED_CARVER).get().entryLookup();

            registry.add(BRBiomes.LEVEL_0, Level0Biome.create(features, carvers), Lifecycle.stable());
        }));
    }
}
