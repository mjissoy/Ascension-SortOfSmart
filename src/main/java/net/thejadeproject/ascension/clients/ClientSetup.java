package net.thejadeproject.ascension.clients;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.keybinds.KeyBindHandler;

public class ClientSetup {
    public static void init(IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(KeyBindHandler::onClientTick);
    }
}
