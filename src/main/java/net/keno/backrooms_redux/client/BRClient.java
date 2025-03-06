package net.keno.backrooms_redux.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.keno.backrooms_redux.networking.NoClipPayload;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class BRClient implements ClientModInitializer {
    public static final String KEY_CATEGORY = "backrooms_redux.key";
    public static final KeyBinding NO_CLIP = KeyBindingHelper
            .registerKeyBinding(new KeyBinding("backrooms_redux.key.no_clip",
                    GLFW.GLFW_KEY_N, KEY_CATEGORY));
    public static int TIME_SINCE_KEY_PRESSED = 0;

    @Override
    public void onInitializeClient() {
        handleNetworking();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                if (--TIME_SINCE_KEY_PRESSED <= 0) {
                    if (NO_CLIP.isPressed()) {
                        TIME_SINCE_KEY_PRESSED = 20;
                        ClientPlayNetworking.send(new NoClipPayload());
                    }
                }
            }
        });
    }

    public static void handleNetworking() {
    }
}
