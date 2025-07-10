package net.thejadeproject.ascension.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.network.clientBound.attributeSync.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.network.serverBound.SyncCultivationSyncPayload;

public class ModPayloads {
    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(AscensionCraft.MOD_ID).versioned("1.0");
        registrar.playToServer(
                SyncCultivationSyncPayload.TYPE,
                SyncCultivationSyncPayload.STREAM_CODEC,
                SyncCultivationSyncPayload::handlePayload

        );
        registrar.playToClient(
                SyncAttackDamageAttribute.TYPE,
                SyncAttackDamageAttribute.STREAM_CODEC,
                SyncAttackDamageAttribute::handlePayload
        );
    }
}
