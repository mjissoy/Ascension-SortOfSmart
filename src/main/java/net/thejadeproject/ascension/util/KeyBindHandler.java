package net.thejadeproject.ascension.util;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.cultivation.CultivationData;
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


    public static final KeyMapping CULTIVATE_KEY = new KeyMapping("key.ascension.cultivate", 67, "category.ascension.cultivation");


    public static void register() {
        IEventBus eventBus = NeoForge.EVENT_BUS;
        eventBus.addListener(EventPriority.HIGH, KeyBindHandler::handleKeyInputEvent);
    }

    public static void handleKeyInputEvent(ClientTickEvent.Post event) {
        CultivationData.setCultivating(net.thejadeproject.ascension.util.KeyBindHandler.CULTIVATE_KEY.isDown());

    }
}
