package net.thejadeproject.ascension.events.custom.skills;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.thejadeproject.ascension.progression.skills.data.casting.CastSource;

public class SkillPreCastEvent extends PlayerEvent implements ICancellableEvent {
    private final String spellId;
    private final CastSource castSource;

    public SkillPreCastEvent(Player player,String spellId,CastSource castSource) {
        super(player);
        this.spellId = spellId;
        this.castSource = castSource;
    }
    public String getSpellId() {
        return this.spellId;
    }

    public CastSource getCastSource() {
        return this.castSource;
    }
}
