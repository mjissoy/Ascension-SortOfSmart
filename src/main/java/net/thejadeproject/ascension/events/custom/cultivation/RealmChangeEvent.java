package net.thejadeproject.ascension.events.custom.cultivation;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughData;

public class RealmChangeEvent extends Event {
    public  final Player player;
    public final String pathId;
    public final int oldMajorRealm;
    public final int newMajorRealm;
    public final int oldMinorRealm;
    public final int newMinorRealm;
    public final double stability;
    public final IBreakthroughData breakthroughData;
    public RealmChangeEvent(Player player, String pathId, int oldMajorRealm, int newMajorRealm, int oldMinorRealm, int newMinorRealm, double stability, IBreakthroughData breakthroughData) {
        this.player = player;
        this.pathId = pathId;
        this.oldMajorRealm = oldMajorRealm;
        this.newMajorRealm = newMajorRealm;
        this.oldMinorRealm = oldMinorRealm;
        this.newMinorRealm = newMinorRealm;
        this.stability = stability;
        this.breakthroughData = breakthroughData;
    }
    public int getTotalMinorRealmsChanged(){
        int totalOldMinorRealms = oldMajorRealm*9+oldMinorRealm;
        int totalNewMinorRealms = newMajorRealm*9+newMinorRealm;
        return totalNewMinorRealms-totalOldMinorRealms;
    }
    public int getMajorRealmsChanged(){
        return newMajorRealm-oldMajorRealm;
    }
}
