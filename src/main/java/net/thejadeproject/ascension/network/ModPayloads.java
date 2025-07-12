package net.thejadeproject.ascension.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.network.clientBound.SyncGeneratedPhysique;
import net.thejadeproject.ascension.network.serverBound.SyncCultivationPayload;
import net.thejadeproject.ascension.network.serverBound.TriggerGeneratePhysique;

public class ModPayloads {
    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(AscensionCraft.MOD_ID).versioned("1.0");
        registrar.playToServer(
                SyncCultivationPayload.TYPE,
                SyncCultivationPayload.STREAM_CODEC,
                SyncCultivationPayload::handlePayload

        );
        registrar.playToClient(
                SyncAttackDamageAttribute.TYPE,
                SyncAttackDamageAttribute.STREAM_CODEC,
                SyncAttackDamageAttribute::handlePayload
        );
        registrar.playToClient(
                SyncGeneratedPhysique.TYPE,
                SyncGeneratedPhysique.STREAM_CODEC,
                SyncGeneratedPhysique::handlePayload
        );
        registrar.playToServer(
                TriggerGeneratePhysique.TYPE,
                TriggerGeneratePhysique.STREAM_CODEC,
                TriggerGeneratePhysique::handlePayload
        );
    }
}
