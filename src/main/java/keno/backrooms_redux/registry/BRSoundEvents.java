package keno.backrooms_redux.registry;

import keno.backrooms_redux.BackroomsRedux;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class BRSoundEvents {
    public static SoundEvent LIGHT_HUM = registerSound(BackroomsRedux.modLoc("light_hum"));

    private static SoundEvent registerSound(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {}
}
