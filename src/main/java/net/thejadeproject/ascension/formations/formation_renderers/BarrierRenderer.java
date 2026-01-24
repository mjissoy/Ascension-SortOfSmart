package net.thejadeproject.ascension.formations.formation_renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.lucent.formation_arrays.api.formations.IFormationRenderer;
import net.lucent.formation_arrays.api.formations.node.IFormationNode;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.formations.formation_nodes.BarrierFormation;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class BarrierRenderer implements IFormationRenderer {
    @OnlyIn(Dist.CLIENT)
    public static final RenderType TRIANGLES = RenderType.create(
            "barrier_triangles",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.TRIANGLE_FAN,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.POSITION_COLOR_SHADER)
                    .setCullState(RenderType.NO_CULL)
                    .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .createCompositeState(false)
    );
    @OnlyIn(Dist.CLIENT)
    public static final RenderType QUADS = RenderType.create(
            "barrier_quads",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderType.POSITION_COLOR_SHADER)
                    .setCullState(RenderType.NO_CULL)
                    .setDepthTestState(RenderType.LEQUAL_DEPTH_TEST)
                    .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                    .createCompositeState(false)
    );
    public static final ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
           "textures/formations/barrier_formation_base.png");

    @OnlyIn(Dist.CLIENT)
    public void renderUVSphere(BarrierFormation node,Vec3 core,Vec3 camera,PoseStack poseStack, MultiBufferSource.BufferSource buffers){
        poseStack.pushPose();
        poseStack.translate(core.x - camera.x(), core.y - camera.y(),core.z  - camera.z());


        VertexConsumer consumer = buffers.getBuffer(QUADS);
        PoseStack.Pose pose = poseStack.last();

        int verticalSections = 32;
        int horizontalSections = 64;
           for (int i = 1; i < verticalSections - 1; i++) {
            double y0 = Math.cos(Math.PI * (double) i / (double) verticalSections);
            double y0Radius = node.BARRIER_RADIUS * Math.sin(Math.PI * (double) i / (double) verticalSections);
            double y1 = Math.cos(Math.PI * (double) (i + 1) / (double) verticalSections);
            double y1Radius = node.BARRIER_RADIUS * Math.sin(Math.PI * (double) (i + 1) / (double) verticalSections);
            for (int j = 0; j < horizontalSections; j++) {
                double x0Pos = Math.cos(2 * Math.PI * (double) (j) / (double) horizontalSections);
                double x1Pos = Math.cos(2 * Math.PI * (double) (j + 1) / (double) horizontalSections);
                double z0Pos = Math.sin(2 * Math.PI * (double) (j) / (double) horizontalSections);
                double z1Pos = Math.sin(2 * Math.PI * (double) (j + 1) / (double) horizontalSections);
                Vector3f[] vertices = new Vector3f[]{
                        new Vector3f((float) (x0Pos * y0Radius), (float) (y0 * node.BARRIER_RADIUS), (float) (z0Pos * y0Radius)),
                        new Vector3f((float) (x1Pos * y0Radius), (float) (y0 * node.BARRIER_RADIUS), (float) (z1Pos * y0Radius)),
                        new Vector3f((float) (x1Pos * y1Radius), (float) (y1 * node.BARRIER_RADIUS), (float) (z1Pos * y1Radius)),
                        new Vector3f((float) (x0Pos * y1Radius), (float) (y1 * node.BARRIER_RADIUS), (float) (z0Pos * y1Radius)),
                };
                for (var vertex : vertices) {

                     //Vector3f newVec = pose.pose().transformPosition(vertex);
                     consumer.addVertex(pose,vertex).setColor(node.BARRIER_COLOUR);
                }
            }
        }
        buffers.endBatch();

        consumer = buffers.getBuffer(TRIANGLES);
        Vector3f vec = new Vector3f(0,node.BARRIER_RADIUS,0);
        consumer.addVertex(pose,vec).setColor(node.BARRIER_COLOUR);
         double y0 = Math.cos(Math.PI / (double) verticalSections);
        double y0Radius = node.BARRIER_RADIUS * Math.sin(Math.PI / (double) verticalSections);
        for (int j = horizontalSections; j >= 0; j--) {
            double x0Pos = Math.cos(2 * Math.PI * (double) (j) / (double) horizontalSections);
            double z0Pos = Math.sin(2 * Math.PI * (double) (j) / (double) horizontalSections);
            consumer.addVertex(pose,new Vector3f((float) (x0Pos * y0Radius), (float) (y0 * node.BARRIER_RADIUS), (float) (z0Pos * y0Radius))).setColor(node.BARRIER_COLOUR);
         }
        buffers.endBatch();
        poseStack.popPose();
    }
    @OnlyIn(Dist.CLIENT)
    public void renderTexture(PoseStack poseStack,Vec3 core,Vec3 camera, MultiBufferSource.BufferSource  bufferSource,BarrierFormation formation,double time){
        poseStack.pushPose();

        // Move to world position relative to camera
        poseStack.translate(
                core.x - camera.x,
                core.y-0.4 - camera.y,
                core.z - camera.z
        );
        PoseStack.Pose pose = poseStack.last();

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(textureLocation));
        //float angle = (float) (Math.floorMod((int) time,100)/100 * 360);
        //poseStack.mulPose(Axis.YP.rotationDegrees(angle));

        Vector3f corner1 = new Vector3f(- 5,0,- 5);
        Vector3f corner2 = new Vector3f(- 5,0,5);
        Vector3f corner3 = new Vector3f( 5,0,5);
        Vector3f corner4 = new Vector3f(5,0,-5);
        int light = 0xF000F0;
        consumer.addVertex(pose,corner1).setUv(0,0).setNormal(pose, 0, 1, 0).setLight(light).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY);
        consumer.addVertex(pose,corner2).setUv(0,1).setNormal(pose, 0, 1, 0).setLight(light).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY);
        consumer.addVertex(pose,corner3).setUv(1,1).setNormal(pose, 0, 1, 0).setLight(light).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY);
        consumer.addVertex(pose,corner4).setUv(1,0).setNormal(pose, 0, 1, 0).setLight(light).setColor(255, 255, 255, 255).setOverlay(OverlayTexture.NO_OVERLAY);


        bufferSource.endBatch();
        poseStack.popPose();
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(RenderLevelStageEvent event, BlockPos core, IFormationNode node) {
        if(node instanceof BarrierFormation barrierNode){
            if(barrierNode.isDestroyed) return;
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
                return;
            }

            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null || mc.player == null) {
                return;
            }
            Camera camera = mc.gameRenderer.getMainCamera();


            // Get player position for distance calculation

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
            renderUVSphere(barrierNode,core.getCenter(),camera.getPosition(),poseStack,buffers);
            renderTexture(poseStack,core.getCenter(),camera.getPosition(),buffers,barrierNode,mc.level.getGameTime());
        }
    }
}
