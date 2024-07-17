package keno.backrooms_redux.worldgen.chunk;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.utils.BRLootTables;
import keno.backrooms_redux.utils.RngUtils;
import keno.backrooms_redux.worldgen.piece_pools.PieceManager;
import keno.backrooms_redux.worldgen.piece_pools.constants.BRPieceManagers;
import net.ludocrypt.limlib.api.world.Manipulation;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
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
            -> chunkGenerator.biomeSource),
                    NbtGroup.CODEC.fieldOf("nbt_group").stable().forGetter((chunkGenerator)
                            -> chunkGenerator.nbtGroup)).apply(instance, instance.stable(Level0ChunkGenerator::new)));
    private double woolType;
    private final PieceManager manager = BackroomsRedux.singleton.getManager(BRPieceManagers.LEVEL_0_MANAGER);

    public Level0ChunkGenerator(BiomeSource biomeSource, NbtGroup nbtGroup) {
        super(biomeSource, nbtGroup);
    }

    /* private static NbtGroup createNbt() {
        PoolArraysSingleton singleton = PoolArraysSingleton.getInstance();
        return NbtGroup.Builder.create(BackroomsRedux.modLoc("level_0"))
                .with("level_0_common", singleton.getPieces("level_0_common"))
                .with("level_0_uncommon", singleton.getPieces("level_0_uncommon"))
                .with("level_0_rare", singleton.getPieces("level_0_rare"))
                .with("level_0_manilla_room")
                .build();
    } */



    @Override
    public int getPlacementRadius() {
        return 1;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk) {
        BlockPos pos = chunk.getPos().getStartPos();
        Random random = RngUtils.getFromPos(chunkRegion, chunk, pos);
        woolType = random.nextDouble();
        //If the generated float number is larger than 0.4, place a common piece
        if (random.nextFloat() > 0.4) {
            generateNbt(chunkRegion, pos, nbtGroup.pick("level_0_common", random), Manipulation.random(random));
        } else {
            //Else, determine if to generate uncommon or rare pieces (level_0_test = placeholder)
            if (random.nextFloat() > 0.3) {
                generateNbt(chunkRegion, pos, nbtGroup.pick("level_0_uncommon", random), Manipulation.random(random));
            } else {
                if (random.nextFloat() > 0.3) {
                    if (random.nextFloat() <= 0.05) {
                        Identifier identifier = nbtGroup.pick("level_0_rare", random);
                        if (identifier.compareTo(BackroomsRedux.modLoc("structures/nbt/level_0/level_0_rare/level_0_chasm.nbt")) == 0) {
                            generateNbt(chunkRegion, pos.down(15), identifier, Manipulation.random(random));
                        } else {
                            generateNbt(chunkRegion, pos, identifier, Manipulation.random(random));
                        }
                    } else {
                        generateNbt(chunkRegion, pos, nbtGroup.pick("level_0_uncommon", random), Manipulation.random(random));
                    }
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
        return 64;
    }

    @Override
    protected void modifyStructure(ChunkRegion region, BlockPos pos, BlockState state, Optional<NbtCompound> blockEntityNbt) {
        super.modifyStructure(region, pos, state, blockEntityNbt);
        Random random = RngUtils.getFromPos(region, region.getChunk(pos), pos);
        for (PieceManager.PieceProcessor<ChunkRegion, BlockPos, BlockState,
                Optional<NbtCompound>, Random> processor : this.manager.getProcessors()) {
            processor.applyProcessor(region, pos, state, blockEntityNbt, random);
        }

        region.getChunk(pos).forEachBlockMatchingPredicate(blockState -> blockState.isOf(Blocks.BROWN_WOOL),
                (pos1, blockState) -> {
                    if (this.woolType <= 0.25) {
                        region.setBlockState(pos1, Blocks.WHITE_WOOL.getDefaultState(), Block.NOTIFY_ALL, 1);
                    } else if (this.woolType > 0.25 && this.woolType <= 0.5) {
                        region.setBlockState(pos1, Blocks.BLUE_WOOL.getDefaultState(), Block.NOTIFY_ALL, 1);
                    } else if (this.woolType > 0.5 && this.woolType <= 0.75) {
                        region.setBlockState(pos1, Blocks.RED_WOOL.getDefaultState(), Block.NOTIFY_ALL, 1);
                    }});
    }

    @Override
    protected Identifier getContainerLootTable(LootableContainerBlockEntity container) {
        if (container.getCachedState().isOf(Blocks.BARREL)) {
            return BRLootTables.LEVEL_0_BARREL;
        }
        return LootTables.EMPTY;
    }

    @Override
    public int getMinimumY() {
        return -64;
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }
}
