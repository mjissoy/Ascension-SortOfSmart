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

public record SyncSpatialRingUpgradesPayload(UUID ringUuid, NonNullList<ItemStack> upgradeItems)
        implements CustomPacketPayload {

    public static final Type<SyncSpatialRingUpgradesPayload> TYPE =
            new Type<>(AscensionCraft.prefix("sync_spatial_ring_upgrades"));

    public static final StreamCodec<FriendlyByteBuf, SyncSpatialRingUpgradesPayload> STREAM_CODEC = StreamCodec.of(
            SyncSpatialRingUpgradesPayload::encode,
            SyncSpatialRingUpgradesPayload::decode
    );

    private static void encode(FriendlyByteBuf buf, SyncSpatialRingUpgradesPayload payload) {
        buf.writeUUID(payload.ringUuid);
        buf.writeInt(payload.upgradeItems.size());
        for (ItemStack stack : payload.upgradeItems) {
            // Write boolean indicating if stack is empty
            buf.writeBoolean(!stack.isEmpty());
            if (!stack.isEmpty()) {
                ItemStack.STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, stack);
            }
        }
    }

    private static SyncSpatialRingUpgradesPayload decode(FriendlyByteBuf buf) {
        UUID uuid = buf.readUUID();
        int size = buf.readInt();
        NonNullList<ItemStack> upgradeItems = NonNullList.withSize(size, ItemStack.EMPTY);
        for (int i = 0; i < size; i++) {
            if (buf.readBoolean()) {
                // Stack is not empty, decode it
                upgradeItems.set(i, ItemStack.STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf));
            } else {
                // Stack is empty, leave as EMPTY
                upgradeItems.set(i, ItemStack.EMPTY);
            }
        }
        return new SyncSpatialRingUpgradesPayload(uuid, upgradeItems);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final SyncSpatialRingUpgradesPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientSpatialRingCache.updateUpgrades(payload.ringUuid(), payload.upgradeItems());
        });
    }
}