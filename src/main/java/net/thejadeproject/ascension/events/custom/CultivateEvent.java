package net.thejadeproject.ascension.events.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class CultivateEvent extends Event {
    public final Player player;
    public final Double baseRate;
    public CultivateEvent(Player player,double baseRate){
        this.player = player;
        this.baseRate = baseRate;
    }

}
