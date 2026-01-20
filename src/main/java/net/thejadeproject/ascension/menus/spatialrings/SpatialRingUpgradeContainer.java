package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.thejadeproject.ascension.items.artifacts.SpatialRingItem;
import net.thejadeproject.ascension.items.stones.SpatialStoneItem;
import net.thejadeproject.ascension.items.upgrades.UpgradeItem;
import net.thejadeproject.ascension.menus.ModMenuTypes;

import javax.annotation.Nonnull;
import java.util.UUID;

public class SpatialRingUpgradeContainer extends AbstractContainerMenu {
    private final ItemStackHandler upgradeHandler;
    private final UUID ringUuid;
    private final SpatialRingData ringData;

    public static SpatialRingUpgradeContainer fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf data) {
        UUID uuid = data.readUUID();
        SpatialRingData ringData = SpatialRingManager.get().getSpatialRing(uuid).orElse(null);
        return new SpatialRingUpgradeContainer(windowId, playerInventory, uuid, ringData);
    }

    public SpatialRingUpgradeContainer(int windowId, Inventory playerInventory, UUID ringUuid, SpatialRingData ringData) {
        super(ModMenuTypes.SPATIAL_RING_UPGRADE.get(), windowId);
        this.ringUuid = ringUuid;
        this.ringData = ringData;

        this.upgradeHandler = new ItemStackHandler(36) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                if (ringData != null) {
                    // Create a NonNullList from current stacks
                    NonNullList<ItemStack> currentStacks = NonNullList.create();
                    for (int i = 0; i < this.getSlots(); i++) {
                        currentStacks.add(this.getStackInSlot(i).copy());
                    }
                    ringData.updateUpgrades(currentStacks);
                }
            }
        };

        if (ringData != null) {
            var existingItems = ringData.meta.getUpgradeItems();
            for (int i = 0; i < Math.min(existingItems.size(), 36); i++) {
                upgradeHandler.setStackInSlot(i, existingItems.get(i).copy());
            }
        }

        addUpgradeSlots();
        addPlayerSlots(playerInventory);
    }

    private void addUpgradeSlots() {
        // First 18 slots: Spatial Stones only
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 9; col++) {
                int index = row * 9 + col;
                this.addSlot(new UpgradeSlot(upgradeHandler, index, 8 + col * 18, 32 + row * 18, true, false));
            }
        }

        // Last 18 slots: Other Upgrades only (no spatial stones)
        for (int row = 2; row < 4; row++) {
            for (int col = 0; col < 9; col++) {
                int index = row * 9 + col;
                this.addSlot(new UpgradeSlot(upgradeHandler, index, 8 + col * 18, 90 + (row - 2) * 18, false, true));
            }
        }
    }

    private void addPlayerSlots(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = 8 + col * 18;
                int y = 140 + row * 18;
                ItemStack stackInSlot = playerInventory.getItem(col + row * 9 + 9);
                boolean isRing = SpatialRingItem.isSpatialring(stackInSlot);
                this.addSlot(new LockableSlot(playerInventory, col + row * 9 + 9, x, y, isRing));
            }
        }

        for (int col = 0; col < 9; col++) {
            int x = 8 + col * 18;
            int y = 198;
            ItemStack stackInSlot = playerInventory.getItem(col);
            boolean isRing = SpatialRingItem.isSpatialring(stackInSlot);
            this.addSlot(new LockableSlot(playerInventory, col, x, y, isRing));
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        // No permission checking - containers remain open
        return true;
    }

    @Override
    public void clicked(int slot, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull Player player) {
        if (slot >= 0 && slot < this.slots.size()) {
            Slot slotObj = this.slots.get(slot);
            if (slotObj instanceof LockableSlot lockableSlot && lockableSlot.mayPickup(player)) {
                ItemStack slotStack = slotObj.getItem();
                if (SpatialRingItem.isSpatialring(slotStack)) {
                    return;
                }
            }
        }
        ItemStack carried = getCarried();
        if (SpatialRingItem.isSpatialring(carried)) {
            return;
        }
        super.clicked(slot, dragType, clickTypeIn, player);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 36) {
                // Transfer from upgrade slots to player inventory
                if (!this.moveItemStackTo(itemstack1, 36, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Transfer from player inventory to appropriate upgrade slots
                if (itemstack1.getItem() instanceof SpatialStoneItem) {
                    // Try to put in first 18 slots (spatial stone slots)
                    if (!this.moveItemStackTo(itemstack1, 0, 18, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getItem() instanceof UpgradeItem) {
                    // Try to put in last 18 slots (upgrade item slots)
                    if (!this.moveItemStackTo(itemstack1, 18, 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Not a valid upgrade item
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    private static class UpgradeSlot extends SlotItemHandler {
        private final boolean spatialStoneSlot;
        private final boolean upgradeItemSlot;

        public UpgradeSlot(ItemStackHandler handler, int index, int x, int y,
                           boolean spatialStoneSlot, boolean upgradeItemSlot) {
            super(handler, index, x, y);
            this.spatialStoneSlot = spatialStoneSlot;
            this.upgradeItemSlot = upgradeItemSlot;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            if (stack.isEmpty()) return true;

            if (spatialStoneSlot) {
                // Only spatial stones in stone slots
                return stack.getItem() instanceof SpatialStoneItem;
            } else if (upgradeItemSlot) {
                // Only upgrade items in upgrade slots (and not spatial stones)
                return stack.getItem() instanceof UpgradeItem &&
                        !(stack.getItem() instanceof SpatialStoneItem);
            }
            return false;
        }

        @Override
        public int getMaxStackSize(@Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}