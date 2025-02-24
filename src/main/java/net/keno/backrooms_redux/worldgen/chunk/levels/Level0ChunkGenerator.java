package net.keno.backrooms_redux.worldgen.chunk.levels;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.worldgen.chunk.SimpleDynamicChunkGenerator;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerationContext;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Level0ChunkGenerator extends SimpleDynamicChunkGenerator {
    public static final NbtGroup FALLBACK = NbtGroup.Builder
            .create(BackroomsRedux.modLoc("level_0"))
            .with("common", 0, 6)
            .with("uncommon", 0, 3)
            .with("rare", 0, 1)
            .with("manilla")
            .build();

    public static final MapCodec<Level0ChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BiomeSource.CODEC.stable().fieldOf("biome_source").forGetter(Level0ChunkGenerator::getBiomeSource)
    ).apply(instance, instance.stable(Level0ChunkGenerator::new)));

    public Level0ChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource, FALLBACK);
    }

    @Override
    protected NbtGroup getGroup() {
        return this.nbtGroup;
    }

    @Override
    public int getPlacementRadius() {
        return 0;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkGenerationContext context, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return null;
    }

    @Override
    public int getWorldHeight() {
        return 0;
    }

    @Override
    public void appendDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }
}
