package net.thejadeproject.ascension.shaders.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.entity.client.shaders.RiftEntity;
import org.joml.Matrix4f;

public class RiftRenderer extends EntityRenderer<RiftEntity> {


    public RiftRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(
            RiftEntity entity,
            float yaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light
    ) {
        poseStack.pushPose();

        // Face player
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.scale(1.2f, 3.0f, 1.0f);

        VertexConsumer vc = buffer.getBuffer(ModRenderShaderTypes.RIFT);
        Matrix4f mat = poseStack.last().pose();

        vc.addVertex(mat, -0.5f, 0f, 0f).setUv(0, 0);
        vc.addVertex(mat,  0.5f, 0f, 0f).setUv(1, 0);
        vc.addVertex(mat,  0.5f, 1f, 0f).setUv(1, 1);
        vc.addVertex(mat, -0.5f, 1f, 0f).setUv(0, 1);

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
    }




    @Override
    public ResourceLocation getTextureLocation(RiftEntity riftEntity) {
        return null;
    }
}
