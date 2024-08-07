package keno.backrooms_redux.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructurePool.class)
public interface StructurePoolAccessor {
    @Accessor("elements")
    ObjectArrayList<StructurePoolElement> backrooms_redux$getElements();
}
