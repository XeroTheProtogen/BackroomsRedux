package keno.backrooms_redux.client.renderer.entity;

import keno.backrooms_redux.client.renderer.entity.model.HallucinationEntityModel;
import keno.backrooms_redux.components.BRComponentRegistry;
import keno.backrooms_redux.components.sanity.SanityComponent;
import keno.backrooms_redux.entity.HallucinationEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class HallucinationEntityRenderer extends GeoEntityRenderer<HallucinationEntity> {
    MinecraftClient client = MinecraftClient.getInstance();
    
    public HallucinationEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new HallucinationEntityModel());
    }

    @Override
    public Identifier getTextureLocation(HallucinationEntity animatable) {
        return MinecraftClient.getInstance().player.getSkinTextures().texture();
    }

    @Override
    public boolean shouldRender(HallucinationEntity entity, Frustum frustum, double x, double y, double z) {
        return super.shouldRender(entity, frustum, x, y, z);
    }

    @Override
    public double getNameRenderCutoffDistance(HallucinationEntity animatable) {
        ClientPlayerEntity player = client.player;
        SanityComponent sanity = BRComponentRegistry.SANITY.get(player);
        return sanity.getValue() > 60d ? 0d : super.getNameRenderCutoffDistance(animatable);
    }

    @Override
    public void renderChildBones(MatrixStack poseStack, HallucinationEntity animatable, GeoBone bone, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!bone.isHidden() && BRComponentRegistry.SANITY.get(client.player).getValue() > 60f) {
            bone.setHidden(true);
            bone.setChildrenHidden(true);
        } else if (BRComponentRegistry.SANITY.get(client.player).getValue() <= 60f && bone.isHidden()) {
            bone.setHidden(false);
            bone.setChildrenHidden(false);
        }
        super.renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
