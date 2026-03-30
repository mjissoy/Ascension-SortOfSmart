package net.thejadeproject.ascension.refactor_packages.skill_casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;

import java.util.ArrayList;

public class SkillHotBar {


    public final int MAX_SLOTS;
    private final HotBarSkillSlot[] skillSlots;

    private int activeSlot = 0;

    public SkillHotBar(int maxSlots){
        MAX_SLOTS = maxSlots;
        skillSlots = new HotBarSkillSlot[maxSlots];
        for(int i=0;i<MAX_SLOTS;i++) skillSlots[i] = new HotBarSkillSlot();
    }

    public void setActiveSlot(IEntityData entityData,int slot){
        if(activeSlot != slot && skillSlots[activeSlot].getSkill() != null){
            if(skillSlots[activeSlot].getSkill() instanceof ICastableSkill castableSkill) castableSkill.unselected(entityData);
            this.activeSlot = slot;
            if(skillSlots[activeSlot].getSkill() != null && skillSlots[activeSlot].getSkill() instanceof  ICastableSkill newCastableSkill) newCastableSkill.selected(entityData);
        }

    }
    public ISkill getActiveSkill(){
        return skillSlots[activeSlot].getSkill();
    }
    public IPreCastData getPreCastData(int slot){
        return skillSlots[slot].getPreCastData();
    }
    public int getActiveSlot(){return activeSlot;}
    public int getSlot(ResourceLocation skill){
        for(int i = 0;i<skillSlots.length;i++){
            if(skill.equals(skillSlots[i].getSkillKey())) return i;
        }
        return -1;
    }
    public ResourceLocation getActiveSkillKey(){
        return skillSlots[activeSlot].getSkillKey();
    }
    public void slotSkill(IEntityData entityData,ResourceLocation skill,int slot){
        skillSlots[slot].setSkill(skill,entityData);
    }
    public void unSlotSkill(IEntityData entityData,int slot){
        skillSlots[slot].unSlotSKill(entityData);
    }
    public void unSlotSkill(IEntityData entityData,ResourceLocation skill){
        for(HotBarSkillSlot skillSlot : skillSlots){
            if(skillSlot.getSkillKey().equals(skill)) skillSlot.unSlotSKill(entityData);
        }
    }

    /*
        used when it is detected a change was made to the skill list
     */
    public void refreshSkillSlots(IEntityData entityData){
        //TODO
    }

}
