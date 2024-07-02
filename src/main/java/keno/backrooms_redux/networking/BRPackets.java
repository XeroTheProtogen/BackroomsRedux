package keno.backrooms_redux.networking;

import keno.backrooms_redux.BackroomsRedux;
import net.minecraft.util.Identifier;

public class BRPackets {
    //Client-to-server packet identifiers
    public static final Identifier TELEPORT_PLAYER_TO_BACKROOMS = BackroomsRedux.modLoc("teleport_player_to_backrooms");
    //Server-to-client packet identifiers
    public static final Identifier OPEN_NOCLIP_WARNING_SCREEN = BackroomsRedux.modLoc("open_noclip_screen");
}
