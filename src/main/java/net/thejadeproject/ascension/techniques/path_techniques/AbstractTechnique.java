package net.thejadeproject.ascension.techniques.path_techniques;

import com.mojang.datafixers.types.templates.Tag;
import net.lucent.easygui.interfaces.ITextureData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.MinorRealmChangeEvent;
import net.thejadeproject.ascension.physiques.GenericPhysique;
import net.thejadeproject.ascension.skills.skill_lists.AcquirableSkillData;
import net.thejadeproject.ascension.skills.skill_lists.SkillList;
import net.thejadeproject.ascension.techniques.ITechnique;
import net.thejadeproject.ascension.techniques.path_techniques.essence.SingleElementTechnique;
import net.thejadeproject.ascension.util.ModTags;

import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractTechnique implements ITechnique {
    public String title;
    public Double baseRate;
    public Consumer<MinorRealmChangeEvent> minorRealmChangeEventConsumer;
    public Consumer<MajorRealmChangeEvent> majorRealmChangeEventConsumer;
    public Map<String,Double> efficiencyBonuses = new HashMap<>();
    public String path;
    public SkillList skillList = null;
    public ITextureData techniqueImage;
    public List<MutableComponent> description = new ArrayList<>();
    public AbstractTechnique(String title, double baseRate,String path){
        this.title = title;
        this.baseRate = baseRate;
        this.path = path;

    }
    @Override
    public void onGatherEfficiencyModifiers(GatherEfficiencyModifiersEvent event) {
        if(!Objects.equals(event.pathID, getPath())) return;
        for(String attribute:event.ascensionAttributeID()){
            if(efficiencyBonuses.containsKey(attribute)) event.addMultiplier(efficiencyBonuses.get(attribute));
        }
    }

    public AbstractTechnique setEfficiencyAttributes(Map<String,Double> efficiencyBonuses){
        this.efficiencyBonuses = efficiencyBonuses;
        return this;
    }
    @Override
    public List<String> getEfficiencyAttributes() {
        return efficiencyBonuses.keySet().stream().toList();
    }
    @Override
    public Double getEfficiencyValue(String attribute) {
        return efficiencyBonuses.get(attribute);
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
    }

    @Override
    public void onMajorRealmIncrease(MajorRealmChangeEvent event) {
        ITechnique.super.onMajorRealmIncrease(event);
        majorRealmChangeEventConsumer.accept(event);
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
        List<MutableComponent> extraInfo = new ArrayList<>(description);

        extraInfo.add(
                Component.literal("Grants Efficiencies:").withStyle(ChatFormatting.BOLD)
        );
        for (Map.Entry<String ,Double> value : efficiencyBonuses.entrySet()){
            extraInfo.add(ModTags.Items.tagDisplayData.get(ItemTags.create(ResourceLocation.bySeparator(value.getKey(),':'))).copy().append(": "+value.getValue().toString()));
        }

        return extraInfo;
    }
}
