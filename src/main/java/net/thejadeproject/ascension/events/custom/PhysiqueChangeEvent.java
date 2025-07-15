package net.thejadeproject.ascension.events.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;


public class PhysiqueChangeEvent extends Event {

    public final Player player;
    public final String oldPhysique;
    public final String newPhysique;
    public PhysiqueChangeEvent(Player player,String oldPhysique,String newPhysique){
        this.player = player;
        this.oldPhysique = oldPhysique;
        this.newPhysique =newPhysique;

    }
}
