package keno.backrooms_redux.worldgen.processors;

import keno.backrooms_redux.BackroomsRedux;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.processor.StructureProcessorList;

public final class BRProcessorLists {
    public static final RegistryKey<StructureProcessorList> DECAYING_HOUSE = RegistryKey.of(
            RegistryKeys.PROCESSOR_LIST, BackroomsRedux.modLoc("decaying_house"));
}
