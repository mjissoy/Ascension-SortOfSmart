package net.thejadeproject.ascension.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.active_skills.metal_dao.OreSightActiveSkill;

import java.util.UUID;

public record ClearOreSightPacket(UUID playerId) implements CustomPacketPayload {

    public static final Type<ClearOreSightPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "clear_ore_sight")
    );

    public static final StreamCodec<FriendlyByteBuf, ClearOreSightPacket> STREAM_CODEC = StreamCodec.of(
            ClearOreSightPacket::write,
            ClearOreSightPacket::new
    );

    public ClearOreSightPacket(FriendlyByteBuf buf) {
        this(buf.readUUID());
    }

    public static void write(FriendlyByteBuf buf, ClearOreSightPacket packet) {
        buf.writeUUID(packet.playerId());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClearOreSightPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            OreSightActiveSkill.clearClientGlowingBlocks(packet.playerId());
        });
    }
}