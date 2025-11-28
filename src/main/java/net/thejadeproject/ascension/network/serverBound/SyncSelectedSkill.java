package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.util.ModAttachments;

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
        System.out.println("syncing selected skill");
        System.out.println(payload.slot);
        String id = context.player().getData(ModAttachments.PLAYER_SKILL_DATA).activeSkillContainer.getSkillIdList().get(payload.slot);
        System.out.println(id);
        ResourceLocation skillId = null;
        if(!id.isEmpty()){
            skillId = ResourceLocation.bySeparator(id,':');
        }
        System.out.println(skillId);
        context.player().getData(ModAttachments.PLAYER_DATA).setSelectedSkillId(skillId);

    }
}