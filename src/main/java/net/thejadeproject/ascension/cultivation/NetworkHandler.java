package net.thejadeproject.ascension.cultivation;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;

import java.util.UUID;

public class NetworkHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar(AscensionCraft.MOD_ID)
                .versioned("1")
                .playBidirectional(
                        CultivationUpdatePayload.TYPE,
                        CultivationUpdatePayload.STREAM_CODEC,
                        new DirectionalPayloadHandler<>(NetworkHandler::handleCultivationUpdate, NetworkHandler::handleCultivationUpdate
                        ))
                .playBidirectional(
                        CultivationStartPayLoad.TYPE,
                        CultivationStartPayLoad.STREAM_CODEC,
                        new DirectionalPayloadHandler<>(NetworkHandler::handleCultivationStart, NetworkHandler::handleCultivationStart)
                );
    }

    private static void handleCultivationUpdate(CultivationUpdatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            CultivationData.ClientCultivationData.update(payload.data());
        });
    }

    private static void handleCultivationStart(CultivationUpdatePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            CultivationData.startCultivation(payload.data());
        });
    }


    public static void sendCultivationStart(UUID playerId) {
        PacketDistributor.sendToServer(new CultivationStartPayLoad(playerId));
    }

    public record CultivationStartPayLoad(UUID playerId) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<CultivationUpdatePayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "cultivation_start"));

        public static final StreamCodec<ByteBuf, CultivationUpdatePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.COMPOUND_TAG,
                CultivationUpdatePayload::data,
                CultivationUpdatePayload::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }


    public static void sendCultivationUpdate(ServerPlayer player, CompoundTag data) {
        PacketDistributor.sendToPlayer(player, new CultivationUpdatePayload(data));
    }

    public record CultivationUpdatePayload(CompoundTag data) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<CultivationUpdatePayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "cultivation_update"));


        public static final StreamCodec<ByteBuf, CultivationUpdatePayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.COMPOUND_TAG,
                CultivationUpdatePayload::data,
                CultivationUpdatePayload::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}