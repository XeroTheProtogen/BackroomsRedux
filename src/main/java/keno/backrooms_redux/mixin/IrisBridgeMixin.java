package keno.backrooms_redux.mixin;

import net.ludocrypt.limlib.impl.bridge.IrisBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(IrisBridge.class)
public abstract class IrisBridgeMixin {
    // This fixes a graphics bug that occurs when Iris Shaders is used.
    @ModifyArg(method = "areShadersInUse", at = @At(value = "INVOKE",
            target = "Ljava/lang/Class;forName(Ljava/lang/String;)Ljava/lang/Class;"), remap = false)
    private static String backrooms_redux$areShadersInUse(String className) {
        return "net.irisshaders.iris.apiimpl.IrisApiV0Impl";
    }
}
