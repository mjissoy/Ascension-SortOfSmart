package net.thejadeproject.ascension.events.custom.skills;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import net.thejadeproject.ascension.constants.SkillRemoveSource;

public class PlayerSkillRemoveEvent extends Event implements ICancellableEvent {
    public final Player player;
    public final ResourceLocation skill;
    public final SkillRemoveSource skillRemoveType;

    public PlayerSkillRemoveEvent(Player player, ResourceLocation skill, SkillRemoveSource skillRemoveType) {
        this.player = player;
        this.skill = skill;
        this.skillRemoveType = skillRemoveType;
    }
}
