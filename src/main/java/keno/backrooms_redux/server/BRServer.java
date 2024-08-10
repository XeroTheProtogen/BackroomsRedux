package keno.backrooms_redux.server;

import keno.backrooms_redux.networking.BRPackets;
import keno.backrooms_redux.networking.TeleportPlayer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class BRServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        ServerPlayNetworking.registerGlobalReceiver(BRPackets.TELEPORT_PLAYER_TO_BACKROOMS, (server, player, handler, buf, responseSender) ->
            server.execute(() -> TeleportPlayer.teleportPlayerToBackrooms(server, player)));
    }
}
