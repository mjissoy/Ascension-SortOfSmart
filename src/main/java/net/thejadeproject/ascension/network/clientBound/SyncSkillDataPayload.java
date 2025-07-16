package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.PlayerData;
import net.thejadeproject.ascension.util.ModAttachments;

public record SyncSkillDataPayload(String skillId, boolean fixed) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncSkillDataPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_skill_data"));
    public static final StreamCodec<ByteBuf, SyncSkillDataPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SyncSkillDataPayload::skillId,
            ByteBufCodecs.BOOL,
            SyncSkillDataPayload::fixed,
            SyncSkillDataPayload::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncSkillDataPayload payload, IPayloadContext context) {

        PlayerData data = context.player().getData(ModAttachments.PLAYER_DATA);
        PlayerData.SkillData skillData = data.getSkill(payload.skillId);
        skillData.fixed = payload.fixed;

    }
}