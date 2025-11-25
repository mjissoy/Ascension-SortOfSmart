package net.thejadeproject.ascension.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class QiDevouringParasiteEffect extends MobEffect {

    public QiDevouringParasiteEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && !player.level().isClientSide()) {
            // Check if player has any experience at all (levels or points)
            boolean hasAnyExperience = player.totalExperience > 0 || player.experienceLevel > 0;

            if (hasAnyExperience) {
                // Calculate drain amount - much more severe with higher amplifier
                int drainAmount = (amplifier + 1) * 25; // Increased from 10 to 25 per amplifier level

                // If player has enough total experience, drain it
                if (player.totalExperience >= drainAmount) {
                    player.giveExperiencePoints(-drainAmount);
                } else {
                    // Not enough XP points, try to drain a level
                    if (player.experienceLevel > 0) {
                        player.giveExperienceLevels(-1);
                        // In 1.21.1, we need to manually set experience progress to 0
                        // by giving negative experience points to clear any remaining progress
                        int currentProgress = (int)(player.experienceProgress * player.getXpNeededForNextLevel());
                        if (currentProgress > 0) {
                            player.giveExperiencePoints(-currentProgress);
                        }
                    } else {
                        // No levels left and not enough points, drain remaining points
                        player.giveExperiencePoints(-player.totalExperience);
                    }
                }
            } else {
                // No experience left at all, start draining health with higher damage
                float damage = 3.0f + (amplifier * 2.0f); // Increased from 1.0 to 3.0 base + 2.0 per level
                player.hurt(player.damageSources().magic(), damage);
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // Apply effect more frequently - every 1 second (20 ticks) at base
        // Higher amplifier levels drain much faster
        int interval = Math.max(10, 20 - (amplifier * 3)); // Reduced from 40 to 20 base, and min from 20 to 10
        return duration % interval == 0;
    }
}