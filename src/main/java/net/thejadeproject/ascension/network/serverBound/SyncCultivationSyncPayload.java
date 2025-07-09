package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;

public record SyncCultivationSyncPayload(Boolean newState) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncCultivationSyncPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_cultivation_state"));
    public static final StreamCodec<ByteBuf, SyncCultivationSyncPayload> STREAM_CODEC = StreamCodec.composite(

            ByteBufCodecs.BOOL,
            SyncCultivationSyncPayload::newState,
            SyncCultivationSyncPayload::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncCultivationSyncPayload payload, IPayloadContext context) {
        System.out.println("Received Packet");
        CompoundTag data = context.player().getPersistentData().getCompound("Cultivation");
        data.putBoolean("CultivationState",payload.newState);
        context.player().getPersistentData().put("Cultivation",data);
    }
}
