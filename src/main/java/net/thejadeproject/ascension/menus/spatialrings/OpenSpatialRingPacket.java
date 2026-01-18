package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.artifacts.SpatialRingItem;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.UUID;

public record OpenSpatialRingPacket() implements CustomPacketPayload {
    public static final Type<OpenSpatialRingPacket> TYPE = new Type<>(AscensionCraft.prefix("open_spatial_ring"));

    public static final StreamCodec<FriendlyByteBuf, OpenSpatialRingPacket> CODEC = StreamCodec.of(
            (buf, packet) -> {},
            buf -> new OpenSpatialRingPacket()
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final OpenSpatialRingPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                ItemStack spatialRing = SpatialRingUtils.findSpatialringForHotkeys(serverPlayer, true);

                if (!spatialRing.isEmpty() && spatialRing.getItem() instanceof SpatialRingItem) {
                    SpatialRingData data = SpatialRingItem.getData(spatialRing);
                    if (data != null) {
                        UUID uuid = data.getUuid();

                        data.updateAccessRecords(serverPlayer.getName().getString(), System.currentTimeMillis());

                        // Get saved scroll offset for this player and ring
                        int scrollOffset;
                        Map<UUID, Map<Player, Integer>> playerScrollOffsets = SpatialRingStorageContainer.getPlayerScrollOffsets();
                        Map<Player, Integer> ringOffsets = playerScrollOffsets.get(uuid);
                        if (ringOffsets != null) {
                            Integer savedOffset = ringOffsets.get(serverPlayer);
                            if (savedOffset != null) {
                                scrollOffset = savedOffset;
                            } else {
                                scrollOffset = 0;
                            }
                        } else {
                            scrollOffset = 0;
                        }

                        serverPlayer.openMenu(new SimpleMenuProvider(
                                (windowId, playerInventory, playerEntity) ->
                                        new SpatialRingStorageContainer(windowId, playerInventory, uuid, data.getHandler(), data.getTotalRows()),
                                spatialRing.getHoverName()
                        ), (buffer -> {
                            buffer.writeUUID(uuid);
                            buffer.writeInt(data.getExtraRows());
                            buffer.writeInt(scrollOffset); // Send scroll offset
                        }));
                    }
                }
            }
        });
    }
}