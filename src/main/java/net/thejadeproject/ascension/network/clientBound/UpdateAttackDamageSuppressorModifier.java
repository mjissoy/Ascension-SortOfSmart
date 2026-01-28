package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;

public record UpdateAttackDamageSuppressorModifier(double value) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateAttackDamageSuppressorModifier> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "update_attack_damage_suppressor_modifier"));
    public static final StreamCodec<ByteBuf, UpdateAttackDamageSuppressorModifier> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            UpdateAttackDamageSuppressorModifier::value,
            UpdateAttackDamageSuppressorModifier::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(UpdateAttackDamageSuppressorModifier payload, IPayloadContext context) {
        context.enqueueWork(()->
                {
                    context.player().getAttribute(Attributes.ATTACK_DAMAGE).addOrReplacePermanentModifier(
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"suppression_modifier"),
                                    payload.value(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    );
                }
                );
    }
}