package net.thejadeproject.ascension.items.pills;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PillCooldownItem extends Item {
    public int cooldownTimeValue = 50;

    public PillCooldownItem(Properties properties, Integer value) {
        super(properties);
        this.cooldownTimeValue = value;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide() && livingEntity instanceof Player player) {
            // Apply cooldown on server side
            player.getCooldowns().addCooldown(this, cooldownTimeValue);
            // Consume one item from the stack
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        // Call super to handle food effects (e.g., regeneration)
        return super.finishUsingItem(stack, level, livingEntity);
    }
}