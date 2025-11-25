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
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.cultivation.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.MinorRealmChangeEvent;
import net.thejadeproject.ascension.guis.easygui.elements.HoverableLabel;
import net.thejadeproject.ascension.progression.breakthrough.IBreakthroughHandler;
import net.thejadeproject.ascension.progression.dao.IDao;
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
import java.util.function.Consumer;

public abstract class AbstractTechnique implements ITechnique {
    public String title;
    public Double baseRate;
    public Consumer<MinorRealmChangeEvent> minorRealmChangeEventConsumer;
    public Consumer<MajorRealmChangeEvent> majorRealmChangeEventConsumer;
    public Map<String,Double> daoBonuses = new HashMap<>();
    public String path;
    public SkillList skillList = new SkillList(List.of());
    public ITextureData techniqueImage;
    public List<MutableComponent> description = new ArrayList<>();
    public StabilityHandler stabilityHandler;
    public IBreakthroughHandler breakthroughHandler;
    public AbstractTechnique(String title, double baseRate,String path,StabilityHandler stabilityHandler,IBreakthroughHandler handler){
        this.title = title;
        this.baseRate = baseRate;
        this.path = path;
        this.stabilityHandler =stabilityHandler;
        this.breakthroughHandler =handler;
    }


    @Override
    public IBreakthroughHandler getBreakthroughHandler() {
        return breakthroughHandler;
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


        for(String attribute:event.ascensionAttributeID()){
            IDao dao = AscensionRegistries.Dao.getDaoFromKey(attribute);
            for(String techniqueAttributeId : getDaoBonuses()){
                Double destructiveValue = dao.getDestructiveValue(techniqueAttributeId);
                Double generativeValue = dao.getDestructiveValue(techniqueAttributeId);
                Double relatedValue = dao.getDestructiveValue(techniqueAttributeId);
            }
            System.out.println("getting eff from technique");
            System.out.println(attribute);
            System.out.println(daoBonuses.get(attribute));
            if(daoBonuses.containsKey(attribute)) event.addDaoMultiplier(daoBonuses.get(attribute));
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
    public Double getEfficiencyValue(String attribute) {
        return daoBonuses.get(attribute);
    }
    @Override
    public String getPath() {
        return path;
    }

    public String getDisplayTitle(){
        return title;
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
    public void onMinorRealmIncrease(MinorRealmChangeEvent event) {
        ITechnique.super.onMinorRealmIncrease(event);
        minorRealmChangeEventConsumer.accept(event);
        if(event.oldRealm > event.newRealm) return;
        updatePlayerSkills(
                event.player,
                event.pathId,
                event.player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(event.pathId).majorRealm,
                event.newRealm
        );
    }

    @Override
    public void onMajorRealmIncrease(MajorRealmChangeEvent event) {
        ITechnique.super.onMajorRealmIncrease(event);
        majorRealmChangeEventConsumer.accept(event);
        if(event.oldRealm > event.newRealm) return;
        updatePlayerSkills(
                event.player,
                event.pathId,
                event.newRealm,
                event.player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(event.pathId).minorRealm
        );
    }

    //TODO add some sort of path registry and use that for this
    @Override
    public void onTechniqueAcquisition(Player player) {
        for(String path : List.of("ascension:intent","ascension:essence","ascension:body")){
            updatePlayerSkills(player,path,0,0);
        }
    }

    public AbstractTechnique setOnMinorRealmChange(Consumer<MinorRealmChangeEvent> consumer){
        minorRealmChangeEventConsumer = consumer;
        return this;
    }
    public AbstractTechnique setOnMajorRealmChange(Consumer<MajorRealmChangeEvent> consumer){
        majorRealmChangeEventConsumer = consumer;
        return this;
    }
    public AbstractTechnique setDescription(List<MutableComponent> description){
        this.description = description;
        return this;
    }

    @Override
    public List<MutableComponent> getDescription() {
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
    @Override
    public List<MutableComponent> getFullDescription() {
        List<MutableComponent> extraInfo = new ArrayList<>();
        if(description != null) extraInfo = new ArrayList<>(description);
        extraInfo.add(
                Component.literal("Dao Efficiencies:").withStyle(ChatFormatting.BOLD)
        );
        for (Map.Entry<String ,Double> value : daoBonuses.entrySet()){
            Component text = AscensionRegistries.Dao.DAO_REGISTRY.get(ResourceLocation.bySeparator(value.getKey(),':')).getDisplayTitle().copy().append(": "+value.getValue().toString());

            extraInfo.add(text.copy().append(": "+value.getValue().toString()));
        }

        return extraInfo;
    }
}
