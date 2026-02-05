package net.thejadeproject.ascension.progression.physiques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.constants.SkillRemoveSource;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.events.custom.*;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.events.custom.skills.PlayerSkillRemoveEvent;
import net.thejadeproject.ascension.guis.easygui.elements.HoverableLabel;
import net.thejadeproject.ascension.progression.physiques.data.IPhysiqueData;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;


//if you want some sort of extra progress data separate from standard use direct nbt write
//make sure to remove it in onRemoveSpiritRoot


public class GenericPhysique implements IPhysique{

    public Map<String,Double> pathBonuses;
    public Map<String,Double> otherBonuses;
    public final String title;
    public ITextureData textureData;

    public Component description = Component.empty();

    private Supplier<IPhysiqueData> dataSupplier= ()-> null;;

    private List<AcquirableSkillData> acquisitionSkills = new ArrayList<>();

    public GenericPhysique(String title, Map<String, Double> pathBonuses, Map<String, Double> otherBonuses) {
        this.pathBonuses = pathBonuses;
        this.otherBonuses = otherBonuses;
        this.title = title;

    }

    @Override
    public void onRealmChangeEvent(RealmChangeEvent event) {
        IPhysique.super.onRealmChangeEvent(event);

        updatePlayerSkills(
                event.player,
                event.pathId,
                event.newMajorRealm,
                event.newMinorRealm
        );
    }

    public GenericPhysique setSkillList(List<AcquirableSkillData> skillList){
        this.acquisitionSkills = skillList;
        return this;
    }
    public GenericPhysique setPhysiqueCard(ITextureData textureData){
        this.textureData = textureData;
        return this;
    }


    @Override
    public Component getDescription() {
        return description;
    }

    @Override
    public Component getFullDescription() {
        return Component.empty().append(getDescription()).append("\n").append(getDisplayPaths());
    }

    public GenericPhysique setDescription(Component description){
        this.description = description;
        return this;
    }

    @Override
    public IRealmChangeHandler getRealmChangeHandler() {
        return null;
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



    //TODO replace String title with Component Title
    @Override
    public Component getDisplayTitle() {
        return Component.literal(title);
    }

    @Override
    public Component getDisplayPaths() {

        MutableComponent component = Component.empty();


        component.append("Path Efficiencies:").withStyle(ChatFormatting.BOLD);
        for (Map.Entry<String ,Double> value : pathBonuses.entrySet()){
            String name = "§b[Essence Path]";
            if(value.getKey().equals("ascension:body")) name = "§6[Body Path]";
            if(value.getKey().equals("ascension:intent")) name = "§5[Intent Path]";

            component.append("\n"+name + ": "+value.getValue().toString());
        }
        return component;
    }

    //TODO move this code over to the ui
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


    }

    public void removePlayerSkills(Player player){
        for(AcquirableSkillData skillData : acquisitionSkills){
            if(skillData.permanent() || skillData.fixed()) continue;
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(skillData.skillId());
            PlayerSkillRemoveEvent removeEvent = new PlayerSkillRemoveEvent(player,skillData.skillId(), SkillRemoveSource.PHYSIQUE_CHANGE);
            if(removeEvent.isCanceled()) return;
            player.getData(ModAttachments.PLAYER_SKILL_DATA).removeSkill(skillData.skillId().toString());
            skill.onSkillRemoved(player);
        }
    }
    @Override
    public void onPhysiqueAcquisition(Player player) {
        for(AcquirableSkillData skillData : acquisitionSkills){

            player.getData(ModAttachments.PLAYER_SKILL_DATA)
                    .addSkill(skillData.skillId(),skillData.fixed(),skillData.permanent());
        }
        for(String path : pathBonuses.keySet()){
            updatePlayerSkills(player,path,0,0);
        }
    }
    @Override
    public void onSkillRemoveEvent(PlayerSkillRemoveEvent event){
        if(event.skillRemoveType == SkillRemoveSource.FORCE_REMOVE) return;
        for(AcquirableSkillData skillData : acquisitionSkills){
            if(event.skill.equals(skillData.skillId())){

                if(event.skillRemoveType != SkillRemoveSource.PHYSIQUE_CHANGE){
                    // skill removed was not from a physique but exists on this physique so cannot be removed
                    event.setCanceled(true);
                    return;
                }
                //permanent takes priority over fixed
                if(skillData.permanent()) {
                    event.setCanceled(true);
                    return;
                }
                if(skillData.fixed()){
                    event.setCanceled(true);
                    return;
                }

            }
        }
    }

    @Override
    public void onRemovePhysique(Player player) {

        removePlayerSkills(player);
    }
    @Override
    public IPhysique setDataSupplier(Supplier<IPhysiqueData> dataSupplier) {
        this.dataSupplier = dataSupplier;
        return this;
    }
    @Override
    public IPhysiqueData getPhysiqueDataInstance(CompoundTag tag){
        IPhysiqueData data = dataSupplier.get();
        data.readData(tag);
        return data;
    }
    @Override
    public IPhysiqueData getPhysiqueDataInstance(RegistryFriendlyByteBuf buf){
        IPhysiqueData data = dataSupplier.get();
        data.decode(buf);
        return data;
    }


}
