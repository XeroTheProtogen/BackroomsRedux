package net.keno.backrooms_redux.data.holders.pos_determ;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import net.keno.backrooms_redux.BackroomsRedux;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record DeterminerType<T extends PosDeterminer>(MapCodec<T> codec) {
    public static final Registry<DeterminerType<?>> REGISTRY = new SimpleRegistry<>(RegistryKey.ofRegistry(
            BackroomsRedux.modLoc("determiner_registry")), Lifecycle.stable());

    public static <D extends PosDeterminer> DeterminerType<D> register(Identifier id, MapCodec<D> codec) {
        return Registry.register(REGISTRY, id, new DeterminerType<>(codec));
    }
}
