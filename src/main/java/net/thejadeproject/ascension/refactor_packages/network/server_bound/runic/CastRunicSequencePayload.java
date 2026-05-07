package net.thejadeproject.ascension.refactor_packages.network.server_bound.runic;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.runic.casting.RunicCastingResult;
import net.thejadeproject.ascension.refactor_packages.runic.casting.RunicSequenceMatcher;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufUtil;

import java.util.ArrayList;
import java.util.List;

public record CastRunicSequencePayload(List<ResourceLocation> runes) implements CustomPacketPayload {

    public static final Type<CastRunicSequencePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "cast_runic_sequence"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CastRunicSequencePayload> STREAM_CODEC =
            StreamCodec.of(CastRunicSequencePayload::encode, CastRunicSequencePayload::decode);

    public static void encode(RegistryFriendlyByteBuf buf, CastRunicSequencePayload packet) {
        buf.writeInt(packet.runes.size());

        for (ResourceLocation runeId : packet.runes) {
            ByteBufUtil.encodeString(buf, runeId.toString());
        }
    }

    public static CastRunicSequencePayload decode(RegistryFriendlyByteBuf buf) {
        int runeCount = buf.readInt();
        List<ResourceLocation> runes = new ArrayList<>();

        for (int i = 0; i < runeCount; i++) {
            runes.add(ByteBufUtil.readResourceLocation(buf));
        }

        return new CastRunicSequencePayload(runes);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(CastRunicSequencePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }

            RunicCastingResult result = RunicSequenceMatcher.tryCast(player, payload.runes);

            if (result.isSuccess()) {
                player.displayClientMessage(
                        Component.translatable("ascension.runic.cast.success", result.getSequenceId().toString()),
                        true
                );
                return;
            }

            player.displayClientMessage(
                    Component.translatable("ascension.runic.cast.failure", result.getFailureReason()),
                    true
            );
        });
    }
}
