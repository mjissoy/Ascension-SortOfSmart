package net.thejadeproject.ascension.items.pills;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemstack = player.getItemInHand(usedHand);
        // Check if the item is on cooldown
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(itemstack);
        }
        // Start the eating process for food items
        if (player.canEat(false)) {
            player.startUsingItem(usedHand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.fail(itemstack);
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