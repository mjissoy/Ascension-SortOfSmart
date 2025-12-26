package net.thejadeproject.ascension.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.active_skills.SpiritualSenseActiveSkill;

import java.util.UUID;

public record ClearSpiritualSensePacket(UUID playerId) implements CustomPacketPayload {

    public static final Type<ClearSpiritualSensePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "clear_spiritual_sense"));
    public static final StreamCodec<FriendlyByteBuf, ClearSpiritualSensePacket> STREAM_CODEC = StreamCodec.of(
            ClearSpiritualSensePacket::write,  // This will now work with static method
            ClearSpiritualSensePacket::new
    );

    public ClearSpiritualSensePacket(FriendlyByteBuf buf) {
        this(buf.readUUID());
    }

    // Change to static method
    public static void write(FriendlyByteBuf buf, ClearSpiritualSensePacket packet) {
        buf.writeUUID(packet.playerId());
    }

    // Optional: Keep instance method for convenience
    public void write(FriendlyByteBuf buf) {
        write(buf, this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ClearSpiritualSensePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Clear sense data on client
            SpiritualSenseActiveSkill.getPlayerSenseDataMap().remove(packet.playerId());
            SpiritualSenseActiveSkill.getPlayerSenseInfoMap().remove(packet.playerId());
        });
    }
}