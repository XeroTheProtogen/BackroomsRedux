package keno.backrooms_redux.client.debug;

import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.sanity.SanityComponent;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class SanityPercentageDisplayCallback implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        assert player != null;
        if (BRComponentRegistry.SANITY.isProvidedBy(player)) {
            SanityComponent sanity = BRComponentRegistry.SANITY.get(player);
            float currentSanity = sanity.getValue();
            drawContext.drawText(client.textRenderer, Text.of("Sanity: %.2f".formatted(currentSanity)),
                    (drawContext.getScaledWindowWidth() / 2) - 80, drawContext.getScaledWindowHeight() / 2,
                    0xffff6347, false);
        }
    }
}
