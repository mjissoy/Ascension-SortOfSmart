package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.UUID;

public class EnderPouch extends Item {

    // Track which hand holds the active Ender Pouch for each player
    private static final Map<UUID, InteractionHand> activeUsers = new WeakHashMap<>();

    public EnderPouch(Properties properties) {
        super(properties.stacksTo(1));
        NeoForge.EVENT_BUS.register(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.isSpectator()) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide) {
            activeUsers.put(player.getUUID(), hand);

            player.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, playerEntity) -> ChestMenu.threeRows(containerId, playerInventory, player.getEnderChestInventory()),
                    Component.translatable("item.ascension.ender_pouch_gui")
            ));

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDER_CHEST_OPEN, SoundSource.PLAYERS,
                    0.5F, level.random.nextFloat() * 0.1F + 0.9F);

            player.getCooldowns().addCooldown(this, 50);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    /**
     * Prevent the Ender Pouch from being moved while its GUI is open.
     * This method is called when the item is being picked up by the cursor.
     */
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other,
                                            Slot slot, ClickAction action,
                                            Player player, SlotAccess access) {
        // If this player has the Ender Pouch GUI open, prevent picking up the Ender Pouch
        if (activeUsers.containsKey(player.getUUID()) && stack.is(this)) {
            InteractionHand hand = activeUsers.get(player.getUUID());

            // Check if this is the slot containing our Ender Pouch
            int expectedSlot = (hand == InteractionHand.MAIN_HAND) ?
                    player.getInventory().selected : 40; // 40 = offhand

            if (slot.getContainerSlot() == expectedSlot) {
                // Return true to cancel the default behavior (prevent moving)
                return true;
            }
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    /**
     * Also prevent the Ender Pouch from being swapped with another item
     */
    @Override
    public boolean overrideStackedOnOther(ItemStack stack, net.minecraft.world.inventory.Slot slot,
                                          net.minecraft.world.inventory.ClickAction action, Player player) {
        if (activeUsers.containsKey(player.getUUID()) && stack.is(this)) {
            // Prevent the Ender Pouch from being placed into other slots
            return true;
        }
        return super.overrideStackedOnOther(stack, slot, action, player);
    }

    /**
     * Clean up when the container closes
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onContainerClose(PlayerContainerEvent.Close event) {
        Player player = event.getEntity();
        activeUsers.remove(player.getUUID());
    }
}