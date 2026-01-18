package net.thejadeproject.ascension.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.karma.KarmaNetworkHandler;
import net.thejadeproject.ascension.events.karma.KarmaSyncPayload;
import net.thejadeproject.ascension.menus.spatialrings.OpenSpatialRingPacket;
import net.thejadeproject.ascension.network.clientBound.*;
import net.thejadeproject.ascension.network.packets.*;
import net.thejadeproject.ascension.network.serverBound.*;
import net.thejadeproject.ascension.network.serverBound.input.ChangePlayerInputState;
import net.thejadeproject.ascension.network.spatialrings.RequestSpatialRingDataPayload;
import net.thejadeproject.ascension.network.spatialrings.SyncSpatialRingInventoryPayload;
import net.thejadeproject.ascension.network.spatialrings.SyncSpatialRingUpgradesPayload;


public class ModPayloads {
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(AscensionCraft.MOD_ID).versioned("1.0");

        //=================================== CLIENT======================================
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
        registrar.playToClient(
                SyncCastingInstance.TYPE,
                SyncCastingInstance.STREAM_CODEC,
                SyncCastingInstance::handlePayload
        );
        registrar.playToClient(
                SyncPlayerQi.TYPE,
                SyncPlayerQi.STREAM_CODEC,
                SyncPlayerQi::handlePayload
        );

        registrar.playToClient(
                SyncSpiritualSensePacket.TYPE,
                SyncSpiritualSensePacket.STREAM_CODEC,
                SyncSpiritualSensePacket::handle
        );
        registrar.playToClient(
                ClearSpiritualSensePacket.TYPE,
                ClearSpiritualSensePacket.STREAM_CODEC,
                ClearSpiritualSensePacket::handle
        );
        registrar.playToClient(
                SyncSpiritualSenseEntitiesPacket.TYPE,
                SyncSpiritualSenseEntitiesPacket.STREAM_CODEC,
                SyncSpiritualSenseEntitiesPacket::handle
        );
        registrar.playToClient(
                SyncOreSightPacket.TYPE,
                SyncOreSightPacket.STREAM_CODEC,
                SyncOreSightPacket::handle
        );
        registrar.playToClient(
                ClearOreSightPacket.TYPE,
                ClearOreSightPacket.STREAM_CODEC,
                ClearOreSightPacket::handle
        );

        registrar.playToClient(
                OpenKarmicLedgerScreen.TYPE,
                OpenKarmicLedgerScreen.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        if (context.flow().isClientbound()) {
                            // Only handle on client
                            net.thejadeproject.ascension.clients.ClientPacketHandler.handleOpenKarmicLedgerScreen(payload);
                        }
                    });
                }
        );

        registrar.playToClient(
                KarmaSyncPayload.TYPE,
                KarmaSyncPayload.STREAM_CODEC,
                KarmaSyncPayload::handle
        );

        registrar.playToClient(
                SyncSpatialRingInventoryPayload.TYPE,
                SyncSpatialRingInventoryPayload.STREAM_CODEC,
                SyncSpatialRingInventoryPayload::handle
        );

        registrar.playToClient(
                SyncSpatialRingUpgradesPayload.TYPE,
                SyncSpatialRingUpgradesPayload.STREAM_CODEC,
                SyncSpatialRingUpgradesPayload::handle
        );


        //===================================== SERVER ==================================
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
        registrar.playToServer(
                ChangeSkillSlotSpellPayload.TYPE,
                ChangeSkillSlotSpellPayload.STREAM_CODEC,
                ChangeSkillSlotSpellPayload::handlePayload
        );

        registrar.playToServer(
                SyncCultivationPayload.TYPE,
                SyncCultivationPayload.STREAM_CODEC,
                SyncCultivationPayload::handlePayload

        );
        registrar.playToServer(
                SyncSelectedSkill.TYPE,
                SyncSelectedSkill.STREAM_CODEC,
                SyncSelectedSkill::handlePayload

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


        registrar.playToServer(
                ServerCastSkillPayload.TYPE,
                ServerCastSkillPayload.STREAM_CODEC,
                ServerCastSkillPayload::handlePayload

        );
        registrar.playToServer(
                ChangePlayerInputState.TYPE,
                ChangePlayerInputState.STREAM_CODEC,
                ChangePlayerInputState::handlePayload

        );

        registrar.playToServer(
                RequestSpatialRingDataPayload.TYPE,
                RequestSpatialRingDataPayload.STREAM_CODEC,
                RequestSpatialRingDataPayload::handle
        );


    }
}
