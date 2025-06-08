package net.thejadeproject.ascension.guis.blockentity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class PillCauldronHumanMenu extends AbstractContainerMenu {
    public final PillCauldronLowHumanEntity blockEntity;
    private final Level level;

    public PillCauldronHumanMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));

        // Input slots
        for (int i = 0; i < PillCauldronLowHumanEntity.INPUT_SLOTS; i++) {
            addSlot(new SlotItemHandler(blockEntity.getInventory(), i, 44 + i * 18, 20));
        }

        // Output slots
        for (int i = 0; i < PillCauldronLowHumanEntity.OUTPUT_SLOTS; i++) {
            addSlot(new SlotItemHandler(blockEntity.getInventory(), i + PillCauldronLowHumanEntity.INPUT_SLOTS, 116 + i * 18, 20));
        }

        // Player inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inv, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
            }
        }

        // Player hotbar
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inv, i, 8 + i * 18, 109));
        }
    }

    public PillCauldronHumanMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super(, containerId);
        this.blockEntity = ((PillCauldronLowHumanEntity) blockEntity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);



    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();

            if (index < 5) {
                if (!moveItemStackTo(stack, 5, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!moveItemStackTo(stack, 0, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9, 8 + l * 18, 84 + i *18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public int getHeatLevel() {
        return blockEntity.getHeatLevel();
    }
}
