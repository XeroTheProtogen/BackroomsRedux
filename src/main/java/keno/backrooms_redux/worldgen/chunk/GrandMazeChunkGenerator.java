package keno.backrooms_redux.worldgen.chunk;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import keno.backrooms_redux.worldgen.maze.GrandMazeGenerator;
import keno.backrooms_redux.worldgen.maze.StraightDepthFirstMaze;
import net.ludocrypt.limlib.api.world.LimlibHelper;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.ludocrypt.limlib.api.world.maze.*;
import net.ludocrypt.limlib.api.world.maze.MazeComponent.CellState;
import net.ludocrypt.limlib.api.world.maze.MazeComponent.Vec2i;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.random.RandomGenerator;

/** Since grand mazes appear to end up sharing a degree of logic,
    this class was made to minimize boiler plate.
    (Derived from the chunk generators in LudoCrypt's The Corners)*/
public class GrandMazeChunkGenerator extends AbstractNbtChunkGenerator {
    public static final Codec<GrandMazeChunkGenerator> CODEC =
            RecordCodecBuilder.create((instance) ->
                    instance.group(BiomeSource.CODEC.fieldOf("biome_source").stable().forGetter((chunkGenerator) -> chunkGenerator.biomeSource),
            NbtGroup.CODEC.fieldOf("group").stable().forGetter((chunkGenerator) -> chunkGenerator.nbtGroup),
                    Codec.INT.fieldOf("maze_width").stable().forGetter((chunkGenerator) -> chunkGenerator.mazeWidth),
                    Codec.INT.fieldOf("maze_height").stable().forGetter((chunkGenerator) -> chunkGenerator.mazeHeight),
                    Codec.INT.fieldOf("maze_dilation").stable().forGetter((chunkGenerator) -> chunkGenerator.mazeDilation),
                    Codec.LONG.fieldOf("seed_modifier").stable().forGetter((chunkGenerator) -> chunkGenerator.mazeSeedModifier))
                    .apply(instance, instance.stable(GrandMazeChunkGenerator::new)));

    private final int mazeWidth;
    private final int mazeHeight;
    private final int mazeDilation;
    private final long mazeSeedModifier;
    private GrandMazeGenerator mazeGenerator;

    public GrandMazeChunkGenerator(BiomeSource biomeSource, NbtGroup nbtGroup, int mazeWidth, int mazeHeight,
                                   int mazeDilation, long mazeSeedModifier) {
        super(biomeSource, nbtGroup);
        this.mazeWidth = mazeWidth;
        this.mazeHeight = mazeHeight;
        this.mazeDilation = mazeDilation;
        this.mazeSeedModifier = mazeSeedModifier;
        this.mazeGenerator = new GrandMazeGenerator(mazeWidth, mazeHeight, mazeDilation, mazeSeedModifier);
    }

