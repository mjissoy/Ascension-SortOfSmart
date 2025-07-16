package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.PlayerData;
import net.thejadeproject.ascension.guis.easygui.screens.GeneratePhysiqueScreen;
import net.thejadeproject.ascension.util.ModAttachments;

import java.nio.charset.StandardCharsets;

public record SyncPathDataPayload(String pathId,int majorRealm,int minorRealm,double progress) implements CustomPacketPayload {
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
        PlayerData.PathData pathData = data.getPathData(payload.pathId());
        pathData.pathProgress = payload.progress();
        pathData.minorRealm = payload.minorRealm();
        pathData.majorRealm = payload.majorRealm();
    }
}