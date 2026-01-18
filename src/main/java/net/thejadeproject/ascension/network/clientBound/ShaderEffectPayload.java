package net.thejadeproject.ascension.network.clientBound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;

public record ShaderEffectPayload(String effectId, int durationTicks) implements CustomPacketPayload {
    public static final Type<ShaderEffectPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "shader_effect")
    );

    public static final StreamCodec<FriendlyByteBuf, ShaderEffectPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ShaderEffectPayload::effectId,
            ByteBufCodecs.VAR_INT,
            ShaderEffectPayload::durationTicks,
            ShaderEffectPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(ShaderEffectPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.flow().isClientbound()) {
                // Optional: Implement shader effects if using Iris/Oculus
                // net.thejadeproject.ascension.client.TribulationClientEvents.handleShaderEffect(payload);
            }
        });
    }
}