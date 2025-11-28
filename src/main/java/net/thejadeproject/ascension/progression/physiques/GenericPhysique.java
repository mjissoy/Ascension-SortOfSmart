package net.thejadeproject.ascension.progression.physiques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.events.custom.*;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.guis.easygui.elements.HoverableLabel;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.util.ModAttachments;
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
    public SkillList skillList = new SkillList(List.of());
     public List<MutableComponent> description;
    public IRealmChangeHandler realmChangeHandler;


    public GenericPhysique(String title, Map<String,Double> pathBonuses, Map<String,Double> otherBonuses,IRealmChangeHandler realmChangeHandler){
        this.pathBonuses = pathBonuses;
        this.otherBonuses = otherBonuses;
        this.title = title;
        this.realmChangeHandler = realmChangeHandler;

    }

    @Override
    public void onRealmChangeEvent(RealmChangeEvent event) {
        IPhysique.super.onRealmChangeEvent(event);
        if(event.getMajorRealmsChanged() < 0 ||(event.getMajorRealmsChanged() == 0 && event.getTotalMinorRealmsChanged() <0) ) return;
        updatePlayerSkills(
                event.player,
                event.pathId,
                event.newMajorRealm,
                event.newMinorRealm
        );
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
    public List<Component> getDescription() {
        List<Component> extraInfo = new ArrayList<>();
        if(description != null) extraInfo = new ArrayList<>(description);
        return extraInfo;
    }

    @Override
    public List<Component> getFullDescription() {
        var list = new ArrayList<>(getDescription());
        list.addAll(getDisplayPaths());
        return list;
    }

    public GenericPhysique setDescription(List<MutableComponent> description){
        this.description = description;
        return this;
    }

    @Override
    public IRealmChangeHandler getRealmChangeHandler() {
        return realmChangeHandler;
    }

    @Override
    public void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event) {

        if(event.pathID != null && pathBonuses.containsKey(event.pathID)) event.addPathMultiplier(pathBonuses.get(event.pathID));
        for(String techniqueAttributeId : otherBonuses.keySet()){
            event.tryAddDao(techniqueAttributeId,otherBonuses.get(techniqueAttributeId));
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
    public List<Component> getDisplayPaths() {
        List<Component> extraInfo = new ArrayList<>();
        extraInfo.add(
                Component.literal("Path Efficiencies:").withStyle(ChatFormatting.BOLD)
        );
        for (Map.Entry<String ,Double> value : pathBonuses.entrySet()){
            String name = "§b[Essence Path]";
            if(value.getKey().equals("ascension:body")) name = "§6[Body Path]";
            if(value.getKey().equals("ascension:intent")) name = "§5[Intent Path]";

            extraInfo.add(Component.literal(name).append(": "+value.getValue().toString()));
        }
        return extraInfo;
    }

    @Override
    public List<Label> getDisplayEfficiencies(IEasyGuiScreen screen) {
        List<Label> extraInfo = new ArrayList<>();
        extraInfo.add(
                (new net.lucent.easygui.elements.other.Label.Builder())
                        .screen(screen)
                        .x(0).y(0)
                        .text(Component.literal("Dao Efficiencies:").withStyle(ChatFormatting.BOLD))
                        .customScaling(0.5)
                        .build()
        );
        for (Map.Entry<String ,Double> value : otherBonuses.entrySet()){
            Component text = AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(value.getKey(),':')).getDisplayTitle().copy().append(": "+value.getValue().toString());
            HoverableLabel hoverableLabel = (new HoverableLabel.Builder())
                    .screen(screen)
                    .x(0).y(0)
                    .text(text)
                    .customScaling(0.5)
                    .dao(AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(value.getKey(),':')))
                    .build();
            extraInfo.add(hoverableLabel);
        }
        return extraInfo;
    }



    public void updatePlayerSkills(Player player, String path, int majorRealm, int minorRealm){
        if(skillList == null) return;
        List<Pair<String,Boolean>> newSkills = skillList.getSkillsOfPathAndRealm(path,majorRealm,minorRealm);

        for(Pair<String,Boolean> skillData :newSkills){
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillData.getA(),':'));
            String skillType = "Passive";
            if(skill instanceof AbstractActiveSkill) skillType = "Active";
            player.getData(ModAttachments.PLAYER_SKILL_DATA).addSkill(skillData.getA(),skillType,skillData.getB(),skill.getSkillData());
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
