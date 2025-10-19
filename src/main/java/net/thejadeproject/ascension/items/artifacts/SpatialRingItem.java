package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.menus.spatialrings.*;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class SpatialRingItem extends Item {
    public SpatialRingItem(String name, SpatialRing tier) {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(tier.rarity));
        this.name = name;
        this.tier = tier;
    }

    final String name;
    final SpatialRing tier;
    private static final Random random = new Random();

    public static SpatialRing getTier(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof SpatialRingItem)
            return ((SpatialRingItem) stack.getItem()).tier;
        else
            return SpatialRing.IRON;
    }

    public static SpatialRingData getData(ItemStack stack) {
        if (!(stack.getItem() instanceof SpatialRingItem))
            return null;

        UUID uuid;

        if (stack.has(AscensionCraft.SPATIALRING_UUID)) {
            uuid = stack.get(AscensionCraft.SPATIALRING_UUID);
        } else if (stack.has(DataComponents.CUSTOM_DATA)) {
            CompoundTag tag = stack.get(DataComponents.CUSTOM_DATA).copyTag();
            if (tag.contains("UUID")) {
                uuid = tag.getUUID("UUID");
                stack.set(AscensionCraft.SPATIALRING_UUID, uuid);
                stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, $ -> $.update(compoundTag -> compoundTag.remove("UUID")));
            } else {
                uuid = UUID.randomUUID();
                stack.set(AscensionCraft.SPATIALRING_UUID, uuid);
            }
        }
        else {
            uuid = UUID.randomUUID();
            stack.set(AscensionCraft.SPATIALRING_UUID, uuid);
        }


        return SpatialRingManager.get().getOrCreateSpatialring(uuid, ((SpatialRingItem) stack.getItem()).tier);
    }

    public static boolean isSpatialring(ItemStack stack) {
        return stack.getItem() instanceof SpatialRingItem;
    }

    @Override
    @Nonnull
    public Component getDescription() {
        return Component.translatable(this.getDescriptionId()).withStyle(this.tier == SpatialRing.JADE? ChatFormatting.DARK_AQUA:ChatFormatting.RESET);
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }


    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
        ItemStack spatialring = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide && playerIn instanceof ServerPlayer && spatialring.getItem() instanceof SpatialRingItem) {
            SpatialRingData data = SpatialRingItem.getData(spatialring);

            SpatialRing itemTier = ((SpatialRingItem) spatialring.getItem()).tier;
            UUID uuid = data.getUuid();

            data.updateAccessRecords(playerIn.getName().getString(), System.currentTimeMillis());

            if (data.getTier().ordinal() < itemTier.ordinal()) {
                data.upgrade(itemTier);
                playerIn.sendSystemMessage(Component.literal("Backpack upgraded to " + itemTier.name));
            }

            if (playerIn.isShiftKeyDown()) {
                // Shift+Right Click: Toggle auto-pickup
                togglePickup(playerIn, spatialring);
            } else {
                // Regular Right Click: Open inventory
                playerIn.openMenu(new SimpleMenuProvider(
                                (windowId, playerInventory, playerEntity) -> new SRContainer(windowId, playerInventory, uuid, data.getTier(), data.getHandler()),
                                spatialring.getHoverName()),
                        (buffer -> buffer.writeUUID(uuid).writeInt(data.getTier().ordinal()))
                );
            }
        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }

    public static void togglePickup(Player playerEntity, ItemStack stack) {
        boolean Pickup = !stack.getOrDefault(AscensionCraft.SPATIALRING_PICKUP, false);

        stack.set(AscensionCraft.SPATIALRING_PICKUP, Pickup);
        if (playerEntity instanceof ServerPlayer serverPlayer)
            serverPlayer.displayClientMessage(Component.translatable(Pickup?"ascension.autopickupenabled":"ascension.autopickupdisabled"), true);
        else
            playerEntity.displayClientMessage(Component.translatable(Pickup?"ascension.autopickupenabled":"ascension.autopickupdisabled"), true);

    }

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Pre event) {
        Player player = event.getPlayer();

        // Check all spatial rings in inventory for auto-pickup
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof SpatialRingItem && stack.getOrDefault(AscensionCraft.SPATIALRING_PICKUP, false)) {
                if (pickupEvent(event, stack)) {
                    // If item was fully picked up, we're done
                    return;
                }
            }
        }

        // Also check curios if available
        if (SpatialRingUtils.curiosLoaded) {
            var curiosInv = CuriosApi.getCuriosInventory(player);
            if (curiosInv.isPresent()) {
                curiosInv.get().findCurios(SpatialRingItem::isSpatialring).forEach(slot -> {
                    ItemStack stack = slot.stack();
                    if (stack.getOrDefault(AscensionCraft.SPATIALRING_PICKUP, false)) {
                        pickupEvent(event, stack);
                    }
                });
            }
        }
    }


    public static boolean pickupEvent(ItemEntityPickupEvent.Pre event, ItemStack stack) {
        if (!stack.getOrDefault(AscensionCraft.SPATIALRING_PICKUP, false))
            return false;

        Optional<IItemHandler> optional = Optional.ofNullable(stack.getCapability(Capabilities.ItemHandler.ITEM));
        if (optional.isPresent()) {
            IItemHandler handler = optional.get();

            if (!(handler instanceof ASCItemHandler))
                return false;

            ItemStack pickedUp = event.getItemEntity().getItem();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack slot = handler.getStackInSlot(i);
                if (slot.isEmpty() || (ItemStack.isSameItemSameComponents(slot, pickedUp) && slot.getCount() < slot.getMaxStackSize() && slot.getCount() < handler.getSlotLimit(i))) {
                    int remainder = handler.insertItem(i, pickedUp.copy(), false).getCount();
                    pickedUp.setCount(remainder);
                    if (remainder == 0)
                        break;
                }
            }
            if (pickedUp.isEmpty())
                event.getPlayer().level().playSound(null, event.getPlayer().blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((random.nextFloat() - random.nextFloat()) * 0.7F + 1.0F) * 2.0F);

            return pickedUp.isEmpty();
        }
        else
            return false;
    }
}
