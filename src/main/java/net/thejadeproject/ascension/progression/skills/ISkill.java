package net.thejadeproject.ascension.progression.skills;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;

import java.util.List;

public interface ISkill {
    default void onSkillAdded(Player player){
        System.out.println("gave player : " +player +" a skill");
    };
    default void onSkillRemoved(Player player){};

    default String getSkillPath(){return "ascension:neutral";}
    ISkillData getSkillData(CompoundTag tag);
    ISkillData getSkillData();

    default List<MutableComponent> getSkillDescription(Player player){return List.of();}
    default ITextureData skillIcon(){return null;}
    Component getSkillTitle();
    //this skill will no be removed by physiques and techniques changing
    default boolean isFixedSkill(){return false;}

    void setFixedSkill(boolean fixed);

    ISkillData decode(RegistryFriendlyByteBuf buf);

}
