package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.guis.easygui.screens.GeneratePhysiqueScreen;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

//TODO
public record SkillCastSyncPayload(String spellId)  implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SkillCastSyncPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "skill_cast_sync"));
    public static final StreamCodec<ByteBuf, SkillCastSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SkillCastSyncPayload::spellId,
            SkillCastSyncPayload::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SkillCastSyncPayload payload, IPayloadContext context) {
        Player player = context.player();
        ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(payload.spellId,':'));
        player.getData(ModAttachments.PLAYER_DATA).tryCast(skill);
    }
}
