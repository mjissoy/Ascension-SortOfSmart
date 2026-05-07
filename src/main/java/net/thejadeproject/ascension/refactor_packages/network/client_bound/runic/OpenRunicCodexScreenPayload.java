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
import net.thejadeproject.ascension.refactor_packages.gui.screens.runic.RunicCodexScreen;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPathHelper;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPlayerData;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record OpenRunicCodexScreenPayload(
        List<ResourceLocation> knownRunes,
        List<ResourceLocation> discoveredSequences
) implements CustomPacketPayload {

    public static final Type<OpenRunicCodexScreenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "open_runic_codex_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenRunicCodexScreenPayload> STREAM_CODEC =
            StreamCodec.of(OpenRunicCodexScreenPayload::encode, OpenRunicCodexScreenPayload::decode);

    public static void sendTo(ServerPlayer player) {
        if (player == null) {
            return;
        }

        RunicPlayerData data = RunicPathHelper.getRunicData(player);

        List<ResourceLocation> knownRunes = data.getKnownRunes().stream()
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .toList();

        List<ResourceLocation> discoveredSequences = data.getDiscoveredSequences().stream()
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .toList();

        PacketDistributor.sendToPlayer(player, new OpenRunicCodexScreenPayload(knownRunes, discoveredSequences));
    }

    public static void encode(RegistryFriendlyByteBuf buf, OpenRunicCodexScreenPayload packet) {
        writeResourceLocationList(buf, packet.knownRunes);
        writeResourceLocationList(buf, packet.discoveredSequences);
    }

    public static OpenRunicCodexScreenPayload decode(RegistryFriendlyByteBuf buf) {
        List<ResourceLocation> knownRunes = readResourceLocationList(buf);
        List<ResourceLocation> discoveredSequences = readResourceLocationList(buf);

        return new OpenRunicCodexScreenPayload(knownRunes, discoveredSequences);
    }

    private static void writeResourceLocationList(RegistryFriendlyByteBuf buf, List<ResourceLocation> ids) {
        buf.writeInt(ids.size());

        for (ResourceLocation id : ids) {
            ByteBufUtil.encodeString(buf, id.toString());
        }
    }

    private static List<ResourceLocation> readResourceLocationList(RegistryFriendlyByteBuf buf) {
        int size = buf.readInt();
        List<ResourceLocation> ids = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ids.add(ByteBufUtil.readResourceLocation(buf));
        }

        return ids;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(OpenRunicCodexScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                open(payload.knownRunes, payload.discoveredSequences);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void open(List<ResourceLocation> knownRunes, List<ResourceLocation> discoveredSequences) {
        Minecraft.getInstance().setScreen(new RunicCodexScreen(knownRunes, discoveredSequences));
    }
}