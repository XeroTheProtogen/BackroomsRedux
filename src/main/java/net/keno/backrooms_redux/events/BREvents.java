package net.keno.backrooms_redux.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BREvents {
    public static Event<OnNoClipCallback> ON_NO_CLIP_EVENT =
            EventFactory.createArrayBacked(OnNoClipCallback.class,
                    listeners -> (player, noClippingFrom, noClippingTo) -> {
                        for (OnNoClipCallback callback : listeners) {
                            ActionResult result = callback.onNoClipCallback(player, noClippingFrom, noClippingTo);
                            if (result != ActionResult.PASS) {
                                return result;
                            }
                        }

                        return ActionResult.PASS;
                    });

    public static Event<CanNoClipCallback> CAN_NO_CLIP_EVENT =
            EventFactory.createArrayBacked(CanNoClipCallback.class, listeners -> (player, noClippingFrom, noClippingTo) -> {
                        for (CanNoClipCallback callback : listeners) {
                            boolean canNoClip = callback.test(player, noClippingFrom, noClippingTo);
                            if (!canNoClip) {
                                return false;
                            }
                        }
                        return true;
                    });

    public interface OnNoClipCallback {
        ActionResult onNoClipCallback(PlayerEntity player, RegistryKey<World> noClippingFrom, RegistryKey<World> noClippingTo);
    }

    public interface CanNoClipCallback {
        boolean test(PlayerEntity player, RegistryKey<World> noClippingFrom, RegistryKey<World> noClippingTo);
    }
}
