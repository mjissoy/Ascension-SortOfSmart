package net.thejadeproject.ascension.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.active_skills.SpiritualSenseActiveSkill;

import java.util.UUID;

public record SyncSpiritualSensePacket(
        UUID playerId,
        Vec3 center,
        float maxRadius,
        long startTime,
        long expansionEndTime,
        long expansionDuration,
        long activeDuration,
        int playerRealm
) implements CustomPacketPayload {

    public static final Type<SyncSpiritualSensePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_spiritual_sense"));
    public static final StreamCodec<FriendlyByteBuf, SyncSpiritualSensePacket> STREAM_CODEC = StreamCodec.of(
            SyncSpiritualSensePacket::write,  // This will now work with static method
            SyncSpiritualSensePacket::new
    );

    public SyncSpiritualSensePacket(FriendlyByteBuf buf) {
        this(
                buf.readUUID(),
                new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()),
                buf.readFloat(),
                buf.readLong(),
                buf.readLong(),
                buf.readLong(),
                buf.readLong(),
                buf.readInt()
        );
    }

    // Change to static method
    public static void write(FriendlyByteBuf buf, SyncSpiritualSensePacket packet) {
        buf.writeUUID(packet.playerId());
        buf.writeDouble(packet.center().x);
        buf.writeDouble(packet.center().y);
        buf.writeDouble(packet.center().z);
        buf.writeFloat(packet.maxRadius());
        buf.writeLong(packet.startTime());
        buf.writeLong(packet.expansionEndTime());
        buf.writeLong(packet.expansionDuration());
        buf.writeLong(packet.activeDuration());
        buf.writeInt(packet.playerRealm());
    }

    // Optional: Keep instance method for convenience
    public void write(FriendlyByteBuf buf) {
        write(buf, this);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncSpiritualSensePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Create sense data on client
            SpiritualSenseActiveSkill.SenseData senseData = new SpiritualSenseActiveSkill.SenseData(
                    packet.center(),
                    packet.maxRadius(),
                    packet.startTime(),
                    packet.expansionEndTime(),
                    packet.expansionDuration(),
                    packet.activeDuration(),
                    packet.playerRealm()
            );

            // Store in client-side map
            SpiritualSenseActiveSkill.getPlayerSenseDataMap().put(packet.playerId(), senseData);
        });
    }
}