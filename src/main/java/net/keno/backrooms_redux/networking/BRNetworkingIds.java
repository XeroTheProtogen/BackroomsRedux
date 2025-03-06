package net.keno.backrooms_redux.networking;

import net.keno.backrooms_redux.BackroomsRedux;
import net.minecraft.network.packet.CustomPayload;

public class BRNetworkingIds {
    public static final CustomPayload.Id<NoClipPayload> NO_CLIP_ID =
            new CustomPayload.Id<>(BackroomsRedux.modLoc("no_clip"));
}
