package net.thejadeproject.ascension.refactor_packages.network.server_bound.cultivation;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.network.client_bound.entity_data.attributes.SyncAttributeHolder;

public record UpdateSuppressionValue(ResourceLocation attributeId, double value) implements CustomPacketPayload {
    public static final Type<UpdateSuppressionValue> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "update_suppression_value"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateSuppressionValue> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC,
                    UpdateSuppressionValue::attributeId,
                    ByteBufCodecs.DOUBLE,
                    UpdateSuppressionValue::value,
                    UpdateSuppressionValue::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(UpdateSuppressionValue payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Holder<Attribute> holder = BuiltInRegistries.ATTRIBUTE
                    .getHolder(payload.attributeId()).orElse(null);
            if (holder == null) return;

            ServerPlayer player = (ServerPlayer) context.player();
            var entityData = player.getData(ModAttachments.ENTITY_DATA);
            if (entityData == null) return;

            var container = entityData.getAscensionAttributeHolder().getAttribute(holder);
            if (container == null) return;

            double clamped = Math.max(0.0, Math.min(1.0, payload.value()));
            container.setSuppressionPercent(clamped);

            entityData.getAscensionAttributeHolder().updateAttributes(entityData);

            PacketDistributor.sendToPlayer(
                    player,
                    new SyncAttributeHolder(entityData.getAscensionAttributeHolder())
            );
        });
    }
}