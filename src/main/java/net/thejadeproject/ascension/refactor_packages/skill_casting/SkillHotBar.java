package net.thejadeproject.ascension.refactor_packages.skill_casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import net.thejadeproject.ascension.refactor_packages.skills.ISkill;

import java.util.ArrayList;

public class SkillHotBar {

    private final Player player;
    private final int MAX_SLOTS;
    private final HotBarSkillSlot[] skillSlots;

    private int activeSlot = 0;

    public SkillHotBar(Player player,int maxSlots){
        this.player = player;
        MAX_SLOTS = maxSlots;
        skillSlots = new HotBarSkillSlot[maxSlots];
        for(int i=0;i<MAX_SLOTS;i++) skillSlots[i] = new HotBarSkillSlot(player);
    }

    public void setActiveSlot(int slot){
        this.activeSlot = slot;
    }
    public ISkill getActiveSkill(){
        return skillSlots[activeSlot].getSkill();
    }
    public ResourceLocation getActiveSkillKey(){
        return skillSlots[activeSlot].getSkillKey();
    }
    public void slotSkill(ResourceLocation skill,int slot){
        skillSlots[slot].setSkill(skill);
    }
    public void unSlotSkill(int slot){
        skillSlots[slot].unSlotSKill();
    }
    public void unSlotSkill(ResourceLocation skill){
        for(HotBarSkillSlot skillSlot : skillSlots){
            if(skillSlot.getSkillKey().equals(skill)) skillSlot.unSlotSKill();
        }
    }

    /*
        used when it is detected a change was made to the skill list
     */
    public void refreshSkillSlots(){
        //TODO
    }

}
