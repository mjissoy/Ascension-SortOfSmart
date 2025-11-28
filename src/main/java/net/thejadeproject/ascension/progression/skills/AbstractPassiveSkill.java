package net.thejadeproject.ascension.progression.skills;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.events.custom.TechniqueChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;

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

    /******SKILL DATA STUFF*****/

    @Override
    public boolean isFixedSkill() {
        //todo uses nbt
        return false;
    }
    @Override
    public void setFixedSkill(boolean fixedSkill) {
       //todo uses nbt data.
    }

    @Override
    public String getSkillPath() {
        return path;
    }

    @Override
    public ISkillData getSkillData(CompoundTag tag) {
        return null;
    }

    @Override
    public ISkillData getSkillData() {
        return null;
    }
    public void onRealmChange(RealmChangeEvent event){}
    public void onTechniqueChange(TechniqueChangeEvent event){}
    public void onPhysiqueChange(PhysiqueChangeEvent  event){

    }
}
