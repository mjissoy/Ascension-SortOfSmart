package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.CastingInstance;
import net.thejadeproject.ascension.util.ModAttachments;

import java.util.UUID;

public record ServerCastSkillPayload(String uuid,int castTicksElapsed,String castType,String skillID) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ServerCastSkillPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "server_cast_skill"));
    public static final StreamCodec<ByteBuf, ServerCastSkillPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ServerCastSkillPayload::uuid,
            ByteBufCodecs.VAR_INT,
            ServerCastSkillPayload::castTicksElapsed,
            ByteBufCodecs.STRING_UTF8,
            ServerCastSkillPayload::castType,
            ByteBufCodecs.STRING_UTF8,
            ServerCastSkillPayload::skillID,
            ServerCastSkillPayload::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(ServerCastSkillPayload payload, IPayloadContext context) {
        //context.player().getData(ModAttachments.PLAYER_DATA).addCastingInstance(CastingInstance.fromPayload(payload));
    }
}
