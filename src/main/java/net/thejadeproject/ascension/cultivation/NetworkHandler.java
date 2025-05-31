package net.thejadeproject.ascension.cultivation;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.thejadeproject.ascension.AscensionCraft;

public class NetworkHandler {
    private static final ResourceLocation CHANNEL_ID = new ResourceLocation(AscensionCraft.MOD_ID, "main");
    private static final String PROTOCOL_VERSION = "1";

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(AscensionCraft.MOD_ID)
                .versioned(PROTOCOL_VERSION)
                .optional();
        registrar.play(
                CultivationUpdatePacket.ID,
                CultivationUpdatePacket::new,
                handler -> handler.server(NetworkHandler::handleCultivationUpdate)
        );
    }

    private static void handleCultivationUpdate(CultivationUpdatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null) {
                ClientCultivationData.update(packet.cultivationData());
            }
        });
    }

    public static void sendCultivationUpdate(ServerPlayer player) {
        CompoundTag cultivationData = player.getPersistentData().getCompound("Cultivation");
        PacketDistributor.sendToPlayer(player, new CultivationUpdatePacket(cultivationData));
    }

    public record CultivationUpdatePacket(CompoundTag cultivationData) implements CustomPacketPayload {
        public static final ResourceLocation ID = new ResourceLocation(AscensionCraft.MOD_ID, "cultivation_update");

        public CultivationUpdatePacket(FriendlyByteBuf buf) {
            this(buf.readNbt());
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeNbt(cultivationData);
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
