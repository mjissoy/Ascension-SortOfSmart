package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.network.clientBound.UpdateAttackDamageSuppressorModifier;
import net.thejadeproject.ascension.network.clientBound.UpdateSpeedSuppressorModifier;

public record UpdateSuppressorValuePayload(String attribute, double value) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateSuppressorValuePayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "update_suppressor_value"));
    public static final StreamCodec<ByteBuf, UpdateSuppressorValuePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            UpdateSuppressorValuePayload::attribute,
            ByteBufCodecs.DOUBLE,
            UpdateSuppressorValuePayload::value,
            UpdateSuppressorValuePayload::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(UpdateSuppressorValuePayload payload, IPayloadContext context) {


        context.enqueueWork(()->{
            double value = Math.clamp(payload.value,0.001,1);

            ResourceLocation id = ResourceLocation.bySeparator(payload.attribute,':');
            Holder<Attribute> attributeHolder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(BuiltInRegistries.ATTRIBUTE.get(id));
            context.player().getAttribute(attributeHolder).addOrReplacePermanentModifier(
                    new AttributeModifier(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"suppression_modifier"),
                            value-1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            );

            if(attributeHolder.is(Attributes.ATTACK_DAMAGE.getKey())){

                PacketDistributor.sendToPlayer((ServerPlayer) context.player(),new UpdateAttackDamageSuppressorModifier(value-1));
            }
            if(attributeHolder.is(Attributes.MOVEMENT_SPEED.getKey())){

                PacketDistributor.sendToPlayer((ServerPlayer) context.player(),new UpdateSpeedSuppressorModifier(value-1));
            }
        });
    }
}
