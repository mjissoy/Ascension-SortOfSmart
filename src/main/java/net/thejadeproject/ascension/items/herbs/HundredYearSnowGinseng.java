package net.thejadeproject.ascension.items.herbs;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * Hundred Year Snow Ginseng.
 * Applies a temporary freeze and slowness effect when consumed raw.
 * Extends HerbBlockItem so Quality and Age tooltips are shown automatically.
 */
public class HundredYearSnowGinseng extends HerbBlockItem {

    public HundredYearSnowGinseng(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            player.setTicksFrozen(300);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
        }
        return super.finishUsingItem(stack, level, entity);
    }
    // appendHoverText inherited from HerbBlockItem — no override needed
}