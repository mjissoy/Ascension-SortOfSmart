package net.thejadeproject.ascension.events.karma;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class KarmaNetworkHandler {

    public static void registerKarmaPayloads(RegisterPayloadHandlersEvent event) {
    }

    public static void sendToPlayer(KarmaSyncPayload packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}
