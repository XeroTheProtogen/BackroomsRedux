package net.keno.backrooms_redux.worldgen;

import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.registry.BRRegistrar;
import net.keno.backrooms_redux.worldgen.chunk.levels.Level0ChunkGenerator;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class BRBiomes {
    public static final TagKey<Biome> LONELY_BIOMES = TagKey.of(RegistryKeys.BIOME,
            BackroomsRedux.modLoc("lonely_biomes"));

    public static final RegistryKey<Biome> TEST_BIOME = RegistryKey.of(RegistryKeys.BIOME, BRRegistrar.TEST);

    public static final RegistryKey<Biome> LEVEL_0_BIOME = RegistryKey.of(RegistryKeys.BIOME, Level0ChunkGenerator.LEVEL_0);

    public static Biome create(RegistryEntryLookup<PlacedFeature> features,
                               RegistryEntryLookup<ConfiguredCarver<?>> carvers,
                               int fogColor,
                               int waterColor,
                               int waterFogColor,
                               int skyColor,
                               @Nullable Function<BiomeEffects.Builder, BiomeEffects.Builder> effectsHandler,
                               @Nullable Function<Biome.Builder, Biome.Builder> biomeHandler) {
        Biome.Builder biome = new Biome.Builder();

        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder generationSettings = new GenerationSettings.LookupBackedBuilder(features, carvers);

        // (your effects here)
        BiomeEffects.Builder effectsBuilder = new BiomeEffects.Builder()
                .fogColor(fogColor)
                .waterColor(waterColor)
                .waterFogColor(waterFogColor)
                .skyColor(skyColor);

        if (effectsHandler != null) {
            effectsBuilder = effectsHandler.apply(effectsBuilder);
        }

        BiomeEffects effects = effectsBuilder.build();

        biome.spawnSettings(spawnSettings.build());
        biome.generationSettings(generationSettings.build());
        biome.effects(effects);

        if (biomeHandler != null) {
            biome = biomeHandler.apply(biome);
        }

        return biome.build();
    }
}
