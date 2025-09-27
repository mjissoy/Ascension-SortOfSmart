package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.cultivation.player.CultivationData;
import net.thejadeproject.ascension.cultivation.player.PlayerData;
import net.thejadeproject.ascension.events.custom.MajorRealmChangeEvent;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

public record TriggerMajorRealmBreakthrough(String path_id) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TriggerMajorRealmBreakthrough> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "trigger_major_realm_breakthrough"));
    public static final StreamCodec<ByteBuf, TriggerMajorRealmBreakthrough> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            TriggerMajorRealmBreakthrough::path_id,
            TriggerMajorRealmBreakthrough::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(TriggerMajorRealmBreakthrough payload, IPayloadContext context) {
        //TODO change to use breakthrough handler instead
        //TODO add verification. aka make sure they are at the correct minor realm. and roll the chance for now don't bother

        PlayerData data = context.player().getData(ModAttachments.PLAYER_DATA);
        CultivationData cultivationData = data.getCultivationData();
        CultivationData.PathData pathData = cultivationData.getPathData(payload.path_id());
        //TODO add verification. aka make sure they are at the correct minor realm. and roll the chance for now don't bother
        if(pathData.majorRealm >= CultivationSystem.getRealmNumber(payload.path_id())) return;
        ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(
                pathData.technique,
                ':'
        ));
        technique.getBreakthroughHandler().attemptBreakthrough(context.player(), payload.path_id(), technique);

    }
}
