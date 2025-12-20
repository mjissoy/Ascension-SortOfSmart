package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public record SyncSelectedSkill(int slot) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncSelectedSkill> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_selected_skill"));
    public static final StreamCodec<ByteBuf, SyncSelectedSkill> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SyncSelectedSkill::slot,
            SyncSelectedSkill::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncSelectedSkill payload, IPayloadContext context) {
        
        
        String id = context.player().getData(ModAttachments.PLAYER_SKILL_DATA).activeSkillContainer.getSkillIdList().get(payload.slot);
        
        ResourceLocation skillId = null;
        if(!id.isEmpty()){
            skillId = ResourceLocation.bySeparator(id,':');
        }
        
        context.player().getData(ModAttachments.PLAYER_DATA).setSelectedSkillId(skillId);

    }
}