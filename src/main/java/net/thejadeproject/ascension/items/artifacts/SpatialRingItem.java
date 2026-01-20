package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.menus.spatialrings.*;
import net.thejadeproject.ascension.network.spatialrings.SyncSpatialRingInventoryPayload;
import net.thejadeproject.ascension.network.spatialrings.SyncSpatialRingUpgradesPayload;

import javax.annotation.Nonnull;
import java.util.UUID;

public class SpatialRingItem extends Item {
    private static final UUID MIGRATION_FLAG = UUID.fromString("00000000-0000-0000-0000-000000000001");

    public SpatialRingItem(Properties properties) {
        super(properties);
    }

    public static SpatialRingData getData(ItemStack stack) {
        if (!(stack.getItem() instanceof SpatialRingItem))
            return null;

        UUID uuid = getOrCreateUUID(stack);
        return SpatialRingManager.get().getOrCreateSpatialring(uuid);
    }

    private static UUID getOrCreateUUID(ItemStack stack) {
        // First check component
        if (stack.has(AscensionCraft.SPATIALRING_UUID)) {
            return stack.get(AscensionCraft.SPATIALRING_UUID);
        }

        // Check for legacy UUID in custom data
        if (stack.has(DataComponents.CUSTOM_DATA)) {
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag = customData.copyTag();
            if (tag.contains("UUID")) {
                UUID uuid = tag.getUUID("UUID");
                // Migrate to component
                stack.set(AscensionCraft.SPATIALRING_UUID, uuid);
                // Remove UUID from custom data
                tag.remove("UUID");
                // Only update custom data if there are other tags left
                if (!tag.isEmpty()) {
                    stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                } else {
                    stack.remove(DataComponents.CUSTOM_DATA);
                }
                return uuid;
            }
        }

        // Create new UUID
        UUID newUuid = UUID.randomUUID();
        stack.set(AscensionCraft.SPATIALRING_UUID, newUuid);
        return newUuid;
    }

    public static boolean isSpatialring(ItemStack stack) {
        return stack.getItem() instanceof SpatialRingItem;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @Nonnull InteractionHand handIn) {
        ItemStack spatialring = playerIn.getItemInHand(handIn);

        if (playerIn.isShiftKeyDown()) {
            // Open upgrade GUI
            if (!worldIn.isClientSide && playerIn instanceof ServerPlayer serverPlayer) {
                SpatialRingData data = SpatialRingItem.getData(spatialring);
                if (data != null) {
                    UUID uuid = data.getUuid();

                    serverPlayer.openMenu(new SimpleMenuProvider(
                            (windowId, playerInventory, playerEntity) ->
                                    new SpatialRingUpgradeContainer(windowId, playerInventory, uuid, data),
                            Component.translatable("container.ascension.spatial_ring_upgrade")
                    ), (buffer -> buffer.writeUUID(uuid)));

                    // Sync upgrades to client
                    if (serverPlayer.connection != null) {
                        serverPlayer.connection.send(new SyncSpatialRingUpgradesPayload(uuid, data.meta.getUpgradeItems()));
                    }
                }
            }
        } else {
            // Open storage GUI
            if (!worldIn.isClientSide && playerIn instanceof ServerPlayer serverPlayer) {
                SpatialRingData data = SpatialRingItem.getData(spatialring);
                if (data != null) {
                    UUID uuid = data.getUuid();

                    data.updateAccessRecords(serverPlayer.getName().getString(), System.currentTimeMillis());

                    serverPlayer.openMenu(new SimpleMenuProvider(
                            (windowId, playerInventory, playerEntity) ->
                                    new SpatialRingStorageContainer(windowId, playerInventory, uuid, data.getHandler(), data.getTotalRows()),
                            spatialring.getHoverName()
                    ), (buffer -> {
                        buffer.writeUUID(uuid);
                        buffer.writeInt(data.getExtraRows());
                        // Default scroll offset is 0 when opening via right-click
                        buffer.writeInt(0);
                    }));

                    // Sync inventory to client
                    if (serverPlayer.connection != null) {
                        var handler = data.getHandler();
                        if (handler instanceof net.neoforged.neoforge.items.ItemStackHandler itemHandler) {
                            net.minecraft.core.NonNullList<ItemStack> items = net.minecraft.core.NonNullList.withSize(itemHandler.getSlots(), ItemStack.EMPTY);
                            for (int i = 0; i < itemHandler.getSlots(); i++) {
                                items.set(i, itemHandler.getStackInSlot(i));
                            }
                            serverPlayer.connection.send(new SyncSpatialRingInventoryPayload(uuid, items, 0, data.getTotalRows()));
                        }
                    }
                }
            }
        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }
}