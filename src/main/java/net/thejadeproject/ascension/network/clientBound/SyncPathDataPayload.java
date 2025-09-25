package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.CultivationData;
import net.thejadeproject.ascension.cultivation.player.PlayerData;
import net.thejadeproject.ascension.util.ModAttachments;

public record SyncPathDataPayload(String pathId,int majorRealm,int minorRealm,double progress,String technique,double stabilityTicks) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncPathDataPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_path_data"));
    public static final StreamCodec<ByteBuf, SyncPathDataPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SyncPathDataPayload::pathId,
            ByteBufCodecs.INT,
            SyncPathDataPayload::majorRealm,
            ByteBufCodecs.INT,
            SyncPathDataPayload::minorRealm,
            ByteBufCodecs.DOUBLE,
            SyncPathDataPayload::progress,
            ByteBufCodecs.STRING_UTF8,
            SyncPathDataPayload::technique,
            ByteBufCodecs.DOUBLE,
            SyncPathDataPayload::stabilityTicks,
            SyncPathDataPayload::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncPathDataPayload payload, IPayloadContext context) {
        System.out.println("syncing data");
        System.out.println(payload.progress());
        PlayerData data = context.player().getData(ModAttachments.PLAYER_DATA);
        CultivationData.PathData pathData = data.getCultivationData().getPathData(payload.pathId());
        pathData.pathProgress = payload.progress();
        pathData.minorRealm = payload.minorRealm();
        pathData.majorRealm = payload.majorRealm();
        pathData.technique = payload.technique();
        pathData.stabilityCultivationTicks = payload.stabilityTicks();
    }
}