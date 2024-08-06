package keno.backrooms_redux.client.renderer.entity.model;

import keno.backrooms_redux.BackroomsRedux;
import keno.backrooms_redux.entity.HallucinationEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

@Environment(EnvType.CLIENT)
public class HallucinationEntityModel extends GeoModel<HallucinationEntity> {
    MinecraftClient client = MinecraftClient.getInstance();
    private static final Identifier HALLUCINATION_WIDE = BackroomsRedux.modLoc("geo/hallucination/wide.geo.json");
    private static final Identifier HALLUCINATION_SLIM = BackroomsRedux.modLoc("geo/hallucination/slim.geo.json");

    @Override
    public Identifier getModelResource(HallucinationEntity animatable) {
        SkinTextures.Model modelType = client.player.getSkinTextures().model();
        return modelType == SkinTextures.Model.SLIM ? HALLUCINATION_SLIM : HALLUCINATION_WIDE;
    }

    @Override
    public Identifier getTextureResource(HallucinationEntity animatable) {
        return client.player.getSkinTextures().texture();
    }

    @Override
    public void setCustomAnimations(HallucinationEntity animatable, long instanceId, AnimationState<HallucinationEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone head = this.getBone("head").get();
        if (head != null) {
            EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(data.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(data.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }

    @Override
    public Identifier getAnimationResource(HallucinationEntity animatable) {
        return BackroomsRedux.modLoc("animations/hallucination.animation.json");
    }
}
