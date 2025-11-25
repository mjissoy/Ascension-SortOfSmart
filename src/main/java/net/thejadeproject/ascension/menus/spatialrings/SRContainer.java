package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.artifacts.SpatialRing;
import net.thejadeproject.ascension.items.artifacts.SpatialRingItem;
import net.thejadeproject.ascension.menus.ModMenuTypes;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public class SRContainer extends AbstractContainerMenu {
    public final IItemHandler handler;
    private final SpatialRing tier;
    private final UUID uuid;


    public static SRContainer fromNetwork(int windowId, final Inventory playerInventory, FriendlyByteBuf data) {
        UUID uuidIn = data.readUUID();
        SpatialRing tier = SpatialRing.values()[data.readInt()];
        return new SRContainer(windowId, playerInventory, uuidIn, tier, new ItemStackHandler(tier.slots));
    }

    public SRContainer(final int windowId, final Inventory playerInventory, UUID uuidIn, SpatialRing tierIn, IItemHandler handler) {
        super(ModMenuTypes.SR_CONTAINER.get(), windowId);

        this.uuid = uuidIn;
        this.handler = handler;

        this.tier = tierIn;


        addPlayerSlots(playerInventory);
        addMySlots();
    }

    public SpatialRing getTier() {
        return this.tier;
    }

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        return true;
    }



    @Override
    public void clicked(int slot, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull Player player) {
        if (clickTypeIn == ClickType.SWAP)
            return;
        if (slot >= 0) getSlot(slot).container.setChanged();
        super.clicked(slot, dragType, clickTypeIn, player);
    }

    private void addPlayerSlots(Inventory playerInventory) {
        int originX = this.tier.slotXOffset;
        int originY = this.tier.slotYOffset;

        //Hotbar
        for (int col = 0; col < 9; col++) {
            int x = originX + col * 18;
            int y = originY + 58;
            Optional<UUID> uuidOptional = SpatialRingUtils.getUUID(playerInventory.items.get(col));
            boolean lockMe = uuidOptional.map(id -> id.compareTo(this.uuid) == 0).orElse(false);
            this.addSlot(new LockableSlot(playerInventory, col, x+1, y+1, lockMe));
        }

        //Player Inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = originX + col * 18;
                int y = originY + row * 18;
                int index = (col + row * 9) + 9;
                Optional<UUID> uuidOptional = SpatialRingUtils.getUUID(playerInventory.items.get(index));
                boolean lockMe = uuidOptional.map(id -> id.compareTo(this.uuid) == 0).orElse(false);
                this.addSlot(new LockableSlot(playerInventory, index, x+1, y+1, lockMe));
            }
        }
    }

    private void addMySlots() {
        if (this.handler == null) return;

        int cols = this.tier.slotCols;
        int rows = this.tier.slotRows;

        int slot_index = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = 7 + col * 18;
                int y = 17 + row * 18;

                if (row > 7 && col > 2 && col < 13 && this.tier == SpatialRing.JADE)
                    continue;

                this.addSlot(new SRContainerSlot(this.handler, slot_index, x + 1, y + 1));
                slot_index++;
                if (slot_index >= this.tier.slots)
                    break;
            }
        }

    }

    @Override
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            int bagslotcount = this.slots.size();
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < playerIn.getInventory().items.size()) {
                // Player is trying to move item FROM their inventory INTO the spatial ring
                // ADD THE CHECK HERE:
                if (SpatialRingItem.isSpatialring(itemstack1)) {
                    return ItemStack.EMPTY; // Block spatial rings from being moved into the ring
                }

                if (!this.moveItemStackTo(itemstack1, playerIn.getInventory().items.size(), bagslotcount, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Item is coming FROM the spatial ring INTO player inventory - this is always allowed
                if (!this.moveItemStackTo(itemstack1, 0, playerIn.getInventory().items.size(), false)) {
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


}
