package keno.backrooms_redux.networking;

import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.client.BRClient;
import keno.backrooms_redux.client.screens.NoclipWarningScreen;
import keno.backrooms_redux.server.BRServer;
import net.minecraft.util.Identifier;

public final class BRPackets {
    //Client-to-server packet identifiers
    /** {@link Identifier} used to send a packet to teleport the player to the backrooms, more details in {@link BRServer}*/
    public static final Identifier TELEPORT_PLAYER_TO_BACKROOMS = BackroomsRedux.modLoc("teleport_player_to_backrooms");
    //Server-to-client packet identifiers
    /** {@link Identifier} used to send a packet that opens the noclip warning screen, more details in {@link BRClient} and {@link NoclipWarningScreen}*/
    public static final Identifier OPEN_NOCLIP_WARNING_SCREEN = BackroomsRedux.modLoc("open_noclip_screen");
}
