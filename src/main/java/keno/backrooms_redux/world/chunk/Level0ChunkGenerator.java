package keno.backrooms_redux.world.chunk;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import keno.backrooms_redux.registry.BRCommonRegistry;
import keno.backrooms_redux.registry.BRRegistrar;
import keno.backrooms_redux.utils.RngUtils;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.Optional;
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
                .with("level_0_common", 1, 4) // COMMON PIECES
                .with("level_0_uncommon", 1, 4) // UNCOMMON PIECES
                // .with("level_0_rare", 1, 2) // RARE PIECES
                .with("level_0_manilla_room") // MANILLA ROOM PIECE
                .with("level_0_test") // PLACEHOLDER STRUCTURE
                .build();
    }

    @Override
    public int getPlacementRadius() {
        return 0;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk) {
        BlockPos pos = chunk.getPos().getStartPos();
        Random random = RngUtils.getFromPos(chunkRegion, chunk, pos);
        //If the generated float number is larger than 0.4, place a common piece
        if (random.nextFloat() > 0.4) {
            generateNbt(chunkRegion, pos, nbtGroup.pick("level_0_common", random), Manipulation.random(random));
        } else {
            //Else, determine if to generate uncommon or rare pieces (level_0_test = placeholder)
            if (random.nextFloat() > 0.3) {
                generateNbt(chunkRegion, pos, nbtGroup.pick("level_0_uncommon", random), Manipulation.random(random));
            } else {
                if (random.nextBoolean()) {
                    generateNbt(chunkRegion, pos, nbtGroup.pick("level_0_test", random));
                } else {
                    generateNbt(chunkRegion, pos, nbtGroup.pick("level_0_manilla_room", random), Manipulation.random(random));
                }
            }
        }
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
    protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt) {
        super.modifyStructure(region, pos, state, blockEntityNbt);
        Random random = RngUtils.getFromPos(region, region.getChunk(pos), pos);

        //Randomly swap moist carpet for soggy carpet
        if (state.isOf(BRCommonRegistry.MOIST_CARPET)) {
            if (random.nextDouble() <= 0.05) {
                region.setBlockState(pos, BRCommonRegistry.SOGGY_CARPET.getDefaultState(), Block.NOTIFY_ALL, 1);
            }
        }

        if (state.isOf(BRCommonRegistry.MOIST_CARPET_STAIRS)) {
            if (random.nextDouble() < 0.1) {
                region.setBlockState(pos, BRCommonRegistry.SOGGY_CARPET_STAIRS.getStateWithProperties(state), Block.NOTIFY_ALL, 1);
            }
        }

        //WIP If either vector in the roof block position is a multiple of 4, replace with lighting
        //White wool is treated as a placeholder
        if (state.isOf(Blocks.WHITE_WOOL)) {
            if (pos.getX() % 4 == 0 && pos.getZ() % 4 == 0) {
                region.setBlockState(pos, Blocks.SEA_LANTERN.getDefaultState(), Block.NOTIFY_ALL, 1);
            } else {
                region.setBlockState(pos, BRCommonRegistry.ROOF_TILE.getDefaultState(), Block.NOTIFY_ALL, 1);
            }
        }
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }
}
