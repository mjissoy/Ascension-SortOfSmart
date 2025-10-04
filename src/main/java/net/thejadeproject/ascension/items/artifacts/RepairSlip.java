package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.Config;

public class RepairSlip extends Item {
    private static final int DEFAULT_REPAIR_INTERVAL = 100; // 5 seconds (100 ticks)
    private static final int DEFAULT_REPAIR_AMOUNT = 2; // Repairs 2 durability

    public RepairSlip(Properties properties) {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            int repairInterval = Config.COMMON.REPAIR_INTERVAL != null ?
                    Config.COMMON.REPAIR_INTERVAL.get() : DEFAULT_REPAIR_INTERVAL;

            if (player.tickCount % repairInterval == 0) {
                repairItems(player);
            }
        }
    }

    private void repairItems(Player player) {
        // Repair all inventory slots
        int repairAmount = Config.COMMON.REPAIR_AMOUNT != null ?
                Config.COMMON.REPAIR_AMOUNT.get() : DEFAULT_REPAIR_AMOUNT;

        player.getInventory().items.forEach(stack -> repairItem(stack, repairAmount));
        player.getInventory().armor.forEach(stack -> repairItem(stack, repairAmount));
        player.getInventory().offhand.forEach(stack -> repairItem(stack, repairAmount));
    }

    private void repairItem(ItemStack stack, int repairAmount) {
        if (!stack.isEmpty() && stack.isDamaged()) {
            stack.setDamageValue(Math.max(stack.getDamageValue() - repairAmount, 0));
        }
    }
}