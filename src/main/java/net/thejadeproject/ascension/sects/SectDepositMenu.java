package net.thejadeproject.ascension.sects;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.menus.ModMenuTypes;

import java.util.HashMap;
import java.util.Map;

public class SectDepositMenu extends AbstractContainerMenu {
    private final Container container;
    private final Player player;

    public SectDepositMenu(int windowId, Inventory playerInventory) {
        this(windowId, playerInventory, new SimpleContainer(27)); // 3 rows like a chest
    }

    public SectDepositMenu(int windowId, Inventory playerInventory, Container container) {
        super(ModMenuTypes.SECT_DEPOSIT_MENU.get(), windowId);
        this.container = container;
        this.player = playerInventory.player;

        checkContainerSize(container, 27);

        // Add container slots (3 rows of 9)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(container, j + i * 9, 8 + j * 18, 18 + i * 18));
            }
        }

        // Player inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Player hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide()) {
            // Process items when menu is closed
            processDepositItems((ServerPlayer) player);
        }
    }

    private void processDepositItems(ServerPlayer player) {
        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager == null) {
            returnAllItems(player);
            return;
        }

        Sect playerSect = manager.getPlayerSect(player.getUUID());
        if (playerSect == null) {
            // Player not in a sect, return all items
            returnAllItems(player);
            player.sendSystemMessage(Component.translatable("sect.deposit.no_sect"));
            return;
        }

        int totalPower = 0;
        Map<String, Integer> depositedItems = new HashMap<>(); // Track items by display name

        // Process each slot in the deposit container
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                int itemValue = getItemValue(stack);
                if (itemValue > 0) {
                    // Valid item, add to total power and track the item
                    String itemName = stack.getHoverName().getString();
                    int count = stack.getCount();
                    totalPower += itemValue * count;

                    // Add to our tracking map
                    depositedItems.merge(itemName, count, Integer::sum);

                    container.setItem(i, ItemStack.EMPTY);
                } else {
                    // Invalid item, return to player
                    returnItemToPlayer(player, stack);
                    container.setItem(i, ItemStack.EMPTY);
                }
            }
        }

        // Add power to sect and send single message
        if (totalPower > 0) {
            playerSect.addPower(totalPower);

            // Create a nicely formatted message
            if (depositedItems.size() == 1) {
                // Single item type
                Map.Entry<String, Integer> entry = depositedItems.entrySet().iterator().next();
                player.sendSystemMessage(Component.literal(
                        "§aDeposited §e" + entry.getValue() + "x " + entry.getKey() +
                                "§a for §e" + totalPower + "§a power to §b" + playerSect.getName() + "§a!"
                ));
            } else {
                // Multiple item types - build a detailed message
                StringBuilder message = new StringBuilder("§aDeposited ");

                int itemCount = 0;
                for (Map.Entry<String, Integer> entry : depositedItems.entrySet()) {
                    if (itemCount > 0) {
                        if (itemCount == depositedItems.size() - 1) {
                            message.append(" and ");
                        } else {
                            message.append(", ");
                        }
                    }
                    message.append("§e").append(entry.getValue()).append("x ").append(entry.getKey());
                    itemCount++;
                }

                message.append("§a for §e").append(totalPower).append("§a power to §b")
                        .append(playerSect.getName()).append("§a!");
                player.sendSystemMessage(Component.literal(message.toString()));
            }

            // Save the sect data
            manager.save();
        }
    }

    private int getItemValue(ItemStack stack) {
        // Define your item values here
        if (stack.is(Items.COPPER_INGOT)) {
            return 1;
        }
        if (stack.is(Items.IRON_INGOT)) {
            return 2;
        }
        if (stack.is(Items.GOLD_INGOT)) {
            return 2;
        }
        if (stack.is(Items.DIAMOND)) {
            return 4;
        }
        if (stack.is(Items.EMERALD)) {
            return 5;
        }
        if (stack.is(Items.NETHERITE_INGOT)) {
            return 6;
        }

        // Check for mod items - you'll need to import your mod items
        // Example:
        if (stack.is(ModItems.FROST_SILVER_INGOT.get())) {
            return 3;
        }
        if (stack.is(ModItems.BLACK_IRON_INGOT.get())) {
            return 3;
        }
        if (stack.is(ModItems.JADE.get())) {
            return 4;
        }
        if (stack.is(ModItems.SPIRITUAL_STONE.get())) {
            return 16;
        }

        return 0; // Not a valid deposit item
    }

    private void returnAllItems(ServerPlayer player) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                returnItemToPlayer(player, stack);
                container.setItem(i, ItemStack.EMPTY);
            }
        }
    }

    private void returnItemToPlayer(ServerPlayer player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 27) {
                // From container to player inventory
                if (!this.moveItemStackTo(itemstack1, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // From player inventory to container
                if (!this.moveItemStackTo(itemstack1, 0, 27, false)) {
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