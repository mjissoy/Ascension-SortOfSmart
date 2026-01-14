package net.thejadeproject.ascension.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.active_skills.SpiritualSenseActiveSkill;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record SyncSpiritualSenseEntitiesPacket(UUID playerId, List<EntityData> entities,
                                               int totalEntities, int totalGroups) implements CustomPacketPayload {

    // Simple data record for transmission
    public record EntityData(
            int entityId,
            UUID entityUuid,
            String name,
            int essenceRealm,
            int bodyRealm,
            int intentRealm,
            float health,
            float maxHealth,
            boolean isPlayer,
            boolean canSeeDetails,
            int count,
            boolean isGrouped
    ) {}

    public static final Type<SyncSpiritualSenseEntitiesPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_spiritual_sense_entities"));

    public static final StreamCodec<FriendlyByteBuf, SyncSpiritualSenseEntitiesPacket> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeUUID(packet.playerId());
                buf.writeInt(packet.entities().size());
                for (EntityData data : packet.entities()) {
                    buf.writeInt(data.entityId());
                    buf.writeUUID(data.entityUuid());
                    buf.writeUtf(data.name());
                    buf.writeInt(data.essenceRealm());
                    buf.writeInt(data.bodyRealm());
                    buf.writeInt(data.intentRealm());
                    buf.writeFloat(data.health());
                    buf.writeFloat(data.maxHealth());
                    buf.writeBoolean(data.isPlayer());
                    buf.writeBoolean(data.canSeeDetails());
                    buf.writeInt(data.count());
                    buf.writeBoolean(data.isGrouped());
                }
                buf.writeInt(packet.totalEntities());
                buf.writeInt(packet.totalGroups());
            },
            buf -> {
                UUID playerId = buf.readUUID();
                int size = buf.readInt();
                List<EntityData> entities = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    EntityData data = new EntityData(
                            buf.readInt(),
                            buf.readUUID(),
                            buf.readUtf(),
                            buf.readInt(),
                            buf.readInt(),
                            buf.readInt(),
                            buf.readFloat(),
                            buf.readFloat(),
                            buf.readBoolean(),
                            buf.readBoolean(),
                            buf.readInt(),
                            buf.readBoolean()
                    );
                    entities.add(data);
                }
                int totalEntities = buf.readInt();
                int totalGroups = buf.readInt();
                return new SyncSpiritualSenseEntitiesPacket(playerId, entities, totalEntities, totalGroups);
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncSpiritualSenseEntitiesPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            // Convert EntityData list to EntityInfo list for client-side use
            List<SpiritualSenseActiveSkill.EntityInfo> entityList = new ArrayList<>();
            for (EntityData data : packet.entities()) {
                SpiritualSenseActiveSkill.EntityInfo entityInfo;

                if (data.isGrouped()) {
                    entityInfo = new SpiritualSenseActiveSkill.EntityInfo(
                            data.name(),
                            net.minecraft.world.entity.EntityType.byString(data.name()).orElse(net.minecraft.world.entity.EntityType.PIG),
                            data.count(),
                            data.health() * data.count(),
                            data.maxHealth() * data.count()
                    );
                } else {
                    entityInfo = new SpiritualSenseActiveSkill.EntityInfo(
                            data.entityId(),
                            data.entityUuid(),
                            data.name(),
                            data.essenceRealm(),
                            data.bodyRealm(),
                            data.intentRealm(),
                            data.health(),
                            data.maxHealth(),
                            data.isPlayer(),
                            net.minecraft.world.entity.EntityType.byString(data.name()).orElse(net.minecraft.world.entity.EntityType.PIG),
                            data.canSeeDetails()
                    );
                }
                entityList.add(entityInfo);
            }

            // Create PlayerSenseInfo and store on client
            SpiritualSenseActiveSkill.PlayerSenseInfo senseInfo =
                    new SpiritualSenseActiveSkill.PlayerSenseInfo(entityList, packet.totalEntities(), packet.totalGroups());
            SpiritualSenseActiveSkill.getPlayerSenseInfoMap().put(packet.playerId(), senseInfo);
        });
    }
}