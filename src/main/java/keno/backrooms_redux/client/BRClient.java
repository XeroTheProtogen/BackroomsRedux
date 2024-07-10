package keno.backrooms_redux.client;

import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.client.debug.SanityPercentageDisplayCallback;
import keno.backrooms_redux.client.screens.NoclipWarningScreen;
import keno.backrooms_redux.networking.BRPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;

public class BRClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerPacketReceivers();
        if (BackroomsRedux.isDevelopmentEnv()) {
            HudRenderCallback.EVENT.register(new SanityPercentageDisplayCallback());
        }
    }

    public void registerPacketReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(BRPackets.OPEN_NOCLIP_WARNING_SCREEN, ((client, handler, buf, responseSender) ->
                client.execute(() ->
                        client.setScreen(new NoclipWarningScreen(Text.translatable("backrooms_redux.gui.noclip_warning"))))));
    }
}
