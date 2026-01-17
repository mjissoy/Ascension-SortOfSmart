package net.thejadeproject.ascension.shaders.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.entity.custom.shaders.RiftEntity;

public class RiftRenderer extends EntityRenderer<RiftEntity> {
    private static final ResourceLocation DUMMY_TEXTURE = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/vfx/white.png");

    public RiftRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(RiftEntity entity, float yaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(1.5f, 3.0f, 1.0f);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(ModRenderTypes.RIFT);
        var pose = poseStack.last();

        // Render vertical quad with proper vertex data
        vertexConsumer.addVertex(pose, -0.5f, 0.0f, 0.0f)
                .setColor(255, 255, 255, 255)
                .setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0, 0, 1);

        vertexConsumer.addVertex(pose, 0.5f, 0.0f, 0.0f)
                .setColor(255, 255, 255, 255)
                .setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0, 0, 1);

        vertexConsumer.addVertex(pose, 0.5f, 1.0f, 0.0f)
                .setColor(255, 255, 255, 255)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0, 0, 1);

        vertexConsumer.addVertex(pose, -0.5f, 1.0f, 0.0f)
                .setColor(255, 255, 255, 255)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, 0, 0, 1);

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(RiftEntity entity) {
        return DUMMY_TEXTURE;
    }
}