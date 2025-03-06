package net.keno.backrooms_redux.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record NoClipPayload() implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, NoClipPayload> CODEC = PacketCodec
            .unit(new NoClipPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return BRNetworkingIds.NO_CLIP_ID;
    }
}
