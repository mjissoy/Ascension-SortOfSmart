package net.thejadeproject.ascension.events.custom.cultivation;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughData;

public abstract class RealmChangeEvent extends Event {
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

    public static class Pre extends RealmChangeEvent {

        public Pre(Player player, String pathId, int oldMajorRealm, int newMajorRealm, int oldMinorRealm, int newMinorRealm, double stability, IBreakthroughData breakthroughData) {
            super(player, pathId, oldMajorRealm, newMajorRealm, oldMinorRealm, newMinorRealm, stability, breakthroughData);
        }
    }
    public static class Post extends RealmChangeEvent{

        public Post(Player player, String pathId, int oldMajorRealm, int newMajorRealm, int oldMinorRealm, int newMinorRealm, double stability, IBreakthroughData breakthroughData) {
            super(player, pathId, oldMajorRealm, newMajorRealm, oldMinorRealm, newMinorRealm, stability, breakthroughData);
        }
        public Post(RealmChangeEvent.Pre previousEvent){
            super(previousEvent.player,previousEvent.pathId,previousEvent.oldMajorRealm,previousEvent.newMajorRealm,previousEvent.oldMinorRealm,previousEvent.newMinorRealm,previousEvent.stability,previousEvent.breakthroughData);
        }
    }


}
