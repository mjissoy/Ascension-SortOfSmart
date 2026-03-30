package net.thejadeproject.ascension.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.karma.KarmaNetworkHandler;
import net.thejadeproject.ascension.events.karma.KarmaSyncPayload;

import net.thejadeproject.ascension.network.clientBound.*;

import net.thejadeproject.ascension.network.serverBound.*;
import net.thejadeproject.ascension.network.serverBound.input.ChangePlayerInputState;
import net.thejadeproject.ascension.refactor_packages.network.client_bound.entity_data.SyncEntityForm;
import net.thejadeproject.ascension.refactor_packages.network.client_bound.entity_data.skills.SyncHeldSkills;


public class ModPayloads {
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(AscensionCraft.MOD_ID).versioned("1.0");

        //=================================== CLIENT======================================
        registrar.playToClient(
                SyncHeldSkills.TYPE,
                SyncHeldSkills.STREAM_CODEC,
                SyncHeldSkills::handlePayload
        );
        registrar.playToClient(
                SyncEntityForm.TYPE,
                SyncEntityForm.STREAM_CODEC,
                SyncEntityForm::handlePayload
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



        //===================================== SERVER ==================================



        registrar.playToServer(
                ToggleTabletDropModePayload.TYPE,
                ToggleTabletDropModePayload.STREAM_CODEC,
                ToggleTabletDropModePayload::handlePayload
        );



        registrar.playToServer(
                ChangePlayerInputState.TYPE,
                ChangePlayerInputState.STREAM_CODEC,
                ChangePlayerInputState::handlePayload

        );




    }
}
