package net.thejadeproject.ascension.common.items.tools;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class SpiritualStoneTools {

    private static void attemptQiRepair(ItemStack stack, Level level, Entity entity, int slotId) {
        if (level.isClientSide() || !(entity instanceof Player player)) return;
        if (slotId < 0 || slotId >= 9) return;
        if (!stack.isDamaged()) return;

        //Repair every 20 ticks 1 Qi = 2 Durability Repaired
        if (player.tickCount % 20 == 0) {
            //TODO implement regen
        }
    }

    public static class Pickaxe extends PickaxeItem {
        public Pickaxe(Tier tier, Properties properties) {
            super(tier, properties);
        }

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
            super.inventoryTick(stack, level, entity, slotId, isSelected);
            attemptQiRepair(stack, level, entity, slotId);
        }

        @Override
        public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return false;
        }
    }

    public static class Axe extends AxeItem {
        public Axe(Tier tier, Properties properties) {
            super(tier, properties);
        }

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
            super.inventoryTick(stack, level, entity, slotId, isSelected);
            attemptQiRepair(stack, level, entity, slotId);
        }
        @Override
        public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return false;
        }
    }

    public static class Shovel extends ShovelItem {
        public Shovel(Tier tier, Properties properties) {
            super(tier, properties);
        }

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
            super.inventoryTick(stack, level, entity, slotId, isSelected);
            attemptQiRepair(stack, level, entity, slotId);
        }
        @Override
        public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return false;
        }
    }

    public static class Hoe extends HoeItem {
        public Hoe(Tier tier, Properties properties) {
            super(tier, properties);
        }

        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
            super.inventoryTick(stack, level, entity, slotId, isSelected);
            attemptQiRepair(stack, level, entity, slotId);
        }
        @Override
        public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return false;
        }
    }
}
