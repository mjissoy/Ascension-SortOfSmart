package net.thejadeproject.ascension.network.clientBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;

public record SyncAttackDamageAttribute(double attackDamage) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncAttackDamageAttribute> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "sync_attack_damage_attribute"));
    public static final StreamCodec<ByteBuf, SyncAttackDamageAttribute> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE,
            SyncAttackDamageAttribute::attackDamage,
            SyncAttackDamageAttribute::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(SyncAttackDamageAttribute payload, IPayloadContext context) {
        context.player().getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(payload.attackDamage());
    }
}