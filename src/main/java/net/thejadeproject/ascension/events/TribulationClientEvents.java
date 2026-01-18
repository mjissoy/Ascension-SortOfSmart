package net.thejadeproject.ascension.events;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.network.clientBound.ScreenShakePayload;
import org.joml.Vector3f;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID, value = Dist.CLIENT)
public class TribulationClientEvents {
    private static int screenShakeTicks = 0;
    private static float screenShakeIntensity = 0f;
    private static final Vector3f shakeOffset = new Vector3f();

    public static void handleScreenShake(ScreenShakePayload payload) {
        screenShakeTicks = payload.durationTicks();
        screenShakeIntensity = payload.intensity();
    }

    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        if (screenShakeTicks > 0) {
            // Apply random offset to camera
            shakeOffset.set(
                    (float) (Math.random() - 0.5) * screenShakeIntensity,
                    (float) (Math.random() - 0.5) * screenShakeIntensity,
                    (float) (Math.random() - 0.5) * screenShakeIntensity
            );

            event.setPitch(event.getPitch() + shakeOffset.x * 10);
            event.setYaw(event.getYaw() + shakeOffset.y * 10);
            event.setRoll(event.getRoll() + shakeOffset.z * 5);

            screenShakeTicks--;
        }
    }
}
