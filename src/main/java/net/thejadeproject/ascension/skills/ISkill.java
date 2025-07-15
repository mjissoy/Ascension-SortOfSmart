package net.thejadeproject.ascension.skills;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface ISkill {
    default void onSkillAdded(Player player){};
    default void onSkillRemoved(Player player){};

    default String getSkillPath(){return "ascension:neutral";}


    default ITextureData skillIcon(){return null;}
}
