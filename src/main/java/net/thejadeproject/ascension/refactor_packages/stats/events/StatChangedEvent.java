package net.thejadeproject.ascension.refactor_packages.stats.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;

public class StatChangedEvent extends Event {

    private final Player player;
    private final Stat stat;

    public StatChangedEvent(Player player, Stat stat){
        this.player = player;
        this.stat = stat;
    }

    public Player getPlayer() {
        return player;
    }

    public Stat getStat() {
        return stat;
    }
}
