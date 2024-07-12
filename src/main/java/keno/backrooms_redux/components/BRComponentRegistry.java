package keno.backrooms_redux.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.components.sanity.SanityComponent;
import keno.backrooms_redux.components.spawn_check.EnteredBackroomsComponent;

public class BRComponentRegistry implements EntityComponentInitializer {
    public static final ComponentKey<SanityComponent> SANITY
            = ComponentRegistry.getOrCreate(BackroomsRedux.modLoc("sanity"), SanityComponent.class);
    public static final ComponentKey<EnteredBackroomsComponent> ENTERED_BACKROOMS
            = ComponentRegistry.getOrCreate(BackroomsRedux.modLoc("entered_backrooms"), EnteredBackroomsComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(SANITY, SanityComponent::new, RespawnCopyStrategy.NEVER_COPY);
        registry.registerForPlayers(ENTERED_BACKROOMS, EnteredBackroomsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
