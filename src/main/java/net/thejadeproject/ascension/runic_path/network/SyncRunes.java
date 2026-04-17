package net.thejadeproject.ascension.runic_path.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record SyncRunes(
        List<ResourceLocation> unlockedRunes,
        List<RealmRuneSelection> selectedRunes
) implements CustomPacketPayload {

    public static final Type<SyncRunes> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_runes"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncRunes> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    SyncRunes::unlockedRunes,
                    RealmRuneSelection.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    SyncRunes::selectedRunes,
                    SyncRunes::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(SyncRunes payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Set<ResourceLocation> syncedUnlocked = new LinkedHashSet<>(payload.unlockedRunes());
            ClientRunicData.setUnlockedRunes(syncedUnlocked);

            Map<Integer, List<ResourceLocation>> syncedSelected = new HashMap<>();
            for (RealmRuneSelection entry : payload.selectedRunes()) {
                syncedSelected.put(entry.majorRealm(), new ArrayList<>(entry.runes()));
            }
            ClientRunicData.setSelectedRunes(syncedSelected);
        });
    }
}