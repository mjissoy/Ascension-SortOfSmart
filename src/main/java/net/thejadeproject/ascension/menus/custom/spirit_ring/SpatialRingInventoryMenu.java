package net.thejadeproject.ascension.menus.custom.spirit_ring;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.thejadeproject.ascension.items.data_components.ModDataComponents;
import net.thejadeproject.ascension.menus.ModMenuTypes;

public class SpatialRingInventoryMenu extends AbstractContainerMenu {
    private ItemStack stack;
    private ItemStackHandler handler;
    private Inventory inventory;

    public SpatialRingInventoryMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        super(ModMenuTypes.SPATIAL_RING_INVENTORY_MENU.get(),containerId);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        this.stack = inventory.player.getItemInHand(InteractionHand.MAIN_HAND);
        this.handler = stack.get(ModDataComponents.SPIRIT_RING_DATA).createItemHandler(stack);
        for(int i = 0; i<handler.getSlots();i++){
            this.addSlot(new SlotItemHandler(handler,i,0,0));
        }
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 0, 0));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i,0, 0));
        }
    }

    public int getSpatialRingInventorySlots(){
        return handler.getSlots();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        //TODO
        return null;
    }


    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).equals(stack);
    }

}
