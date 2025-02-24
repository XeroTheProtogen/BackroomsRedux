package net.keno.backrooms_redux.worldgen.chunk.levels;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.keno.backrooms_redux.BackroomsRedux;
import net.keno.backrooms_redux.blocks.LampBlock;
import net.keno.backrooms_redux.listeners.HeardData;
import net.keno.backrooms_redux.registry.BRCommon;
import net.keno.backrooms_redux.worldgen.chunk.SimpleDynamicChunkGenerator;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Level0ChunkGenerator extends SimpleDynamicChunkGenerator {
    public static final Identifier LEVEL_0 = BackroomsRedux.modLoc("level_0");

    private static final NbtGroup STARTING_GROUP = NbtGroup
            .Builder.create(BackroomsRedux.modLoc("level_0"))
            .with("level_0_common", 0, 3).build();

    public static final MapCodec<Level0ChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BiomeSource.CODEC.stable().fieldOf("biome_source").forGetter(Level0ChunkGenerator::getBiomeSource)
    ).apply(instance, instance.stable(Level0ChunkGenerator::new)));

    public Level0ChunkGenerator(BiomeSource biomeSource) {
        super(biomeSource, STARTING_GROUP);
    }

    @Override
    public NbtGroup getGroup() {
        return HeardData.getLevelPool(LEVEL_0).convertToGroup();
    }

    @Override
    public int getPlacementRadius() {
        return 0;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkGenerationContext context, BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk) {
        BlockPos pos = chunkRegion.getCenterPos().getStartPos();
        Random random = Random.create(LimlibHelper.blockSeed(pos));

        generateNbt(chunkRegion, pos, getDynamicGroup().pick("level_0_common",
                random), Manipulation.random(random));
        return CompletableFuture.completedFuture(chunk);
    }

    private boolean lightPlacement(BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        int y = pos.getY();
        float remainderX = x % 8;
        float remainderZ = z % 9;

        if ((remainderX >= -1 && remainderX <= 1) && (remainderZ >= -1 && remainderZ <= 1)) {
            float remainderY = y % 5;
            return remainderY >= 0 && remainderY <= 1;
        } else {
            return false;
        }
    }

    @Override
    protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt) {
        if (state.isAir() || state.isOf(Blocks.LIGHT)) {
            return;
        }

        super.modifyStructure(region, pos, state, blockEntityNbt);

        Random random = Random.create(LimlibHelper.blockSeed(pos));

        if (state.isOf(BRCommon.ROOF_TILE)) {
            if (lightPlacement(pos)) {
                region.setBlockState(pos, BRCommon.ROOF_LIGHT.getDefaultState().with(LampBlock.LIT, random.nextBetween(1, 10) <= 7), Block.NOTIFY_ALL);
            }
        }

        if (state.isOf(Blocks.WHITE_WOOL)) {
            region.setBlockState(pos, BRCommon.ROOF_TILE.getDefaultState(), Block.NOTIFY_ALL);
        }
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public int getWorldHeight() {
        return 0;
    }

    @Override
    public void appendDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }
}
