package net.thejadeproject.ascension.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.menus.spatialrings.OpenSpatialRingPacket;
import net.thejadeproject.ascension.network.clientBound.*;
import net.thejadeproject.ascension.network.serverBound.SyncCultivationPayload;
import net.thejadeproject.ascension.network.serverBound.ToggleTabletDropModePayload;
import net.thejadeproject.ascension.network.serverBound.TriggerGeneratePhysique;
import net.thejadeproject.ascension.network.serverBound.TriggerMajorRealmBreakthrough;

public class ModPayloads {
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
                OpenPickPhysiqueScreen.TYPE,
                OpenPickPhysiqueScreen.STREAM_CODEC,
                OpenPickPhysiqueScreen::handlePayload
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
        registrar.playToServer(
                TriggerMajorRealmBreakthrough.TYPE,
                TriggerMajorRealmBreakthrough.STREAM_CODEC,
                TriggerMajorRealmBreakthrough::handlePayload
        );
        registrar.playToClient(
                SyncSkillDataPayload.TYPE,
                SyncSkillDataPayload.STREAM_CODEC,
                SyncSkillDataPayload::handlePayload
        );
        registrar.playToClient(
                SyncPathDataPayload.TYPE,
                SyncPathDataPayload.STREAM_CODEC,
                SyncPathDataPayload::handlePayload
        );
        registrar.playToClient(
                SyncPlayerPhysique.TYPE,
                SyncPlayerPhysique.STREAM_CODEC,
                SyncPlayerPhysique::handlePayload
        );

        registrar.playToServer(
                OpenSpatialRingPacket.TYPE,  // ADD THIS LINE
                OpenSpatialRingPacket.CODEC, // ADD THIS LINE
                OpenSpatialRingPacket::handle // ADD THIS LINE
        );
        registrar.playToServer(
                ToggleTabletDropModePayload.TYPE,
                ToggleTabletDropModePayload.STREAM_CODEC,
                ToggleTabletDropModePayload::handlePayload
        );


    }
}
