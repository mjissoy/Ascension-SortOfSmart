package net.thejadeproject.ascension.events.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class TechniqueChangeEvent extends Event {

    public  final Player player;
    public final String path;
    public final String oldTechniqueId;
    public final String newTechniqueId;
    public TechniqueChangeEvent(Player player, String path, String oldTechniqueId, String newTechniqueId){

        this.player = player;

        this.path = path;
        this.oldTechniqueId = oldTechniqueId;
        this.newTechniqueId = newTechniqueId;
    }
}
