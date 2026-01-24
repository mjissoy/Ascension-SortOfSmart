package net.thejadeproject.ascension.progression.skills;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.constants.SkillType;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;

import java.util.List;
//TODO add an IActiveSkill as well
public interface ISkill {
    default void onSkillAdded(Player player){
        
    };
    default void onSkillRemoved(Player player){};

    default String getSkillPath(){return "ascension:neutral";}

    default Component getSkillDescription(Player player){return Component.empty();}
    default ITextureData skillIcon(){return null;}
    default Component getSkillTitle(){
        return Component.empty();
    };
    SkillType getType();

    IPersistentSkillData getPersistentDataInstance(CompoundTag tag);
    IPersistentSkillData getPersistentDataInstance();
    IPersistentSkillData getPersistentDataInstance(RegistryFriendlyByteBuf buf);

}
