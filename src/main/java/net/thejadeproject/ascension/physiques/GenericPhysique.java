package net.thejadeproject.ascension.physiques;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationData;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.MinorRealmChangeEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.skills.ISkill;
import net.thejadeproject.ascension.skills.skill_lists.AcquirableSkillData;
import net.thejadeproject.ascension.skills.skill_lists.SkillList;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//if you want some sort of extra progress data separate from standard use direct nbt write
//make sure to remove it in onRemoveSpiritRoot


public class GenericPhysique implements IPhysique{

    public Map<String,Double> pathBonuses;
    public Map<String,Double> otherBonuses;
    public final String title;
    public ITextureData textureData;
    public SkillList skillList;

    public GenericPhysique(String title, Map<String,Double> pathBonuses, Map<String,Double> otherBonuses){
        this(title,pathBonuses,otherBonuses,new ArrayList<>());
    }
    public GenericPhysique(String title, Map<String,Double> pathBonuses, Map<String,Double> otherBonuses,List<AcquirableSkillData> skillList){
        this.pathBonuses = pathBonuses;
        this.otherBonuses = otherBonuses;
        this.title = title;
        this.skillList = new SkillList(skillList);
    }
    public GenericPhysique setPhysiqueCard(ITextureData textureData){
        this.textureData = textureData;
        return this;
    }
    @Override
    public List<String> getDisplayPathBonuses() {
        List<String> bonuses = new ArrayList<>();
        for(Map.Entry<String,Double> entry : pathBonuses.entrySet()){
            bonuses.add(entry.getKey() + ": "+entry.getValue().toString());
        }
        return bonuses;
    }

    @Override
    public List<String> getDisplayOtherBonuses() {
        List<String> bonuses = new ArrayList<>();
        for(Map.Entry<String,Double> entry : otherBonuses.entrySet()){
            bonuses.add(entry.getKey() + ": "+entry.getValue().toString());
        }
        return bonuses;
    }

    @Override
    public void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event) {

        if(event.pathID != null && pathBonuses.containsKey(event.pathID)) event.addMultiplier(pathBonuses.get(event.pathID));
        if(event.ascensionAttributeID != null && otherBonuses.containsKey(event.ascensionAttributeID)) event.addMultiplier(otherBonuses.get(event.ascensionAttributeID));

    }

    @Override
    public ITextureData getPhysiqueImage() {
        return textureData;
    }

    @Override
    public SkillList getSkillList() {
        return skillList;
    }


    @Override
    public String getDisplayTitle() {
        return title;
    }


    @Override
    public void onMajorRealmIncrease(MajorRealmChangeEvent event) {
        if(event.oldRealm > event.newRealm) return;
        updatePlayerSkills(event.player,event.path,event.newRealm,CultivationData.PlayerCultivationData.getMinorRealm(event.path, event.player));
    }

    @Override
    public void onMinorRealmIncrease(MinorRealmChangeEvent event) {
        if(event.oldRealm > event.newRealm)return;
        updatePlayerSkills(event.player,event.path,CultivationData.PlayerCultivationData.getMajorRealm(event.path, event.player),event.newRealm);
    }
    public void updatePlayerSkills(Player player, String path, int majorRealm, int minorRealm){
        List<Pair<String,Boolean>> newSkills = skillList.getSkillsOfPathAndRealm(path,majorRealm,minorRealm);
        for(Pair<String,Boolean> skillData :newSkills){
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillData.getA(),':'));
            String skillType = "Passive";
            if(skill instanceof AbstractActiveSkill) skillType = "Active";
            boolean fixed = false;
            if(player.getPersistentData().getCompound("Skills").getCompound(skillType).hasUUID(skillData.getA())){
                fixed = player.getPersistentData().getCompound("Skills").getCompound(skillType).getBoolean(skillData.getA());
            }
            player.getPersistentData().getCompound("Skills").getCompound(skillType).putBoolean(skillType,fixed || skillData.getB());
            skill.onSkillAdded(player);
        }

    }
}
