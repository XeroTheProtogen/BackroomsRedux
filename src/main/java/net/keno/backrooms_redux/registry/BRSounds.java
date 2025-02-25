package net.keno.backrooms_redux.registry;

import net.keno.backrooms_redux.BackroomsRedux;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;

import java.util.Optional;

public class BRSounds {
    public static final SoundEvent HUM_BUZZ
            = registerSound("level_0.light_hum_buzz",
            Optional.empty());

    public static final MusicSound HUM_BUZZ_MUSIC =
            new MusicSound(RegistryEntry.of(HUM_BUZZ), 0,
                    0, true);

    public static SoundEvent registerSound(String id, Optional<Float> fixedRange) {
        return Registry.register(Registries.SOUND_EVENT,
                BackroomsRedux.modLoc(id), new SoundEvent(BackroomsRedux.modLoc(id), fixedRange));
    }

    public static void init() {

    }
}
