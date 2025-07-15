package net.thejadeproject.ascension.events.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;


public class PhysiqueChangeEvent extends Event {

    public  final Player player;
    public PhysiqueChangeEvent(Player player){

        this.player = player;

    }
}
