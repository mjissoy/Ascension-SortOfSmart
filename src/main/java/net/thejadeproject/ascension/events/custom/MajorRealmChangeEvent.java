package net.thejadeproject.ascension.events.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughData;

public class MajorRealmChangeEvent extends Event {



    public  final Player player;
    public final String pathId;
    public final int oldRealm;
    public final int newRealm;
    public final double stability;
    public final IBreakthroughData breakthroughData;
    public MajorRealmChangeEvent(Player player,String path, int oldRealm,int newRealm,double stability,IBreakthroughData breakthroughData){
        this.pathId = path;
        this.oldRealm = oldRealm;
        this.newRealm = newRealm;
        this.player = player;
        this.stability  = stability;
        this.breakthroughData = breakthroughData;
    }

}
