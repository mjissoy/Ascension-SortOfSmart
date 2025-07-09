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
        if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.fail(player.getItemInHand(usedHand));
        return super.use(level, player, usedHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if(livingEntity instanceof Player player && level.isClientSide()){
            player.getCooldowns().addCooldown(this,cooldownTimeValue);
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}


