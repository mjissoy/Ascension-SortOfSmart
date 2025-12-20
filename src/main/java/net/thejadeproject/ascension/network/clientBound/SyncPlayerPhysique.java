package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public record SyncPlayerPhysique(String physique) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncPlayerPhysique> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_player_physique"));
    public static final StreamCodec<ByteBuf, SyncPlayerPhysique> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SyncPlayerPhysique::physique,
            SyncPlayerPhysique::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncPlayerPhysique payload, IPayloadContext context) {

        context.player().setData(ModAttachments.PHYSIQUE,payload.physique());

    }
}