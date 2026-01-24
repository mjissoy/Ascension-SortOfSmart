package net.thejadeproject.ascension.network.serverBound;

import io.netty.buffer.ByteBuf;
import net.lucent.formation_arrays.api.cores.IFormationCore;
import net.lucent.formation_arrays.api.formations.node.IFormationNode;
import net.lucent.formation_arrays.blocks.block_entities.formation_cores.AbstractFormationCoreBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.formations.formation_nodes.BarrierFormation;

import java.util.UUID;

public record PlayerTryHitBarrier( String formationId, BlockPos core) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerTryHitBarrier> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "player_try_hit_barrier"));
    public static final StreamCodec<ByteBuf, PlayerTryHitBarrier> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            PlayerTryHitBarrier::formationId,
            BlockPos.STREAM_CODEC,
            PlayerTryHitBarrier::core,
            PlayerTryHitBarrier::new
    );
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    public static void handlePayload(PlayerTryHitBarrier payload, IPayloadContext context) {


        UUID formationId = UUID.fromString(payload.formationId);
        if(context.player().level().getBlockEntity(payload.core) instanceof IFormationCore core){
            IFormationNode node = core.getFormationNode(formationId);
            if(node instanceof BarrierFormation){
                ((BarrierFormation) node).handlePlayerAttackAttempt(context.player());
            }
        }
    }
}
