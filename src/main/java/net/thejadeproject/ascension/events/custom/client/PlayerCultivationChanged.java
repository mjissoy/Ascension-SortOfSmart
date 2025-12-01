package net.thejadeproject.ascension.events.custom.client;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

import java.util.List;

public class PlayerCultivationChanged extends Event {
    public final Player player;
    public final String path;

    public PlayerCultivationChanged(Player player, String path) {
        this.player = player;
        this.path = path;

    }
}
