package net.thejadeproject.ascension.events.karma;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.util.ModAttachments;
import org.jetbrains.annotations.NotNull;

public record KarmaSyncPayload(int karmaValue, String karmaRank) implements CustomPacketPayload {

    public static final Type<KarmaSyncPayload> TYPE = new Type<>(AscensionCraft.prefix("karma_sync"));

    public static final StreamCodec<ByteBuf, KarmaSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            KarmaSyncPayload::karmaValue,
            ByteBufCodecs.STRING_UTF8,
            KarmaSyncPayload::karmaRank,
            KarmaSyncPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                KarmaData karmaData = player.getData(ModAttachments.PLAYER_KARMA);
                karmaData.setKarmaValue(this.karmaValue);
            }
        });
    }
}
