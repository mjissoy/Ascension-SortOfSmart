package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.util.ModAttachments;

/**
 *
 * @param slot the skill bar slot
 * @param skillId the id of the skill
 * @param slotSkill if true add skill id to slot if false remove skill id from slot
 */
public record ChangeSkillSlotSpellPayload(int slot,String skillId,boolean slotSkill)implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ChangeSkillSlotSpellPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "change_skill_slot_spell"));
    public static final StreamCodec<ByteBuf, ChangeSkillSlotSpellPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ChangeSkillSlotSpellPayload::slot,
            ByteBufCodecs.STRING_UTF8,
            ChangeSkillSlotSpellPayload::skillId,
            ByteBufCodecs.BOOL,
            ChangeSkillSlotSpellPayload::slotSkill,
            ChangeSkillSlotSpellPayload::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(ChangeSkillSlotSpellPayload payload, IPayloadContext context) {

        PlayerSkillData playerSkillData = context.player().getData(ModAttachments.PLAYER_SKILL_DATA);

        playerSkillData.modifySkillSlot(payload.slot, payload.skillId, payload.slotSkill);
    }
}
