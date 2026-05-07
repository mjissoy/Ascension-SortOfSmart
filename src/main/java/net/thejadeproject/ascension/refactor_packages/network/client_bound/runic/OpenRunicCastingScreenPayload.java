package net.thejadeproject.ascension.refactor_packages.network.client_bound.runic;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.gui.screens.runic.RunicCastingScreen;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPathHelper;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufUtil;

import java.util.ArrayList;
import java.util.List;

public record OpenRunicCastingScreenPayload(
        int maxRuneSlots,
        int durationSeconds,
        List<ResourceLocation> usableRunes
) implements CustomPacketPayload {

    public static final Type<OpenRunicCastingScreenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "open_runic_casting_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenRunicCastingScreenPayload> STREAM_CODEC =
            StreamCodec.of(OpenRunicCastingScreenPayload::encode, OpenRunicCastingScreenPayload::decode);

    public static void sendTo(ServerPlayer player) {
        if (player == null || !player.hasData(ModAttachments.ENTITY_DATA)) {
            return;
        }

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        PacketDistributor.sendToPlayer(player, new OpenRunicCastingScreenPayload(
                RunicPathHelper.getRuneSlotCount(player, true),
                RunicPathHelper.getCastingDurationSeconds(entityData),
                RunicPathHelper.getUsableKnownRunes(player)
        ));
    }

    public static void encode(RegistryFriendlyByteBuf buf, OpenRunicCastingScreenPayload packet) {
        buf.writeInt(packet.maxRuneSlots);
        buf.writeInt(packet.durationSeconds);
        buf.writeInt(packet.usableRunes.size());

        for (ResourceLocation runeId : packet.usableRunes) {
            ByteBufUtil.encodeString(buf, runeId.toString());
        }
    }

    public static OpenRunicCastingScreenPayload decode(RegistryFriendlyByteBuf buf) {
        int maxRuneSlots = buf.readInt();
        int durationSeconds = buf.readInt();
        int runeCount = buf.readInt();
        List<ResourceLocation> usableRunes = new ArrayList<>();

        for (int i = 0; i < runeCount; i++) {
            usableRunes.add(ByteBufUtil.readResourceLocation(buf));
        }

        return new OpenRunicCastingScreenPayload(maxRuneSlots, durationSeconds, usableRunes);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(OpenRunicCastingScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                open(payload.maxRuneSlots, payload.durationSeconds, payload.usableRunes);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void open(int maxRuneSlots, int durationSeconds, List<ResourceLocation> usableRunes) {
        Minecraft.getInstance().setScreen(new RunicCastingScreen(maxRuneSlots, durationSeconds, usableRunes));
    }
}
