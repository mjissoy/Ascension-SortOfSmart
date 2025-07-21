package net.thejadeproject.ascension.progression.physiques;

import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationData;
import net.thejadeproject.ascension.events.custom.*;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.util.ModAttachments;
import net.thejadeproject.ascension.util.ModTags;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


//if you want some sort of extra progress data separate from standard use direct nbt write
//make sure to remove it in onRemoveSpiritRoot


public class GenericPhysique implements IPhysique{

    public Map<String,Double> pathBonuses;
    public Map<String,Double> otherBonuses;
    public final String title;
    public ITextureData textureData;
    public SkillList skillList;
    public Supplier<MajorRealmChangeEvent> majorRealmAction;
    public Supplier<MinorRealmChangeEvent> minorRealmAction;
    public List<MutableComponent> description;
    public GenericPhysique(String title, Map<String,Double> pathBonuses, Map<String,Double> otherBonuses){
        this.pathBonuses = pathBonuses;
        this.otherBonuses = otherBonuses;
        this.title = title;
    }

    public GenericPhysique setOnMajorRealmIncrease(Supplier<MajorRealmChangeEvent> action){
        majorRealmAction = action;
        return this;
    }
    public GenericPhysique setOnMinorRealmIncrease(Supplier<MinorRealmChangeEvent> action){
        minorRealmAction = action;
        return this;
    }

    public GenericPhysique setSkillList(List<AcquirableSkillData> skillList){
        this.skillList = new SkillList(skillList);
        return this;
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
    public List<MutableComponent> getDescription() {
        List<MutableComponent> extraInfo = new ArrayList<>();
        if(description != null) extraInfo = new ArrayList<>(description);
        extraInfo.add(
                Component.literal("Path Efficiencies:").withStyle(ChatFormatting.BOLD)
        );
        for (Map.Entry<String ,Double> value : pathBonuses.entrySet()){
            String name = "§b[Essence Path]";
            if(value.getKey().equals("ascension:body")) name = "§6[Body Path]";
            if(value.getKey().equals("ascension:intent")) name = "§5[Intent Path]";

            extraInfo.add(Component.literal(name).append(": "+value.getValue().toString()));
        }

        extraInfo.add(
                Component.literal("Dao Efficiencies:").withStyle(ChatFormatting.BOLD)
        );
        for (Map.Entry<String ,Double> value : otherBonuses.entrySet()){
            extraInfo.add(ModTags.Items.tagDisplayData.get(ItemTags.create(ResourceLocation.bySeparator(value.getKey(),':'))).copy().append(": "+value.getValue().toString()));
        }

        return extraInfo;
    }

    public GenericPhysique setDescription(List<MutableComponent> description){
        this.description = description;
        return this;
    }
    @Override
    public void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event) {

        if(event.pathID != null && pathBonuses.containsKey(event.pathID)) event.addMultiplier(pathBonuses.get(event.pathID));
        for(String attributeId : event.ascensionAttributeID()){
            if( otherBonuses.containsKey(attributeId)) event.addMultiplier(otherBonuses.get(attributeId));

        }

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
        updatePlayerSkills(event.player,event.pathId,event.newRealm,CultivationData.PlayerCultivationData.getMinorRealm(event.pathId, event.player));
    }

    @Override
    public void onMinorRealmIncrease(MinorRealmChangeEvent event) {
        if(event.oldRealm > event.newRealm)return;
        updatePlayerSkills(event.player,event.pathId,CultivationData.PlayerCultivationData.getMajorRealm(event.pathId, event.player),event.newRealm);
    }
    public void updatePlayerSkills(Player player, String path, int majorRealm, int minorRealm){
        if(skillList == null) return;
        List<Pair<String,Boolean>> newSkills = skillList.getSkillsOfPathAndRealm(path,majorRealm,minorRealm);

        for(Pair<String,Boolean> skillData :newSkills){
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillData.getA(),':'));
            String skillType = "Passive";
            if(skill instanceof AbstractActiveSkill) skillType = "Active";
            boolean fixed = false;

            if(player.getData(ModAttachments.PLAYER_DATA).hasSkill(skillData.getA())){
                fixed = player.getData(ModAttachments.PLAYER_DATA).getSkill(skillData.getA()).fixed;
            }
            player.getData(ModAttachments.PLAYER_DATA).getSkill(skillData.getA()).fixed = fixed || skillData.getB();

            skill.onSkillAdded(player);
        }

    }

    public void removePlayerSkills(Player player){

    }
    @Override
    public void onPhysiqueAcquisition(Player player) {
        System.out.println("physique acquired");
        IPhysique.super.onPhysiqueAcquisition(player);
        for(String path : pathBonuses.keySet()){
            updatePlayerSkills(player,path,0,0);
        }
    }

    @Override
    public void onRemovePhysique(Player player) {
        IPhysique.super.onRemovePhysique(player);
        removePlayerSkills(player);
    }
}
