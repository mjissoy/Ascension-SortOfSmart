package net.thejadeproject.ascension.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.active_skills.metal_dao.OreSightActiveSkill;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record SyncOreSightPacket(
        UUID playerId,
        Map<BlockPos, BlockData> glowingBlocks,
        long durationSeconds
) implements CustomPacketPayload {

    public record BlockData(int color, long expirationTime, ResourceLocation blockId) {}

    public static final Type<SyncOreSightPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_ore_sight")
    );

    public static final StreamCodec<FriendlyByteBuf, SyncOreSightPacket> STREAM_CODEC = StreamCodec.of(
            SyncOreSightPacket::write,
            SyncOreSightPacket::new
    );

    public SyncOreSightPacket(FriendlyByteBuf buf) {
        this(
                buf.readUUID(),
                buf.readMap(
                        b -> BlockPos.of(b.readLong()),
                        b -> new BlockData(
                                b.readInt(),
                                b.readLong(),
                                b.readResourceLocation()
                        )
                ),
                buf.readLong()
        );
    }

    public static void write(FriendlyByteBuf buf, SyncOreSightPacket packet) {
        buf.writeUUID(packet.playerId());
        buf.writeMap(
                packet.glowingBlocks(),
                (b, pos) -> b.writeLong(pos.asLong()),
                (b, data) -> {
                    b.writeInt(data.color());
                    b.writeLong(data.expirationTime());
                    b.writeResourceLocation(data.blockId());
                }
        );
        buf.writeLong(packet.durationSeconds());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncOreSightPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Convert BlockData to client-side OreGlowData
            Map<BlockPos, OreSightActiveSkill.OreGlowData> clientGlowingBlocks = new HashMap<>();

            for (Map.Entry<BlockPos, BlockData> entry : packet.glowingBlocks().entrySet()) {
                Block block = BuiltInRegistries.BLOCK.get(entry.getValue().blockId());
                if (block != null) {
                    clientGlowingBlocks.put(
                            entry.getKey(),
                            new OreSightActiveSkill.OreGlowData(
                                    entry.getValue().color(),
                                    entry.getValue().expirationTime(),
                                    block
                            )
                    );
                }
            }

            // Store in client-side static field (we need to add this to OreSightActiveSkill)
            OreSightActiveSkill.updateClientGlowingBlocks(packet.playerId(), clientGlowingBlocks, packet.durationSeconds());
        });
    }
}