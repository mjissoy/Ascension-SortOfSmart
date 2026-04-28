package net.thejadeproject.ascension.common.items.herbs;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;


/**
 * Hundred Year Fire Ginseng.
 * Sets the eater briefly on fire when consumed raw.
 * Extends HerbBlockItem so Quality and Age tooltips are shown automatically.
 */
public class HundredYearFireGinseng extends HerbBlockItem {

    public HundredYearFireGinseng(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            player.setRemainingFireTicks(300);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 0.8F, 1.0F);

            for (int i = 0; i < 15; i++) {
                double x = player.getX() + (level.random.nextDouble() - 0.5) * 3;
                double y = player.getY() + level.random.nextDouble() * 2;
                double z = player.getZ() + (level.random.nextDouble() - 0.5) * 3;
                level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.05, 0);
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }
    // appendHoverText inherited from HerbBlockItem — no override needed
}