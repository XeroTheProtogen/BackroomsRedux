package keno.backrooms_redux.mixin.client.player;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import keno.backrooms_redux.worldgen.biome.BRBiomes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(OtherClientPlayerEntity.class)
public abstract class OtherClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public OtherClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @ModifyReturnValue(method = "shouldRender", at = @At("RETURN"))
    public boolean backrooms_redux$shouldRender(boolean original) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        return !world.getBiome(this.getBlockPos()).isIn(BRBiomes.ISOLATING_BIOMES) && original;
    }
}
