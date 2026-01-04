package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.ChatFormatting;
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
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.menus.spatialrings.*;

import javax.annotation.Nonnull;
import java.util.UUID;

public class SpatialRingItem extends Item {

    public SpatialRingItem(Properties properties) {
        super(properties);
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
        } else {
            uuid = UUID.randomUUID();
            stack.set(AscensionCraft.SPATIALRING_UUID, uuid);
        }

        return SpatialRingManager.get().getOrCreateSpatialring(uuid);
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
                }
            }
        } else {
            // Open storage GUI
            if (!worldIn.isClientSide && playerIn instanceof ServerPlayer serverPlayer) {
                SpatialRingData data = SpatialRingItem.getData(spatialring);
                if (data != null) {
                    UUID uuid = data.getUuid();

                    data.updateAccessRecords(serverPlayer.getName().getString(), System.currentTimeMillis());

                    // FIXED LINE: Pass data.getHandler() instead of data
                    serverPlayer.openMenu(new SimpleMenuProvider(
                            (windowId, playerInventory, playerEntity) ->
                                    new SpatialRingStorageContainer(windowId, playerInventory, uuid, data.getHandler(), data.getTotalRows()),
                            spatialring.getHoverName()
                    ), (buffer -> {
                        buffer.writeUUID(uuid);
                        buffer.writeInt(data.getExtraRows());
                    }));
                }
            }
        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }
}