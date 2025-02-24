package net.keno.backrooms_redux.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import net.keno.backrooms_redux.worldgen.chunk.DynamicNbtUpdater;
import net.ludocrypt.limlib.api.world.chunk.AbstractNbtChunkGenerator;
import net.ludocrypt.limlib.api.world.chunk.LiminalChunkGenerator;
import net.minecraft.util.collection.BoundedRegionArray;
import net.minecraft.world.chunk.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("MixinAnnotationTarget")
@Mixin(value = ChunkGenerating.class, priority = 1500)
public abstract class ChunkGeneratingMixinSquared {
    @TargetHandler(mixin = "net.ludocrypt.limlib.impl.mixin.ChunkGeneratingMixin",
    name = "limlib$liminalChunkGenerator", prefix = "handler")
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "HEAD", shift = At.Shift.BY, by = 2))
    private static void backrooms_redux$cyclicUpdating(ChunkGenerationContext context, ChunkGenerationStep step,
                                                       BoundedRegionArray<AbstractChunkHolder> chunks, Chunk chunk,
                                                       CallbackInfoReturnable<CompletableFuture<Chunk>> originalCi, CallbackInfo ci) {
        if (context.generator() instanceof LiminalChunkGenerator limChunkGen) {
            if (limChunkGen instanceof AbstractNbtChunkGenerator nbtChunkGenerator) {
                if (nbtChunkGenerator instanceof DynamicNbtUpdater updater) {
                    updater.update(nbtChunkGenerator);
                }
            }
        }
    }
}
