package net.thejadeproject.ascension.items.herbs;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class HundredYearSnowGinseng extends ItemNameBlockItem {
    public HundredYearSnowGinseng(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide && entity instanceof Player player) {
            // Apply freezing
            player.setTicksFrozen(300); // 15 seconds of freezing (300 ticks)

            // Add other powder snow effects
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2));
        }
        return super.finishUsingItem(stack, level, entity);
    }
}