package net.thejadeproject.ascension.refactor_packages.runic.casting;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPathHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public final class RunicCastingSuppressionHandler {

    private static final Map<UUID, Long> ACTIVE_CASTERS = new HashMap<>();

    private static final int EFFECT_REFRESH_INTERVAL = 10;
    private static final int EFFECT_DURATION_TICKS = 24;

    private RunicCastingSuppressionHandler() {}

    public static void beginCasting(ServerPlayer player, int durationTicks) {
        if (player == null || durationTicks <= 0) return;

        long expiryTime = player.level().getGameTime() + durationTicks;
        ACTIVE_CASTERS.put(player.getUUID(), expiryTime);
    }

    public static void stopCasting(ServerPlayer player) {
        if (player == null) return;
        ACTIVE_CASTERS.remove(player.getUUID());
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.level().isClientSide()) return;

        long gameTime = player.level().getGameTime();

        Iterator<Map.Entry<UUID, Long>> iterator = ACTIVE_CASTERS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Long> entry = iterator.next();

            if (gameTime >= entry.getValue()) {
                iterator.remove();
            }
        }

        if (!ACTIVE_CASTERS.containsKey(player.getUUID())) return;
        if (!player.isAlive() || player.isSpectator()) {
            stopCasting(player);
            return;
        }

        if (gameTime % EFFECT_REFRESH_INTERVAL != 0) return;

        applySuppressionField(player);
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            stopCasting(player);
        }
    }

    private static void applySuppressionField(ServerPlayer caster) {
        if (!caster.hasData(ModAttachments.ENTITY_DATA)) return;

        IEntityData entityData = caster.getData(ModAttachments.ENTITY_DATA);
        int runicRealm = RunicPathHelper.getRunicMajorRealm(entityData);

        double radius = getSuppressionRadius(runicRealm);
        int slowAmplifier = getSlowAmplifier(runicRealm);
        int fatigueAmplifier = getFatigueAmplifier(runicRealm);

        AABB area = caster.getBoundingBox().inflate(radius);

        for (LivingEntity target : caster.level().getEntitiesOfClass(LivingEntity.class, area)) {
            if (target == caster) continue;
            if (!target.isAlive()) continue;

            if (target instanceof Player playerTarget && playerTarget.isCreative()) {
                continue;
            }

            target.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    EFFECT_DURATION_TICKS,
                    slowAmplifier,
                    true,
                    false,
                    false
            ));

            target.addEffect(new MobEffectInstance(
                    MobEffects.DIG_SLOWDOWN,
                    EFFECT_DURATION_TICKS,
                    fatigueAmplifier,
                    true,
                    false,
                    false
            ));
        }
    }

    private static double getSuppressionRadius(int runicRealm) {
        return Math.min(7.0D, 3.0D + Math.max(0, runicRealm) * 0.5D);
    }

    private static int getSlowAmplifier(int runicRealm) {
        if (runicRealm >= 7) return 2;
        if (runicRealm >= 3) return 1;
        return 0;
    }

    private static int getFatigueAmplifier(int runicRealm) {
        return runicRealm >= 5 ? 1 : 0;
    }
}