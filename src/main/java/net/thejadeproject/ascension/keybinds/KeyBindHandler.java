package net.thejadeproject.ascension.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.cultivation.ClientCultivationData;
import net.thejadeproject.ascension.cultivation.NetworkHandler;

public class KeyBindHandler {
    private static boolean wasCultivating = false;

    private KeyBindHandler() {
    }

    public static boolean isCultivating(Player player) {
        if (player.level().isClientSide) {
            boolean isDown = KeyBindHandler.CULTIVATE_KEY.isDown();
            if (isDown && !wasCultivating) {
                NetworkHandler.sendCultivationStart(player.getUUID());
            }
            wasCultivating = isDown;
            return isDown;
        }
        return player.getPersistentData().getBoolean("isCultivating");
    }


    private static final int KEY_C = 67;



    public static final KeyMapping CULTIVATE_KEY = new KeyMapping("key.ascension.cultivate", 67, "category.ascension.cultivation");



    public static void register() {
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.addListener(EventPriority.HIGH, KeyBindHandler::handleKeyInputEvent);
    }

    public static void handleKeyInputEvent(ClientTickEvent.Pre event) {
        boolean isDown = KeyBindHandler.CULTIVATE_KEY.isDown();
        ClientCultivationData.setCultivating(isDown);
    }

    public static void onClientTick(ClientTickEvent event) {
        boolean isDown = KeyBindHandler.CULTIVATE_KEY.isDown();
        ClientCultivationData.setCultivating(isDown);
    }
}
