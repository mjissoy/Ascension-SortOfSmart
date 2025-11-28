package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.util.ModAttachments;

public record SyncPlayerQi(double qi)  implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncPlayerQi> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_player_qi"));
    public static final StreamCodec<ByteBuf, SyncPlayerQi> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            SyncPlayerQi::qi,
            SyncPlayerQi::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncPlayerQi payload, IPayloadContext context) {

        context.player().getData(ModAttachments.PLAYER_DATA).setQiNoSync(payload.qi);

    }
}