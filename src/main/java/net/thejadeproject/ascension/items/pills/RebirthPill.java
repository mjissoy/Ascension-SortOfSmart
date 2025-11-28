package net.thejadeproject.ascension.items.pills;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.cultivation.player.PlayerDataChangeHandler;

public class RebirthPill extends PillCooldownItem{
    public RebirthPill(Properties properties, Integer value) {
        super(properties, value);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide() && livingEntity instanceof Player player) {

            // Consume one item from the stack
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            PlayerDataChangeHandler.resetData(player);

        }
        // Call super to handle food effects (e.g., regeneration)
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
