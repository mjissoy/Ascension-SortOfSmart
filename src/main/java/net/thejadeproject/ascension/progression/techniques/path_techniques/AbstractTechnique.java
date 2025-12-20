package net.thejadeproject.ascension.progression.techniques.path_techniques;

import net.lucent.easygui.elements.other.Label;
import net.lucent.easygui.interfaces.IEasyGuiScreen;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.thejadeproject.ascension.cultivation.CultivationSystem;
import net.thejadeproject.ascension.cultivation.player.realm_change_handlers.IRealmChangeHandler;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.events.custom.skills.PlayerSkillRemoveEvent;
import net.thejadeproject.ascension.guis.easygui.elements.HoverableLabel;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.ISkill;
import net.thejadeproject.ascension.progression.skills.skill_lists.AcquirableSkillData;
import net.thejadeproject.ascension.progression.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.progression.techniques.stability_handlers.StabilityHandler;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.List;

public abstract class AbstractTechnique implements ITechnique {
    public String title;
    public Double baseRate;

    public Map<String,Double> daoBonuses = new HashMap<>();
    public String path;
    public SkillList skillList = new SkillList(List.of());
    public ITextureData techniqueImage;
    public Component description = Component.empty();
    public StabilityHandler stabilityHandler;
    public IBreakthroughHandler breakthroughHandler;
    public IRealmChangeHandler realmChangeHandler;


    public AbstractTechnique(String title, double baseRate, String path, StabilityHandler stabilityHandler, IBreakthroughHandler handler,IRealmChangeHandler realmChangeHandler){
        this.title = title;
        this.baseRate = baseRate;
        this.path = path;
        this.stabilityHandler =stabilityHandler;
        this.breakthroughHandler =handler;
        this.realmChangeHandler = realmChangeHandler;
    }


    @Override
    public IBreakthroughHandler getBreakthroughHandler() {
        return breakthroughHandler;
    }
    @Override
    public IRealmChangeHandler getRealmChangeHandler() {
        return realmChangeHandler;
    }
    @Override
    public StabilityHandler getStabilityHandler() {
        return stabilityHandler;
    }

    @Override
    public void tryCultivate(Player player) {
        if(player.level().isClientSide()) return;
        if(!CultivationSystem.cultivate(player,getPath(),baseRate,getCultivationAttributes())) tryStabiliseRealm(player);
    }

    @Override
    public void tryStabiliseRealm(Player player) {
        CultivationSystem.stabiliseRealm(stabilityHandler,player,getPath(),getCultivationAttributes(),0);
    }

    @Override
    public void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event) {

        for(String techniqueAttributeId : getDaoBonuses()){
            event.tryAddDao(techniqueAttributeId,getDaoBonus(techniqueAttributeId));
        }

    }

    public AbstractTechnique setEfficiencyAttributes(Map<String,Double> efficiencyBonuses){
        this.daoBonuses = efficiencyBonuses;
        return this;
    }
    @Override
    public Set<String> getDaoBonuses() {
        return daoBonuses.keySet();
    }
    @Override
    public Double getDaoBonus(String attribute) {
        return daoBonuses.get(attribute);
    }
    @Override
    public String getPath() {
        return path;
    }

    public Component getDisplayTitle(){
        return Component.literal(title);
    }

    @Override
    public SkillList getSkillList() {
        return skillList;
    }
    public AbstractTechnique setSkillList(List<AcquirableSkillData> skillList){
        this.skillList = new SkillList(skillList);
        return this;
    }
    public double getBaseRate(){
        return baseRate;
    }
    public double getRate(){
        return  getBaseRate();
    }

    @Override
    public ITextureData getTechniqueImage() {
        return techniqueImage;
    }

    public void setTechniqueImage(ITextureData techniqueImage) {
        this.techniqueImage = techniqueImage;
    }

    @Override
    public void onRealmChangeEvent(RealmChangeEvent event) {
        System.out.println("Handler: " + System.identityHashCode(event));
        System.out.println("realm changed");
        System.out.println("major : "+event.oldMajorRealm +"->"+event.newMajorRealm);
        System.out.println("minor : "+event.oldMinorRealm +"->"+event.newMinorRealm);
        updatePlayerSkills(event.player,event.oldMajorRealm,event.newMajorRealm,event.oldMinorRealm,event.newMinorRealm);
        ITechnique.super.onRealmChangeEvent(event);
    }

    //TODO add some sort of path registry and use that for this
    @Override
    public void onTechniqueAcquisition(Player player) {
        updatePlayerSkills(player,-1,0,-1,0);
    }

    public AbstractTechnique setDescription(Component description){
        this.description = description;
        return this;
    }

    @Override
    public Component getDescription() {
        return description;
    }

    @Override
    public List<Label> getDisplayDaoEfficiencies(IEasyGuiScreen screen) {
        List<Label> extraInfo = new ArrayList<>();
        extraInfo.add(
                (new Label.Builder())
                        .screen(screen)
                        .x(0).y(0)
                        .text(Component.literal("Dao Efficiencies:").withStyle(ChatFormatting.BOLD))
                        .customScaling(0.5)
                        .build()
        );
        for (Map.Entry<String ,Double> value : daoBonuses.entrySet()){
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
    public void updatePlayerSkills(Player player,int oldMajorRealm,int newMajorRealm,int oldMinorRealm,int newMinorRealm){
        System.out.println("trying to update player skills");
        System.out.println("major : "+oldMajorRealm +"->"+newMajorRealm);
        System.out.println("minor : "+oldMinorRealm +"->"+newMinorRealm);
        if(
                (oldMajorRealm > newMajorRealm)
                || (oldMajorRealm == newMajorRealm && oldMinorRealm >= newMinorRealm)
        ){
            System.out.println("trying to remove skills");
            //realm was lowered
            List<Pair<String,Boolean>> skillsToRemove = getSkillList().getSkillsBetweenRealmsIncludingMin(
                    newMajorRealm,oldMajorRealm,
                    newMinorRealm,oldMinorRealm
            );
            for(Pair<String,Boolean> skillData : skillsToRemove){
                if(skillData.getB()) continue; //fixed skill do not remove
                PlayerSkillRemoveEvent event = new PlayerSkillRemoveEvent(player,newMinorRealm,newMajorRealm,path,skillData.getA());
                if(event.isCanceled()) continue;
                ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillData.getA(),':'));
                if(skill == null) continue; //TODO find a way to log an error?

                System.out.println("removing skill: "+skillData.getA());
                player.getData(ModAttachments.PLAYER_SKILL_DATA).removeSkill(skillData.getA());
                skill.onSkillRemoved(player);
            }

        }
        else{
            //add skills
            List<Pair<String,Boolean>> skillStoAdd = getSkillList().getSkillsBetweenRealmsExcludingMin(
                    oldMajorRealm,newMajorRealm,
                    oldMinorRealm,newMinorRealm
            );
            for(Pair<String,Boolean> skillData : skillStoAdd){
                ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(ResourceLocation.bySeparator(skillData.getA(),':'));
                if(skill == null) continue; //TODO find a way to log an error?
                String skillType = "Passive";
                if(skill instanceof AbstractActiveSkill) skillType = "Active";

                System.out.println("adding skill: "+skillData.getA());
                player.getData(ModAttachments.PLAYER_SKILL_DATA).addSkill(skillData.getA(),skillType,skillData.getB(),skill.getSkillData());
                skill.onSkillAdded(player);
            }
        }
    }

}
