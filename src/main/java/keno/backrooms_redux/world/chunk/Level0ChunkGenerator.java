package keno.backrooms_redux.world.chunk;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import keno.backrooms_redux.registry.BRRegistrar;
import keno.backrooms_redux.utils.RngUtils;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class Level0ChunkGenerator extends AbstractNbtChunkGenerator {
    public static final Codec<Level0ChunkGenerator> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter((chunkGenerator)
            -> chunkGenerator.biomeSource), NbtGroup.CODEC.fieldOf("nbt_group").stable().forGetter((chunkGenerator)
                    -> chunkGenerator.nbtGroup)).apply(instance, instance.stable(Level0ChunkGenerator::new)));

    public Level0ChunkGenerator(BiomeSource biomeSource) {
        this(biomeSource, createNbt());
    }

    public Level0ChunkGenerator(BiomeSource biomeSource, NbtGroup nbtGroup) {
        super(biomeSource, nbtGroup);
    }

    public static NbtGroup createNbt() {
        return NbtGroup.Builder.create(BRRegistrar.LEVEL_O_ID)
                .with("level_0_test")
                .build();
    }

    @Override
    public int getPlacementRadius() {
        return 0;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk) {
        BlockPos pos = chunk.getPos().getStartPos();
        generateNbt(chunkRegion, pos, nbtGroup.pick("level_0_test", RngUtils.getFromPos(chunkRegion, chunk, pos)));
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public int getWorldHeight() {
        return 80;
    }

    @Override
    public int getMinimumY() {
        return 0;
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }
}
