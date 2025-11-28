package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.CastingInstance;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.guis.easygui.screens.GeneratePhysiqueScreen;
import net.thejadeproject.ascension.util.ModAttachments;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public record SyncCastingInstance(CastingInstance primary, List<CastingInstance> castingInstances) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncCastingInstance> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_casting_instances"));
    public static final StreamCodec<ByteBuf, SyncCastingInstance> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.of(CastingInstance::encode,CastingInstance::decode),
            SyncCastingInstance::primary,
            ByteBufCodecs.collection(
                    ArrayList::new,
                    StreamCodec.of(CastingInstance::encode,CastingInstance::decode),
                    256),
            SyncCastingInstance::castingInstances,
            SyncCastingInstance::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncCastingInstance payload, IPayloadContext context) {
        PlayerData data = context.player().getData(ModAttachments.PLAYER_DATA);
        if(data == null) return;
        data.setCastingInstances(payload.castingInstances());
        data.setPrimarySkillCastingInstance(payload.primary);
    }
}