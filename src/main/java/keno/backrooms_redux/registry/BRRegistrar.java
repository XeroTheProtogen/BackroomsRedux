package keno.backrooms_redux.registry;

import com.mojang.serialization.Lifecycle;
import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.worldgen.biome.BRBiomes;
import keno.backrooms_redux.worldgen.biome.Level0Biome;
import keno.backrooms_redux.worldgen.chunk.Level0ChunkGenerator;
import keno.backrooms_redux.worldgen.piece_pools.BRPiecePools;
import keno.backrooms_redux.worldgen.piece_pools.PoolArraysSingleton;
import net.ludocrypt.limlib.api.LimlibRegistrar;
import net.ludocrypt.limlib.api.LimlibRegistryHooks;
import net.ludocrypt.limlib.api.LimlibWorld;
import net.ludocrypt.limlib.api.effects.sound.SoundEffects;
import net.ludocrypt.limlib.api.effects.sound.reverb.StaticReverbEffect;
import net.ludocrypt.limlib.api.skybox.EmptySkybox;
import net.ludocrypt.limlib.api.skybox.Skybox;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.MusicSound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.Optional;
import java.util.OptionalLong;

public class BRRegistrar implements LimlibRegistrar {
    public static final Identifier LEVEL_O_ID = BackroomsRedux.modLoc("level_0");
    private static final PoolArraysSingleton singleton = PoolArraysSingleton.getInstance();


    public static final LimlibWorld LEVEL_0 = new LimlibWorld(() -> new DimensionType(OptionalLong.of(13000),
            false, false, false, false, 8.0d,
            true, true, -64, 80, 0, BlockTags.INFINIBURN_OVERWORLD,
            Identifier.of("minecraft", "end"), 0f,
            new DimensionType.MonsterSettings(true, false, ConstantIntProvider.ZERO, 0)),
            (registry) ->
                    new DimensionOptions(
                            registry.get(RegistryKeys.DIMENSION_TYPE)
                                    .getOptional(RegistryKey.of(RegistryKeys.DIMENSION_TYPE, BackroomsRedux.modLoc("level_0")))
                                    .get(),
                            new Level0ChunkGenerator(
                                    new FixedBiomeSource(registry.get(RegistryKeys.BIOME).getOptional(BRBiomes.LEVEL_0).get()),
                                    NbtGroup.Builder.create(LEVEL_O_ID)
                                            .with("level_0_common", singleton.getPieces(BRPiecePools.LEVEL_0_COMMON))
                                            .with("level_0_uncommon", singleton.getPieces(BRPiecePools.LEVEL_0_UNCOMMON))
                                            .with("level_0_rare", singleton.getPieces(BRPiecePools.LEVEL_0_RARE))
                                            .with("level_0_manilla_room")
                                            .build())));

    public static final RegistryKey<World> LEVEL_0_WORLD = RegistryKey.of(RegistryKeys.WORLD, LEVEL_O_ID);

    public static final SoundEffects LEVEL_0_EFFECTS = new SoundEffects(
            Optional.of(new StaticReverbEffect.Builder().setDecayTime(5.0f).build()),
            Optional.empty(),
            Optional.of(new MusicSound(RegistryEntry.of(BRSoundEvents.LIGHT_HUM), 0, 0, true)));

    public static final Skybox LEVEL_0_SKYBOX = new EmptySkybox();

    @Override
    public void registerHooks() {
        Registry.register(LimlibWorld.LIMLIB_WORLD, LEVEL_O_ID, LEVEL_0);

        LimlibRegistryHooks.hook(SoundEffects.SOUND_EFFECTS_KEY, (infoLookup, registryKey, registry) -> {
            registry.add(RegistryKey.of(SoundEffects.SOUND_EFFECTS_KEY, LEVEL_O_ID),
                    LEVEL_0_EFFECTS,
                    Lifecycle.stable());
        });

        LimlibRegistryHooks.hook(Skybox.SKYBOX_KEY, ((infoLookup, registryKey, registry) -> {
            registry.add(RegistryKey.of(Skybox.SKYBOX_KEY, LEVEL_O_ID),
                    LEVEL_0_SKYBOX,
                    Lifecycle.stable());
        }));

        LimlibRegistryHooks.hook(RegistryKeys.BIOME, ((infoLookup, registryKey, registry) -> {
            RegistryEntryLookup<PlacedFeature> features = infoLookup.getRegistryInfo(RegistryKeys.PLACED_FEATURE).get().entryLookup();
            RegistryEntryLookup<ConfiguredCarver<?>> carvers = infoLookup.getRegistryInfo(RegistryKeys.CONFIGURED_CARVER).get().entryLookup();

            registry.add(BRBiomes.LEVEL_0, Level0Biome.create(features, carvers), Lifecycle.stable());
        }));
    }
}
