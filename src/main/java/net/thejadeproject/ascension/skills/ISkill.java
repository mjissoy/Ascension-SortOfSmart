package net.thejadeproject.ascension.skills;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.skills.data.CastType;

import java.util.List;

public interface ISkill {
    default void onSkillAdded(Player player){
        System.out.println("gave player : " +player +" a skill");
    };
    default void onSkillRemoved(Player player){};

    default String getSkillPath(){return "ascension:neutral";}

    default List<MutableComponent> getSkillDescription(){return List.of();}
    default ITextureData skillIcon(){return null;}

    //this skill will no be removed by physiques and techniques changing
    default boolean isFixedSkill(){return false;}
    default CastType getCastType(){return CastType.NONE;}
    void setFixedSkill(boolean fixed);
}
