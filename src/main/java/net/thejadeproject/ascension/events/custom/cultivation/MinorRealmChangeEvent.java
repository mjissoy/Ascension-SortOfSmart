package net.thejadeproject.ascension.events.custom.cultivation;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class MinorRealmChangeEvent extends Event {
    public  final Player player;
    public final String pathId;
    public final int oldRealm;
    public final int newRealm;
    public MinorRealmChangeEvent(Player player,String path, int oldRealm,int newRealm){
        this.pathId = path;
        this.oldRealm = oldRealm;
        this.newRealm = newRealm;
        this.player = player;

    }
}
