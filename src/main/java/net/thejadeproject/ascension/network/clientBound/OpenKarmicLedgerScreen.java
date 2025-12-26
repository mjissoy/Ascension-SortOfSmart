package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public record OpenKarmicLedgerScreen(
        int karmaValue,
        String karmaRank,
        Map<String, Integer> mobKills,
        int playerKills
) implements CustomPacketPayload {

    public static final Type<OpenKarmicLedgerScreen> TYPE =
            new Type<>(AscensionCraft.prefix("open_karmic_ledger"));

    public static final StreamCodec<ByteBuf, OpenKarmicLedgerScreen> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    OpenKarmicLedgerScreen::karmaValue,
                    ByteBufCodecs.STRING_UTF8,
                    OpenKarmicLedgerScreen::karmaRank,
                    ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.INT),
                    OpenKarmicLedgerScreen::mobKills,
                    ByteBufCodecs.INT,
                    OpenKarmicLedgerScreen::playerKills,
                    OpenKarmicLedgerScreen::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}