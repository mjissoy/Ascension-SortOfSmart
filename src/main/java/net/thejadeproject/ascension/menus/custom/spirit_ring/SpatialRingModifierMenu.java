package net.thejadeproject.ascension.menus.custom.spirit_ring;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.thejadeproject.ascension.items.data_components.ModDataComponents;
import net.thejadeproject.ascension.menus.ModMenuTypes;

public class SpatialRingModifierMenu extends AbstractContainerMenu {
    private ItemStack stack;
    private ItemStackHandler modifierHandler;
    private ItemStackHandler upgradeHandler;
    private Inventory inventory;

    public SpatialRingModifierMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        super(ModMenuTypes.SPATIAL_RING_MODIFIER_MENU.get(),containerId);
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        this.stack = inventory.player.getItemInHand(InteractionHand.MAIN_HAND);
        this.modifierHandler = stack.get(ModDataComponents.SPIRIT_RING_DATA).createModifierHandler(stack);
        this.upgradeHandler = stack.get(ModDataComponents.SPIRIT_RING_DATA).createUpgradeHandler(stack);
        for(int i = 0; i<modifierHandler.getSlots();i++){
            this.addSlot(new SlotItemHandler(modifierHandler,i,0,0));
        }
        for(int i = 0; i<upgradeHandler.getSlots();i++){
            this.addSlot(new SlotItemHandler(upgradeHandler,i,0,0));
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



    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        //TODO
        return null;
    }

    @Override
    public void slotsChanged(Container container) {
        super.slotsChanged(container);
        inventory.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).equals(stack);
    }


}
