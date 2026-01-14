package net.thejadeproject.ascension.progression.skills;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.constants.SkillType;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.events.custom.TechniqueChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPassiveSkill implements ISkill{
    public String path;
    public Component title;
    public ITextureData skillIcon;
    public List<MutableComponent> skillDescription = new ArrayList<>(); //for static descriptions
    public AbstractPassiveSkill(Component title){
        this.title = title;
    }

    /******SKILL DESCRIPTION STUFF*****/

    @Override
    public Component getSkillTitle() {
        return title;
    }

    //used for static skills
    public AbstractPassiveSkill setSkillDescription(List<MutableComponent> components){
        skillDescription = components;
        return this;
    }

    @Override
    public List<MutableComponent> getSkillDescription(Player player) {
        return skillDescription;
    }

    /******SKILL ICON STUFF*****/
    public AbstractPassiveSkill setSkillIcon(ITextureData textureData){
        this.skillIcon = textureData;
        return this;
    }
    @Override
    public ITextureData skillIcon() {
        return skillIcon;
    }



    @Override
    public String getSkillPath() {
        return path;
    }


    public void onRealmChange(RealmChangeEvent event){}
    public void onTechniqueChange(TechniqueChangeEvent event){}
    public void onPhysiqueChange(PhysiqueChangeEvent  event){

    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(CompoundTag tag) {
        return null;
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(RegistryFriendlyByteBuf buf) {
        return null;
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance() {
        return null;
    }

    @Override
    public SkillType getType() {
        return SkillType.PASSIVE;
    }
}
