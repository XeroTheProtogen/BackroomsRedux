package keno.backrooms_redux.client.renderer.entity.model;

import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.entity.HallucinationEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class HallucinationEntityModel extends GeoModel<HallucinationEntity> {
    @Override
    public Identifier getModelResource(HallucinationEntity animatable) {
        return BackroomsRedux.modLoc("geo/hallucination.geo.json");
    }

    @Override
    public Identifier getTextureResource(HallucinationEntity animatable) {
        return MinecraftClient.getInstance().player.getSkinTextures().texture();
    }

    @Override
    public void setCustomAnimations(HallucinationEntity animatable, long instanceId, AnimationState<HallucinationEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone head = this.getBone("head").get();

        if (head != null) {
            EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(data.headPitch() * MathHelper.DEGREES_PER_RADIAN);
            head.setRotY(data.netHeadYaw() * MathHelper.DEGREES_PER_RADIAN);
        }
    }

    @Override
    public Identifier getAnimationResource(HallucinationEntity animatable) {
        return BackroomsRedux.modLoc("animations/hallucination.animation.json");
    }
}
