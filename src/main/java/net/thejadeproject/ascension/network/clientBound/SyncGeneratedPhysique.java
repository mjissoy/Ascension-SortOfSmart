package net.thejadeproject.ascension.network.clientBound;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
//import net.thejadeproject.ascension.guis.easygui.screens.GeneratePhysiqueScreen;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.events.custom.PhysiqueGeneratedEvent;
import net.thejadeproject.ascension.util.ModAttachments;

import java.nio.charset.StandardCharsets;
import java.util.List;

public record SyncGeneratedPhysique(String generated_physique, byte[] other_physiques)  implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncGeneratedPhysique> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_generated_physique"));
    public static final StreamCodec<ByteBuf, SyncGeneratedPhysique> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SyncGeneratedPhysique::generated_physique,
            ByteBufCodecs.BYTE_ARRAY,
            SyncGeneratedPhysique::other_physiques,
            SyncGeneratedPhysique::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncGeneratedPhysique payload, IPayloadContext context) {
        //deserialize physiques
        String joined = new String(payload.other_physiques(), StandardCharsets.UTF_8);
        NeoForge.EVENT_BUS.post(new PhysiqueGeneratedEvent(payload.generated_physique(),joined.split(";")));

    }
}