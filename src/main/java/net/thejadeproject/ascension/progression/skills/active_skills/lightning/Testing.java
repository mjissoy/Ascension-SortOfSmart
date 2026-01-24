package net.thejadeproject.ascension.progression.skills.active_skills.lightning;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.active_skills.metal_dao.OreSightActiveSkill;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class Testing {
    public static List<Vec3> points = new ArrayList<>();
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        if(points.isEmpty()) return;

        Camera camera = mc.gameRenderer.getMainCamera();
        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

        // Get player position for distance calculation
        BlockPos playerPos = mc.player.blockPosition();

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        renderArc(points,poseStack,buffers,-1,255,mc.player.position(),camera.getPosition());
    }
    public static List<Vec3> generateArc(Vec3 p1,Vec3 p2){
        Vec3 dir = p2.subtract(p1.add(new Vec3(0,1.5,0))).normalize();
        List<Vec3> points = new ArrayList<>();
        points.add(new Vec3(p1.x,p1.y+1.5,p1.z));
        while(points.size() < 10){
            dir = dir.xRot(ThreadLocalRandom.current().nextFloat(-35,35)* Mth.DEG_TO_RAD);
            dir = dir.yRot( ThreadLocalRandom.current().nextFloat(-35,35)* Mth.DEG_TO_RAD);
            dir = dir.zRot(ThreadLocalRandom.current().nextFloat(-35,35 )* Mth.DEG_TO_RAD);
            dir = dir.scale(ThreadLocalRandom.current().nextDouble(1.5,2));
            System.out.println(dir.toString());
            Vec3 newPoint = points.getLast().add(dir);
            points.add(newPoint);

            dir = p2.subtract(newPoint).normalize();
        }
        points.add(p2);
        for(Vec3 point : points){
            System.out.println(point.toString());
        }
        return points;

    }
    public static void renderArc(List<Vec3> points,PoseStack poseStack, MultiBufferSource.BufferSource buffers, int color, int alpha,Vec3 playerPos, Vec3 camera){
        poseStack.pushPose();
        poseStack.translate( - camera.x(),  - camera.y(),  - camera.z());
        VertexConsumer consumer = buffers.getBuffer(RenderType.debugQuads());
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        for(int i =1; i<points.size();i++){
            Vec3 dir = points.get(i).subtract(points.get(i-1)).normalize();
            Vec3 mid = points.get(i).add(points.get(i-1)).scale(0.5);
            Vec3 toCamera = camera.subtract(mid).normalize();
            Vec3 side = dir.cross(toCamera).normalize().scale(0.1);
            Vec3 a1 = points.get(i).add(side);
            Vec3 a2 = points.get(i).subtract(side);
            Vec3 b1 = points.get(i-1).add(side);
            Vec3 b2 = points.get(i-1).subtract(side);

            consumer.addVertex(pose, a1.toVector3f()).setColor(56,41,217,255);
            consumer.addVertex(pose, b1.toVector3f()).setColor(56,41,217,255);
            consumer.addVertex(pose, b2.toVector3f()).setColor(56,41,217,255);

            //consumer.addVertex(pose, a1.toVector3f()).setColor(56,41,217,255);
            //consumer.addVertex(pose, b2.toVector3f()).setColor(56,41,217,255);
            consumer.addVertex(pose, a2.toVector3f()).setColor(56,41,217,255);


            //consumer.addVertex(matrix, (float) points.get(i-1).x, (float) points.get(i-1).y, (float) points.get(i-1).z).setColor(56, 41, 217,255).setNormal(pose, 0.0f, 1.0f, 0.0f);;
            //consumer.addVertex(matrix, (float) points.get(i).x, (float) points.get(i).y, (float) points.get(i).z).setColor(56, 41, 217,255).setNormal(pose, 0.0f, 1.0f, 0.0f);;

        }

        poseStack.popPose();
    }
}
