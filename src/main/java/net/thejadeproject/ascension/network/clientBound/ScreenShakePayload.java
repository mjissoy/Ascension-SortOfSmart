package net.thejadeproject.ascension.network.clientBound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;

public record ScreenShakePayload(int durationTicks, float intensity) implements CustomPacketPayload {
    public static final Type<ScreenShakePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "screen_shake")
    );

    public static final StreamCodec<FriendlyByteBuf, ScreenShakePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ScreenShakePayload::durationTicks,
            ByteBufCodecs.FLOAT,
            ScreenShakePayload::intensity,
            ScreenShakePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(ScreenShakePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.flow().isClientbound()) {
                net.thejadeproject.ascension.events.TribulationClientEvents.handleScreenShake(payload);
            }
        });
    }
}