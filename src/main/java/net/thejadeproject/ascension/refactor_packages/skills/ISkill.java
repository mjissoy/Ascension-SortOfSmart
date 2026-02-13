package net.thejadeproject.ascension.refactor_packages.skills;

import net.minecraft.world.entity.player.Player;

public interface ISkill {


    void onAdded(Player player);
    void onRemoved(Player player,IPersistentSkillData persistentData);
}
