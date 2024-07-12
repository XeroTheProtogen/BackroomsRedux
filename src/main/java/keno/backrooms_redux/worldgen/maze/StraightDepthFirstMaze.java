package keno.backrooms_redux.worldgen.maze;

import com.google.common.collect.Lists;
import net.ludocrypt.limlib.api.world.maze.DepthLikeMaze;
import net.minecraft.nbt.NbtCompound;

import java.util.List;
import java.util.random.RandomGenerator;

/** @author Ludocrypt (The Corners) */
public class StraightDepthFirstMaze extends DepthLikeMaze {
    public final RandomGenerator generator;
    public final double bias;

    public StraightDepthFirstMaze(int width, int height, RandomGenerator generator, double bias) {
        super(width, height);
        this.generator = generator;
        this.bias = bias;
    }

    @Override
    public void create() {
        visit(new Vec2i(0, 0));
        this.visitedCells++;
        this.stack.push(new Vec2i(0, 0));

        while (visitedCells < this.width * this.height) {
            List<Face> neighbours = Lists.newArrayList();

            for (Face face : Face.values()) {
                if (this.hasNeighbour(this.stack.peek(), face)) {
                    neighbours.add(face);
                }
            }

            if (!neighbours.isEmpty()) {
                Face nextFace = neighbours.get(generator.nextInt(neighbours.size()));
                if (dir(this.stack.peek()) != null) {
                    if (neighbours.contains(dir(this.stack.peek()))) {
                        if (generator.nextDouble() < bias) {
                            nextFace = dir(this.stack.peek());
                        }
                    }

                }

                this.cellState(this.stack.peek()).go(nextFace);
                this.cellState(this.stack.peek().go(nextFace)).go(nextFace.mirror());
                this.visit(this.stack.peek().go(nextFace));
                this.stack.push(this.stack.peek().go(nextFace));

                this.visitedCells++;
            } else {
                this.stack.pop();
            }

        }
    }

    public NbtCompound dir(Vec2i vec, Face dir) {
        NbtCompound appendage = new NbtCompound();
        appendage.putByte("dir", (byte) dir.ordinal());
        cellState(vec).getExtra().put("dir", appendage);
        return appendage;
    }

    public Face dir(Vec2i vec) {
        return cellState(vec).getExtra().containsKey("dir")
                ? Face.values()[cellState(vec).getExtra().get("dir").getByte("dir")]
                : null;
    }
}
