package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.screens.GeneratePhysiqueScreen;
import net.thejadeproject.ascension.util.ModAttachments;

public record OpenPickPhysiqueScreen(boolean state)  implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenPickPhysiqueScreen> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "open_physique_screen"));
    public static final StreamCodec<ByteBuf, OpenPickPhysiqueScreen> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            OpenPickPhysiqueScreen::state,
            OpenPickPhysiqueScreen::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(OpenPickPhysiqueScreen payload, IPayloadContext context) {
        Minecraft.getInstance().setScreen(new GeneratePhysiqueScreen(Component.literal("generate")));

    }
}
