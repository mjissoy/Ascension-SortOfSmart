package net.thejadeproject.ascension.network.spatialrings;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.menus.spatialrings.ClientSpatialRingCache;

import java.util.UUID;

public record SyncSpatialRingInventoryPayload(UUID ringUuid, NonNullList<ItemStack> items, int scrollOffset, int totalRows)
        implements CustomPacketPayload {

    public static final Type<SyncSpatialRingInventoryPayload> TYPE =
            new Type<>(AscensionCraft.prefix("sync_spatial_ring_inventory"));

    public static final StreamCodec<FriendlyByteBuf, SyncSpatialRingInventoryPayload> STREAM_CODEC = StreamCodec.of(
            SyncSpatialRingInventoryPayload::encode,
            SyncSpatialRingInventoryPayload::decode
    );

    private static void encode(FriendlyByteBuf buf, SyncSpatialRingInventoryPayload payload) {
        buf.writeUUID(payload.ringUuid);
        buf.writeInt(payload.items.size());
        for (ItemStack stack : payload.items) {
            // More efficient: write boolean for empty, then item if not empty
            buf.writeBoolean(!stack.isEmpty());
            if (!stack.isEmpty()) {
                ItemStack.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, stack);
            }
        }
        buf.writeInt(payload.scrollOffset);
        buf.writeInt(payload.totalRows);
    }

    private static SyncSpatialRingInventoryPayload decode(FriendlyByteBuf buf) {
        UUID uuid = buf.readUUID();
        int size = buf.readInt();
        NonNullList<ItemStack> items = NonNullList.withSize(size, ItemStack.EMPTY);
        for (int i = 0; i < size; i++) {
            if (buf.readBoolean()) {
                items.set(i, ItemStack.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf));
            } else {
                items.set(i, ItemStack.EMPTY);
            }
        }
        int scrollOffset = buf.readInt();
        int totalRows = buf.readInt();
        return new SyncSpatialRingInventoryPayload(uuid, items, scrollOffset, totalRows);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SyncSpatialRingInventoryPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientSpatialRingCache.updateInventory(payload.ringUuid(), payload.items(),
                    payload.scrollOffset(), payload.totalRows());
        });
    }
}