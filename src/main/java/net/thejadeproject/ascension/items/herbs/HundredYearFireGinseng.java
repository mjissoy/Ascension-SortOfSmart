package net.thejadeproject.ascension.items.herbs;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HundredYearFireGinseng extends Item {
    public HundredYearFireGinseng(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            // Set player on fire for 3 seconds (60 ticks) - shorter since no fire resistance
            player.setRemainingFireTicks(300);


            // Visual and sound effects
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.8F, 1.0F);

            // Flame particles
            for (int i = 0; i < 15; i++) {
                double x = player.getX() + (level.random.nextDouble() - 0.5) * 3;
                double y = player.getY() + level.random.nextDouble() * 2;
                double z = player.getZ() + (level.random.nextDouble() - 0.5) * 3;
                level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.05, 0);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }
}
