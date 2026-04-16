package net.thejadeproject.ascension.runic_path.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.runic_path.ClientRunicData;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public record SyncRunes(List<ResourceLocation> runes) implements CustomPacketPayload {

    public static final Type<SyncRunes> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_runes"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRunes> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    SyncRunes::runes,
                    SyncRunes::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(SyncRunes payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Set<ResourceLocation> synced = new LinkedHashSet<>(payload.runes());
            ClientRunicData.setUnlockedRunes(synced);
        });
    }
}