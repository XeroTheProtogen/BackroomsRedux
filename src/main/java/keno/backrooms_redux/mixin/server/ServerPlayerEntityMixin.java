package keno.backrooms_redux.mixin.server;

import com.mojang.authlib.GameProfile;
import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.spawn_check.EnteredBackroomsComponent;
import keno.backrooms_redux.worldgen.biome.BRBiomes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Unique
    private EnteredBackroomsComponent component = BRComponentRegistry.ENTERED_BACKROOMS.get((ServerPlayerEntity) (Object) this);

    @Shadow public abstract ServerWorld getServerWorld();

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void backrooms_redux$tick(CallbackInfo ci) {
        if (!this.component.getBool()) {
            if (this.getServerWorld().getBiome(this.getBlockPos()).isIn(BRBiomes.BACKROOMS_BIOMES)) {
                this.component.setBool(true);
            }
        }
    }
}
