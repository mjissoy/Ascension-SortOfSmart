package net.thejadeproject.ascension.guis.menu;

import net.lucent.easygui.util.inventory.EasyItemHandlerSlot;
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
import net.thejadeproject.ascension.menus.spatialrings.LockableSlot;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpatialRingItemContainerMenu extends AbstractContainerMenu {
    private final UUID uuid;
    private final int totalRows;
    private final ItemStackHandler handler;
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

    public static SpatialRingItemContainerMenu fromNetwork(int windowId, final Inventory playerInventory, FriendlyByteBuf data) {
        UUID uuidIn = data.readUUID();
        int extraRows = data.readInt();
        int totalRows = 3 + extraRows;
        int slots = totalRows * 9;

        ItemStackHandler dummyHandler = new ItemStackHandler(slots);
        return new SpatialRingItemContainerMenu(windowId, playerInventory, uuidIn, dummyHandler, totalRows);
    }

    public SpatialRingItemContainerMenu(final int windowId, final Inventory playerInventory, UUID uuidIn, ItemStackHandler handler, int totalRows) {
        super(ModMenuTypes.SPATIAL_RING_STORAGE.get(), windowId);
        this.uuid = uuidIn;
        this.totalRows = Math.max(totalRows, 3);
        this.handler = handler;
        this.playerInventory = playerInventory;



        addStorageSlots();
        addPlayerSlots();
    }



    private void addStorageSlots() {
        if (this.handler == null) return;

        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < SLOTS_PER_ROW; col++) {

                int slotIndex = row * SLOTS_PER_ROW + col;

                this.addSlot(new SpatialRingItemContainerMenu.StorageSlot(this.handler, slotIndex, 0, 0,"i_"+slotIndex));
            }
        }
    }

    private void addPlayerSlots() {


        this.playerSlotsStart = this.slots.size();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {

                ItemStack stackInSlot = playerInventory.getItem(col + row * 9 + 9);
                boolean isRing = SpatialRingItem.isSpatialring(stackInSlot);
                this.addSlot(new LockableSlot(this.playerInventory, col + row * 9 + 9,0, 0, isRing,"p_"+(col + row * 9 + 9)));
            }
        }
        for (int col = 0; col < 9; col++) {

            ItemStack stackInSlot = playerInventory.getItem(col);
            boolean isRing = SpatialRingItem.isSpatialring(stackInSlot);
            this.addSlot(new LockableSlot(this.playerInventory, col, 0, 0, isRing,"p_"+col));
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



    @Override
    public boolean stillValid(@Nonnull Player player) {
        return true;
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

    private static class StorageSlot extends EasyItemHandlerSlot {
        public StorageSlot(ItemStackHandler itemHandler, int index, int xPosition, int yPosition,String id) {
            super(itemHandler, index, xPosition, yPosition,id);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return super.mayPlace(stack) && !SpatialRingItem.isSpatialring(stack);
        }
    }
}