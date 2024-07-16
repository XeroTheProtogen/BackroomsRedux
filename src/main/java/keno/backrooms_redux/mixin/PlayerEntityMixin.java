package keno.backrooms_redux.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import keno.backrooms_redux.worldgen.biome.BRBiomes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyReturnValue(method = "shouldRenderName", at = @At("RETURN"))
    public boolean backrooms_redux$shouldRenderName(boolean original) {
        World world = this.getWorld();
        return !world.getBiome(getBlockPos()).isIn(BRBiomes.ISOLATING_BIOMES) && original;
    }
}
