package net.keno.backrooms_redux.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import org.jetbrains.annotations.Nullable;

public class LampBlock extends Block {
    public static BooleanProperty LIT = Properties.LIT;

    public LampBlock(Settings settings, int onLuminance, int offLuminance) {
        super(settings.luminance(blockState -> blockState.get(LIT) ? onLuminance : offLuminance));
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(LIT, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
