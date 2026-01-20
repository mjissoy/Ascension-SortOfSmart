package net.thejadeproject.ascension.effects;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class HeartDemonEffect extends MobEffect {
    private static final double BASE_DRAIN_RATE = 10.0;
    private static final double DRAIN_RATE_PER_LEVEL = 12.5;

    protected HeartDemonEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player) || player.level().isClientSide()) {
            return true;
        }

        var cultivationData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData();

        // Apply to all cultivation paths
        for (var pathData : cultivationData.getPaths()) {
            if (pathData.isCultivating()) {
                pathData.setCultivating(false);
            }

            // At level 3+ drain progress and potentially regress minor realms
            if (amplifier >= 3) {
                handleCultivationDrain(player, pathData, amplifier, cultivationData);
            }
        }
        return true;
    }

    private void handleCultivationDrain(Player player, CultivationData.PathData pathData, int amplifier, CultivationData cultivationData) {
        String pathId = pathData.pathId;
        double drainRate = calculateDrainRate(amplifier);

        if (pathData.pathProgress > 0) {
            double newProgress = Math.max(0, pathData.pathProgress - drainRate);
            cultivationData.setPathProgress(pathId, newProgress);
        } else if (pathData.minorRealm > 0) {
            int newMinorRealm = pathData.minorRealm - 1;
            cultivationData.setPathMinorRealm(pathId, newMinorRealm);

            double maxProgress = cultivationData.getMaxQiForRealm(pathId);
            cultivationData.setPathProgress(pathId, maxProgress);

            player.displayClientMessage(
                    Component.translatable("effect.ascension.heart_demon.minor_realm_decrease", newMinorRealm)
                            .withStyle(ChatFormatting.DARK_RED),
                    true
            );
        }
    }

    private double calculateDrainRate(int amplifier) {
        return BASE_DRAIN_RATE + (amplifier - 3) * DRAIN_RATE_PER_LEVEL;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
