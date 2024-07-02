package keno.backrooms_redux.client;

import keno.backrooms_redux.client.screens.NoclipWarningScreen;
import keno.backrooms_redux.networking.BRPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class BRClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerPacketReceivers();
    }

    public void registerPacketReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(BRPackets.OPEN_NOCLIP_WARNING_SCREEN, ((client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();

            client.execute(() ->
                    client.setScreen(new NoclipWarningScreen(Text.translatable("backrooms_redux.gui.noclip_warning"), pos)));
        }));
    }
}
