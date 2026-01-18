package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.thejadeproject.ascension.items.artifacts.SpatialRingItem;
import net.thejadeproject.ascension.menus.ModMenuTypes;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpatialRingStorageContainer extends AbstractContainerMenu {
    private final UUID uuid;
    private final int totalRows;
    private final IItemHandler handler;
    private int scrollOffset = 0;
    private static final int VISIBLE_ROWS = 6;
    private static final int SLOTS_PER_ROW = 9;
    private Inventory playerInventory;
    private int playerSlotsStart = -1;

    // Store last scroll offset per ring per player
    private static final Map<UUID, Map<Player, Integer>> playerScrollOffsets = new HashMap<>();

    public static Map<UUID, Map<Player, Integer>> getPlayerScrollOffsets() {
        return playerScrollOffsets;
    }

    public static SpatialRingStorageContainer fromNetwork(int windowId, final Inventory playerInventory, FriendlyByteBuf data) {
        UUID uuidIn = data.readUUID();
        int extraRows = data.readInt();
        int totalRows = 3 + extraRows;
        int slots = totalRows * 9;
        ItemStackHandler dummyHandler = new ItemStackHandler(slots);
        return new SpatialRingStorageContainer(windowId, playerInventory, uuidIn, dummyHandler, totalRows);
    }

    public SpatialRingStorageContainer(final int windowId, final Inventory playerInventory, UUID uuidIn, IItemHandler handler, int totalRows) {
        super(ModMenuTypes.SPATIAL_RING_STORAGE.get(), windowId);
        this.uuid = uuidIn;
        this.totalRows = Math.max(totalRows, 3);
        this.handler = handler;
        this.playerInventory = playerInventory;

        // Restore previous scroll offset for this player and ring
        if (playerInventory.player != null) {
            Map<Player, Integer> playerOffsets = playerScrollOffsets.get(uuidIn);
            if (playerOffsets != null) {
                Integer savedOffset = playerOffsets.get(playerInventory.player);
                if (savedOffset != null) {
                    this.scrollOffset = savedOffset;
                }
            }
        }

        rebuildSlots();
    }

    private void rebuildSlots() {
        this.slots.clear();
        addStorageSlots();
        addPlayerSlots();
    }

    private void addStorageSlots() {
        if (this.handler == null) return;
        int rowsToShow = Math.min(this.totalRows, VISIBLE_ROWS);
        for (int row = 0; row < rowsToShow; row++) {
            for (int col = 0; col < SLOTS_PER_ROW; col++) {
                int actualRow = row + scrollOffset;
                if (actualRow >= this.totalRows) break;
                int slotIndex = actualRow * SLOTS_PER_ROW + col;
                if (slotIndex >= this.handler.getSlots()) break;
                int x = 8 + col * 18;
                int y = 18 + row * 18;
                this.addSlot(new StorageSlot(this.handler, slotIndex, x, y));
            }
        }
    }

    private void addPlayerSlots() {
        int visibleRows = Math.min(this.totalRows, VISIBLE_ROWS);
        int playerInventoryY = 17 + visibleRows * 18 + 15;
        int hotbarY = playerInventoryY + 58;
        this.playerSlotsStart = this.slots.size();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = 8 + col * 18;
                int y = playerInventoryY + row * 18;
                ItemStack stackInSlot = playerInventory.getItem(col + row * 9 + 9);
                boolean isRing = SpatialRingItem.isSpatialring(stackInSlot);
                this.addSlot(new LockableSlot(this.playerInventory, col + row * 9 + 9, x, y, isRing));
            }
        }
        for (int col = 0; col < 9; col++) {
            int x = 8 + col * 18;
            int y = hotbarY;
            ItemStack stackInSlot = playerInventory.getItem(col);
            boolean isRing = SpatialRingItem.isSpatialring(stackInSlot);
            this.addSlot(new LockableSlot(this.playerInventory, col, x, y, isRing));
        }
    }

    public void setScrollOffset(int offset) {
        int maxScroll = Math.max(0, this.totalRows - VISIBLE_ROWS);
        int newOffset = Math.max(0, Math.min(offset, maxScroll));
        if (newOffset != this.scrollOffset) {
            this.scrollOffset = newOffset;
            rebuildSlots();

            // Save scroll offset for this player and ring
            if (this.playerInventory.player != null) {
                playerScrollOffsets.computeIfAbsent(this.uuid, k -> new HashMap<>())
                        .put(this.playerInventory.player, this.scrollOffset);
            }
        }
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getVisibleRows() {
        return VISIBLE_ROWS;
    }

    public int getVisibleStorageSlots() {
        return Math.min(this.totalRows, VISIBLE_ROWS) * SLOTS_PER_ROW;
    }

    // Remove player's saved scroll offset when container closes
    @Override
    public void removed(Player player) {
        super.removed(player);
        Map<Player, Integer> playerOffsets = playerScrollOffsets.get(this.uuid);
        if (playerOffsets != null) {
            playerOffsets.remove(player);
            if (playerOffsets.isEmpty()) {
                playerScrollOffsets.remove(this.uuid);
            }
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
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
        if (slot == null || !slot.hasItem()) {
            return itemstack;
        }
        ItemStack slotStack = slot.getItem();
        itemstack = slotStack.copy();
        int visibleStorageSlots = getVisibleStorageSlots();
        int playerInventoryStart = visibleStorageSlots;
        int hotbarStart = playerInventoryStart + 27;
        int hotbarEnd = hotbarStart + 9;
        if (index < visibleStorageSlots) {
            if (!this.moveItemStackTo(slotStack, playerInventoryStart, hotbarEnd, true)) {
                return ItemStack.EMPTY;
            }
        }
        else {
            if (SpatialRingItem.isSpatialring(slotStack)) {
                return ItemStack.EMPTY;
            }
            if (!this.moveItemStackTo(slotStack, 0, visibleStorageSlots, false)) {
                if (index >= playerInventoryStart && index < hotbarStart) {
                    if (!this.moveItemStackTo(slotStack, hotbarStart, hotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= hotbarStart && index < hotbarEnd) {
                    if (!this.moveItemStackTo(slotStack, playerInventoryStart, hotbarStart, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        if (slotStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return itemstack;
    }

    private static class StorageSlot extends SlotItemHandler {
        public StorageSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return super.mayPlace(stack) && !SpatialRingItem.isSpatialring(stack);
        }
    }
}