    @Override
    public int getPlacementRadius() {
        return 3;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(ChunkRegion chunkRegion, ChunkStatus targetStatus, Executor executor, ServerWorld world, ChunkGenerator generator, StructureTemplateManager structureTemplateManager, ServerLightingProvider lightingProvider, Function<Chunk, CompletableFuture<Either<Chunk, ChunkHolder.Unloaded>>> fullChunkConverter, List<Chunk> chunks, Chunk chunk) {
        BlockPos pos = chunk.getPos().getStartPos();
        this.mazeGenerator.generateMaze(new Vec2i(pos.getX(), pos.getZ()), chunkRegion, this::newGrandMaze, this::decorateMazeCell);
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public int getWorldHeight() {
        return 0;
    }

    public MazeComponent newGrandMaze(ChunkRegion region, Vec2i mazePos, int width, int height, Random random) {
        // Find the position of the grandMaze that contains the current maze
        BlockPos grandMazePos = new BlockPos(mazePos.getX() - Math
                .floorMod(mazePos.getX(), (mazeGenerator.width * mazeGenerator.width * mazeGenerator.thicknessX)), 0,
                mazePos.getY() - Math.floorMod(mazePos.getY(),
                                (mazeGenerator.height * mazeGenerator.height * mazeGenerator.thicknessY)));

        // Check if the grandMaze was already generated, if not generate it
        MazeComponent grandMaze;

        if (mazeGenerator.grandMazeMap.containsKey(grandMazePos)) {
            grandMaze = mazeGenerator.grandMazeMap.get(grandMazePos);
        } else {
            grandMaze = new DepthFirstMaze(mazeGenerator.width / mazeGenerator.dilation,
                    mazeGenerator.height / mazeGenerator.dilation,
                    Random.create(LimlibHelper.blockSeed(grandMazePos.getX(), mazeGenerator.seedModifier, grandMazePos.getZ())));
            grandMaze.generateMaze();
            mazeGenerator.grandMazeMap.put(grandMazePos, grandMaze);
        }

        // Get the cell of the grandMaze that corresponds to the current maze
        CellState originCell = grandMaze
                .cellState((((mazePos.getX() - grandMazePos
                                .getX()) / mazeGenerator.thicknessX) / mazeGenerator.width) / mazeGenerator.dilation,
                        (((mazePos.getY() - grandMazePos
                                .getZ()) / mazeGenerator.thicknessY) / height) / mazeGenerator.dilation);
        Vec2i start = null;
        List<Vec2i> endings = Lists.newArrayList();

        // Check if the origin cell has an opening in the south, or it's on the edge of
        // the grandMaze, if so set the starting point to the middle of that side, if it
        // has not been set.
        if (originCell.goesDown() || originCell.getPosition().getX() == 0) {

            if (start == null) {
                start = new Vec2i(0, (mazeGenerator.height / mazeGenerator.dilation) / 2);
            }

        }

        // Check if the origin cell has an opening in the west, or it's on the edge of
        // the grandMaze, if so set the starting point to the middle of that side, if it
        // has not been set.
        if (originCell.goesLeft() || originCell.getPosition().getY() == 0) {

            if (start == null) {
                start = new Vec2i((mazeGenerator.width / mazeGenerator.dilation) / 2, 0);
            } else {
                endings.add(new Vec2i((mazeGenerator.width / mazeGenerator.dilation) / 2, 0));
            }

        }

        // Check if the origin cell has an opening in the north, or it's on the edge of
        // the grandMaze, if so set the starting point to the middle of that side, if it
        // has not been set. Else add an ending point to the middle of that side.
        if (originCell
                .goesUp() || originCell.getPosition().getX() == (mazeGenerator.width / mazeGenerator.dilation) - 1) {

            if (start == null) {
                start = new Vec2i((mazeGenerator.width / mazeGenerator.dilation) - 1,
                        (mazeGenerator.height / mazeGenerator.dilation) / 2);
            } else {
                endings
                        .add(new Vec2i((mazeGenerator.width / mazeGenerator.dilation) - 1,
                                (mazeGenerator.height / mazeGenerator.dilation) / 2));
            }

        }

        // Check if the origin cell has an opening in the east, or it's on the edge of
        // the grandMaze, if so set the starting point to the middle of that side, if it
        // has not been set. Else add an ending point to the middle of that side.
        if (originCell.goesRight() || originCell
                .getPosition()
                .getY() == (mazeGenerator.height / mazeGenerator.dilation) - 1) {

            if (start == null) {
                start = new Vec2i((mazeGenerator.width / mazeGenerator.dilation) / 2,
                        (mazeGenerator.height / mazeGenerator.dilation) - 1);
            } else {
                endings
                        .add(new Vec2i((mazeGenerator.width / mazeGenerator.dilation) / 2,
                                (mazeGenerator.height / mazeGenerator.dilation) - 1));
            }

        }

        // If the origin cell is a dead end, add a random ending point in the middle of
        // the maze. This ensures there is always somewhere to go in a dead end.
        if (endings.isEmpty()) {
            endings
                    .add(new Vec2i(random.nextInt((mazeGenerator.width / mazeGenerator.dilation) - 2) + 1,
                            random.nextInt((mazeGenerator.height / mazeGenerator.dilation) - 2) + 1));
        }

        // Create a new maze.
        MazeComponent mazeToSolve = new StraightDepthFirstMaze(mazeGenerator.width / mazeGenerator.dilation,
                mazeGenerator.height / mazeGenerator.dilation, (RandomGenerator) random, 0.45D);
        mazeToSolve.generateMaze();
        // Create a maze solver and solve the maze using the starting point and ending
        // points.
        MazeComponent solvedMaze = new DepthFirstMazeSolver(mazeToSolve, random, start, endings.toArray(new Vec2i[0]));
        solvedMaze.generateMaze();
        // Create a scaled maze using the dilation.
        MazeComponent dilatedMaze = new DilateMaze(solvedMaze, mazeGenerator.dilation);
        dilatedMaze.generateMaze();
        Vec2i starting = new Vec2i(random.nextInt((dilatedMaze.width / 2) - 2) + 1,
                random.nextInt((dilatedMaze.height / 2) - 2) + 1);
        Vec2i ending = new Vec2i(random.nextInt((dilatedMaze.width / 2) - 2) + 1,
                random.nextInt((dilatedMaze.height / 2) - 2) + 1);
        // Make a new maze
        MazeComponent overlayMaze = new StraightDepthFirstMaze(dilatedMaze.width / 2, dilatedMaze.height / 2,
                (RandomGenerator) random, 0.7D);
        overlayMaze.generateMaze();
        // Find a path along two random points
        MazeComponent solvedOverlay = new DepthFirstMazeSolver(overlayMaze, random, starting, ending);
        solvedOverlay.generateMaze();
        // Make it bigger
        MazeComponent dilatedOverlay = new DilateMaze(solvedOverlay, 2);
        dilatedOverlay.generateMaze();
        // Combine the two
        CombineMaze combinedMaze = new CombineMaze(dilatedMaze, dilatedOverlay);
        combinedMaze.generateMaze();
        return combinedMaze;
    }

    public void decorateMazeCell(ChunkRegion region, Vec2i cellPos, Vec2i mazePos, MazeComponent component, CellState state,
                                 Vec2i thickness, Random random) {

    }
    
    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }
}
