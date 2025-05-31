package net.thejadeproject.ascension.keybinds;

import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.thejadeproject.ascension.cultivation.ClientCultivationData;
import net.thejadeproject.ascension.cultivation.NetworkHandler;

public class KeyHandler {
    private static boolean wasCultivating = false;

    public static boolean isCultivating(Player player) {
        if (player.level().isClientSide) {
            boolean isDown = KeyBindings.CULTIVATE_KEY.isDown();
            if (isDown && !wasCultivating) {
                NetworkHandler.sendCultivationStart(player.getUUID());
            }
            wasCultivating = isDown;
            return isDown;
        }
        return player.getPersistentData().getBoolean("isCultivating");
    }

    public static void onClientTick(ClientTickEvent event) {
        boolean isDown = KeyBindings.CULTIVATE_KEY.isDown();
        ClientCultivationData.setCultivating(isDown);
    }
}
