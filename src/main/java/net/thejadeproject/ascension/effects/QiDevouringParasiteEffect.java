package net.thejadeproject.ascension.effects;

import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.util.ModAttachments;

public class QiDevouringParasiteEffect extends MobEffect {

    public QiDevouringParasiteEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && !player.level().isClientSide()) {
            int drainAmount = (amplifier + 1) * 25;
            if (player.getData(ModAttachments.PLAYER_DATA).getCurrentQi() >= drainAmount) {
                player.getData(ModAttachments.PLAYER_DATA).tryConsumeQi(drainAmount);
            }else{
                    float damage = player.getMaxHealth() / 100;
                    player.hurt(player.damageSources().magic(), damage);
                }
            }
            return true;
        }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = Math.max(10, 20 - (amplifier * 3));
        return duration % interval == 0;
    }
}