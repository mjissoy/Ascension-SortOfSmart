package net.thejadeproject.ascension.events.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

//TODO properly update to have Pre and Post
//TODO setup to have data of old physique
//TODO setup so physique listener calls on physique removed
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
