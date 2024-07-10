package keno.backrooms_redux.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.components.sanity.SanityComponent;

public class BRComponentRegistry implements EntityComponentInitializer {
    public static final ComponentKey<SanityComponent> SANITY
            = ComponentRegistry.getOrCreate(BackroomsRedux.modLoc("sanity"), SanityComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(SANITY, SanityComponent::new, RespawnCopyStrategy.NEVER_COPY);
    }
}
