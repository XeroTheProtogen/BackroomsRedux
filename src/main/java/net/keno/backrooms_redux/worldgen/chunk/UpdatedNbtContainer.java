package net.keno.backrooms_redux.worldgen.chunk;

import net.ludocrypt.limlib.api.world.FunctionMap;
import net.ludocrypt.limlib.api.world.NbtGroup;
import net.ludocrypt.limlib.api.world.NbtPlacerUtil;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public interface UpdatedNbtContainer {
    NbtGroup getGroup();
    FunctionMap<Identifier, NbtPlacerUtil, ResourceManager> getStructures();
    void setGroup(NbtGroup group);
    void setStructures(FunctionMap<Identifier, NbtPlacerUtil, ResourceManager> structures);
}
