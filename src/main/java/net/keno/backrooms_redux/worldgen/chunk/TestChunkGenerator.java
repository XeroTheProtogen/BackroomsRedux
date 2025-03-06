package net.keno.backrooms_redux.worldgen.chunk;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.data.listeners.HeardData;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerationContext;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/** A chunk generator used to test experimental API, enabled only in dev environments */
public class TestChunkGenerator extends AbstractNbtChunkGenerator implements DynamicNbtUpdater {
    private static final NbtGroup STARTING_GROUP = NbtGroup
            .Builder.create(BackroomsRedux.modLoc("test"))
            .with("common", "0").build();


    public static final MapCodec<TestChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter(chunkGen -> chunkGen.biomeSource),
                    NbtGroup.CODEC.fieldOf("nbt_group").forGetter(TestChunkGenerator::getDynamicGroup)
            ).apply(instance, instance.stable(TestChunkGenerator::new)));

    public TestChunkGenerator(BiomeSource biomeSource) {
        this(biomeSource, STARTING_GROUP);
    }

    public TestChunkGenerator(BiomeSource source, NbtGroup group) {
        super(source, group);
    }

    @Override
    public NbtGroup getDynamicGroup() {
        try {
            return HeardData.getLevelPool(BackroomsRedux.modLoc("test")).convertToGroup();
        } catch (Exception e) {
            return this.nbtGroup;
        }
    }

    @Override
    public int getPlacementRadius() {
        return 0;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkGenerationContext context, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        BlockPos pos = chunkRegion.getCenterPos().getStartPos();

        generateNbt(chunkRegion, pos, getDynamicGroup().pick("common", Random.create(LimlibHelper.blockSeed(pos))));
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public int getWorldHeight() {
        return 256;
    }

    @Override
    public int getMinimumY() {
        return -32;
    }

    @Override
    public void appendDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }
}
