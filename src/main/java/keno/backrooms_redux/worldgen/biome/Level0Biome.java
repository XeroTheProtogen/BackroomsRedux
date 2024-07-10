package keno.backrooms_redux.worldgen.biome;

import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

public class Level0Biome {
    public static Biome create(RegistryEntryLookup<PlacedFeature> features, RegistryEntryLookup<ConfiguredCarver<?>> carvers) {
        Biome.Builder biome = new Biome.Builder();

        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();

        GenerationSettings.LookupBackedBuilder generationSettings = new GenerationSettings.LookupBackedBuilder(features, carvers);

        BiomeEffects.Builder biomeEffects = new BiomeEffects.Builder();
        biomeEffects.fogColor(0xff000000);
        biomeEffects.skyColor(0xff000000);
        biomeEffects.waterColor(0xff86cecb);
        biomeEffects.waterFogColor(0xff86cecb);
        BiomeEffects effects = biomeEffects.build();

        biome.spawnSettings(spawnSettings.build());
        biome.generationSettings(generationSettings.build());
        biome.effects(effects);

        biome.temperature(0.8f);
        biome.downfall(0f);

        return biome.build();
    }
}
