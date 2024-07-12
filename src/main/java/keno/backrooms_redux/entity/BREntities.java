package keno.backrooms_redux.entity;

import keno.backrooms_redux.BackroomsRedux;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class BREntities {
    public static final EntityType<HallucinationEntity> HALLUCINATION =
            Registry.register(Registries.ENTITY_TYPE, BackroomsRedux.modLoc("hallucination"),
                    EntityType.Builder.create(HallucinationEntity::new, SpawnGroup.MISC)
                            .setDimensions(0.8f, 2f).build());

    public static void init() {}
}
