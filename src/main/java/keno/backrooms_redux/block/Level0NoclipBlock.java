package keno.backrooms_redux.block;

import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class Level0NoclipBlock extends AbstractNoclipBlock{
    public Level0NoclipBlock(Settings settings) {
        super(settings, new Pair<>(World.NETHER, 50), 3);
    }
}
