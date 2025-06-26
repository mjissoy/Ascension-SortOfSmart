package net.thejadeproject.ascension.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;

public class CleanseEffect extends MobEffect {
    public CleanseEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    // Called every tick the effect is active
    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // Remove all harmful effects
        ArrayList<MobEffectInstance> effectsToRemove = new ArrayList<>();
        for (MobEffectInstance effectInstance : entity.getActiveEffects()) {
            if (effectInstance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                effectsToRemove.add(effectInstance);
            }
        }
        for (MobEffectInstance effectInstance : effectsToRemove) {
            entity.removeEffect(effectInstance.getEffect());
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
