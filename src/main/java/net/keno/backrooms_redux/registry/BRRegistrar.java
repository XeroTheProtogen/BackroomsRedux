package net.keno.backrooms_redux.registry;

import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.worldgen.BRBiomes;
import net.keno.backrooms_redux.worldgen.chunk.TestChunkGenerator;
import net.keno.backrooms_redux.worldgen.chunk.levels.Level0ChunkGenerator;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.ludocrypt.limlib.api.effects.sound.SoundEffects;
import net.ludocrypt.limlib.api.effects.sound.reverb.StaticReverbEffect;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryInfo;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Function;

public class BRRegistrar implements LimlibRegistrar {
    public static final Identifier TEST = BackroomsRedux.modLoc("test");

    public static final LimlibWorld TEST_WORLD = new LimlibWorld(()
            -> new DimensionType(OptionalLong.empty(),
            false, false, false, false,
            2.0f, false, false, -32, 256,
            10, BlockTags.INFINIBURN_OVERWORLD, Identifier.ofVanilla("the_end"),
            1f, new DimensionType.MonsterSettings(false, false,
            UniformIntProvider.create(0, 6), 7)),
            registry ->
                    new DimensionOptions(registry
                            .get(RegistryKeys.DIMENSION_TYPE)
                            .getOptional(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, TEST))
                            .get(),
                            new TestChunkGenerator(new FixedBiomeSource(registry
                                    .get(RegistryKeys.BIOME)
                                    .getOptional(BRBiomes.TEST_BIOME)
                                    .get()))));

    public static final LimlibWorld LEVEL_0_WORLD = registerLevel(new DimensionType(OptionalLong.empty(),
            false, false, false, false, 2.0f,
            false, false, -32, 256, 4, BlockTags.INFINIBURN_OVERWORLD,
                    Identifier.ofVanilla("overworld"), 0f,
                    new DimensionType.MonsterSettings(false, false,
            UniformIntProvider.create(0, 6), 7)), Level0ChunkGenerator.LEVEL_0,
            registry -> new Level0ChunkGenerator(new FixedBiomeSource(registry
                    .get(RegistryKeys.BIOME)
                    .getOptional(BRBiomes.LEVEL_0_BIOME)
                    .get())));

    // Sound Effects
    public static final SoundEffects LIGHT_HUM = new SoundEffects(Optional.of(new StaticReverbEffect
            .Builder().setDecayHFRatio(1.5f).build()),
            Optional.empty(),
            Optional.of(BRSounds.HUM_BUZZ_MUSIC));


    public static <e extends AbstractNbtChunkGenerator> LimlibWorld registerLevel(DimensionType type, Identifier id,
                                                                                  Function<LimlibWorld.RegistryProvider, e> generator) {
        return new LimlibWorld(() -> type, registry -> new DimensionOptions(registry
                .get(RegistryKeys.DIMENSION_TYPE).getOptional(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, id)).get(),
                generator.apply(registry)));
    }

    @Override
    public void registerHooks() {
        if (BackroomsRedux.isDevEnvironment) {
            LimlibWorld.LIMLIB_WORLD
                    .add(RegistryKey.of(LimlibWorld.LIMLIB_WORLD_KEY, TEST), TEST_WORLD, RegistryEntryInfo.DEFAULT);
        }

        LimlibWorld.LIMLIB_WORLD.add(RegistryKey.of(LimlibWorld.LIMLIB_WORLD_KEY, Level0ChunkGenerator.LEVEL_0), LEVEL_0_WORLD, RegistryEntryInfo.DEFAULT);

        LimlibRegistryHooks.hook(RegistryKeys.BIOME, (infoLookup, registryKey, registry) -> {
            RegistryEntryLookup<PlacedFeature> features = infoLookup.getRegistryInfo(RegistryKeys.PLACED_FEATURE).get().entryLookup();
            RegistryEntryLookup<ConfiguredCarver<?>> carvers = infoLookup.getRegistryInfo(RegistryKeys.CONFIGURED_CARVER).get().entryLookup();

            if (BackroomsRedux.isDevEnvironment) {
                registry.add(BRBiomes.TEST_BIOME,
                        BRBiomes.create(features, carvers, 0xfede3a, 0x00ecff, 0x00ecff,
                                0x000000, null,
                                biome -> biome.precipitation(false).temperature(0.5f).downfall(0.3f)), RegistryEntryInfo.DEFAULT);
            }

            registry.add(BRBiomes.LEVEL_0_BIOME,
                    BRBiomes.create(features, carvers, 0xfede3a, 0x00ecff, 0x00ecff,
                            0x000000, null,
                            biome -> biome.precipitation(false).temperature(0.5f).downfall(0.3f)), RegistryEntryInfo.DEFAULT);
        });

        LimlibRegistryHooks.hook(SoundEffects.SOUND_EFFECTS_KEY,
                (infoLookup, registryKey, registry) -> {
                    registerSoundEffect(registry, "level_0", LIGHT_HUM);
                });
    }

    public static <e extends SoundEffects> void registerSoundEffect(MutableRegistry<SoundEffects> registry, String id, e soundEffects) {
        registry.add(RegistryKey.of(SoundEffects.SOUND_EFFECTS_KEY, BackroomsRedux.modLoc(id)), soundEffects, RegistryEntryInfo.DEFAULT);
    }
}
