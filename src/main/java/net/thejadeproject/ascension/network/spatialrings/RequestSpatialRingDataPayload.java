package net.thejadeproject.ascension.network.spatialrings;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.menus.spatialrings.SpatialRingData;
import net.thejadeproject.ascension.menus.spatialrings.SpatialRingManager;

import java.util.UUID;

public record RequestSpatialRingDataPayload(UUID ringUuid) implements CustomPacketPayload {

    public static final Type<RequestSpatialRingDataPayload> TYPE =
            new Type<>(AscensionCraft.prefix("request_spatial_ring_data"));

    public static final StreamCodec<FriendlyByteBuf, RequestSpatialRingDataPayload> STREAM_CODEC =
            StreamCodec.of(RequestSpatialRingDataPayload::encode, RequestSpatialRingDataPayload::decode);

    private static void encode(FriendlyByteBuf buf, RequestSpatialRingDataPayload payload) {
        buf.writeUUID(payload.ringUuid);
    }

    private static RequestSpatialRingDataPayload decode(FriendlyByteBuf buf) {
        return new RequestSpatialRingDataPayload(buf.readUUID());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final RequestSpatialRingDataPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                SpatialRingManager.get().getSpatialRing(payload.ringUuid()).ifPresent(data -> {
                    data.syncToClient(serverPlayer);
                });
            }
        });
    }
}