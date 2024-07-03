package keno.backrooms_redux.client.screens;

import keno.backrooms_redux.networking.BRPackets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class NoclipWarningScreen extends Screen {
    private ButtonWidget button;

    public NoclipWarningScreen(Text title) {
        super(title);
    }


    @Override
    protected void init() {
        super.init();
        this.button = ButtonWidget.builder(Text.translatable("backrooms_redux.gui.agreement"), button -> {
            PacketByteBuf packet = PacketByteBufs.empty();
            this.client.setScreen(null);
            ClientPlayNetworking.send(BRPackets.TELEPORT_PLAYER_TO_BACKROOMS, packet);
        }).position(90, 90).dimensions(50, 70, 100, 120).build();
        this.addDrawableChild(button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        button.render(context, mouseX, mouseY, delta);
        if (button.isHovered()) {
            context.drawCenteredTextWithShadow(client.textRenderer,
                    Text.translatable("backrooms_redux.gui.omen"),
                    context.getScaledWindowWidth() / 2,
                    (context.getScaledWindowHeight() / 2) - 50, 0xffff6347);
        }
    }
}
