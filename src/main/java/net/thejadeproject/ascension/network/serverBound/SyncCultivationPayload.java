package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.util.ModAttachments;

public record SyncCultivationPayload(String path,Boolean newState) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncCultivationPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_cultivation_state"));
    public static final StreamCodec<ByteBuf, SyncCultivationPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SyncCultivationPayload::path,
            ByteBufCodecs.BOOL,
            SyncCultivationPayload::newState,
            SyncCultivationPayload::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncCultivationPayload payload, IPayloadContext context) {
        System.out.println("Received Packet");
        context.player().getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(payload.path).setCultivating(payload.newState);
        CompoundTag data = context.player().getPersistentData().getCompound("Cultivation");
        data.putBoolean("CultivationState",payload.newState);
        context.player().getPersistentData().put("Cultivation",data);
    }
}
