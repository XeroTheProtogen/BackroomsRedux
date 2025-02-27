package net.keno.backrooms_redux.mixin.client;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.keno.backrooms_redux.worldgen.BRBiomes;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(OtherClientPlayerEntity.class)
public abstract class OtherClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public OtherClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    public void backrooms_redux$hidePlayers(double distance, CallbackInfoReturnable<Boolean> cir) {
        if (getWorld().getBiome(getBlockPos()).isIn(BRBiomes.LONELY_BIOMES)) {
            cir.setReturnValue(false);
        }
    }
}
