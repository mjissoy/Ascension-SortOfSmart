package net.thejadeproject.ascension.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class QiDevouringParasiteEffect extends MobEffect {

    public QiDevouringParasiteEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && !player.level().isClientSide()) {
            int drainAmount = (amplifier + 1) * 25;
            //TODO
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = Math.max(10, 20 - (amplifier * 3));
        return duration % interval == 0;
    }
}