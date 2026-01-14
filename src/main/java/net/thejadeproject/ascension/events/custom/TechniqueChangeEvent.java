package net.thejadeproject.ascension.events.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.progression.techniques.data.ITechniqueData;

public abstract class TechniqueChangeEvent extends Event {

    public  final Player player;
    public final String path;
    public final String oldTechniqueId;
    public final String newTechniqueId;
    public final ITechniqueData oldTechniqueData;
    public TechniqueChangeEvent(Player player, String path, String oldTechniqueId, String newTechniqueId, ITechniqueData oldTechniqueData){

        this.player = player;

        this.path = path;
        this.oldTechniqueId = oldTechniqueId;
        this.newTechniqueId = newTechniqueId;
        this.oldTechniqueData = oldTechniqueData;
    }

    public static class Pre extends TechniqueChangeEvent{

        public Pre(Player player, String path, String oldTechniqueId, String newTechniqueId, ITechniqueData oldTechniqueData) {
            super(player, path, oldTechniqueId, newTechniqueId, oldTechniqueData);
        }
    }
    public static class Post extends TechniqueChangeEvent{

        public Post(Player player, String path, String oldTechniqueId, String newTechniqueId, ITechniqueData oldTechniqueData) {
            super(player, path, oldTechniqueId, newTechniqueId, oldTechniqueData);
        }
        public Post(Pre previousEvent){
            super(previousEvent.player,previousEvent.path,previousEvent.oldTechniqueId,previousEvent.newTechniqueId,previousEvent.oldTechniqueData);
        }
    }


}
