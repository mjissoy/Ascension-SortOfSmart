package net.thejadeproject.ascension.events.custom.skills;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class PlayerSkillRemoveEvent extends Event implements ICancellableEvent {
    public final Player player;
    public final int majorRealm;
    public final int minorRealm;
    public final String path;
    public final String skill;

    public PlayerSkillRemoveEvent(Player player, int majorRealm, int minorRealm, String path, String skill) {
        this.player = player;
        this.majorRealm = majorRealm;
        this.minorRealm = minorRealm;
        this.path = path;
        this.skill = skill;
    }
}
