package keno.backrooms_redux.block;

import keno.backrooms_redux.networking.BRPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OverworldGlitchingBlock extends Block {
    public OverworldGlitchingBlock(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) return super.onUse(state, world, pos, player, hand, hit);

        if (player instanceof ServerPlayerEntity serverPlayer) {
            PacketByteBuf packet = PacketByteBufs.create();
            packet.writeBlockPos(serverPlayer.getBlockPos());

            ServerPlayNetworking.send(serverPlayer, BRPackets.OPEN_NOCLIP_WARNING_SCREEN, packet);
        }
        return ActionResult.success(true);
    }
}
