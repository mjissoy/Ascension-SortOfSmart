package net.thejadeproject.ascension.refactor_packages.skills.castable;

import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;

public interface ICastableSkill extends ISkill {

    void onEquip(Player player);
    void onUnEquip(Player player,IPreCastData preCastData);
}
