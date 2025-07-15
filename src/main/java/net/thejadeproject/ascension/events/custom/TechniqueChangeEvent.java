package net.thejadeproject.ascension.events.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class TechniqueChangeEvent extends Event {

    public  final Player player;
    public TechniqueChangeEvent(Player player){

        this.player = player;

    }
}
