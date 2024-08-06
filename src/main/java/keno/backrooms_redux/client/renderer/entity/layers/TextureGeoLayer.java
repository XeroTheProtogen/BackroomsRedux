package keno.backrooms_redux.client.renderer.entity.layers;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class TextureGeoLayer<T extends GeoEntity> extends GeoRenderLayer<T> {
    private Identifier layerTexture;
    public TextureGeoLayer(GeoRenderer<T> entityRendererIn, Identifier texture) {
        super(entityRendererIn);
        this.layerTexture = texture;
    }

    protected void setLayerTexture(Identifier identifier) {
        this.layerTexture = identifier;
    }

    @Override
    public void render(MatrixStack poseStack, T animatable, BakedGeoModel bakedModel, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderLayer armorRenderType = RenderLayer.getArmorCutoutNoCull(layerTexture);

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack,
                bufferSource, animatable, armorRenderType, bufferSource.getBuffer(armorRenderType),
                partialTick, packedLight, OverlayTexture.field_32953, 1, 1, 1, 1);
    }
}
