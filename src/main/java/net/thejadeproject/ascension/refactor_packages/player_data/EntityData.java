package net.thejadeproject.ascension.refactor_packages.player_data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.StatInstance;

import java.util.HashMap;

public class EntityData {
    private ResourceLocation activeForm;

    private final HashMap<ResourceLocation, IEntityFormData> formData = new HashMap<>();

    public IEntityFormData getActiveFormData(){
        return formData.get(activeForm);
    }
    public ResourceLocation getActiveFormId(){
        return activeForm;
    }

    public StatInstance getStat(Stat stat){
        return getActiveFormData().getStatSheet().getStatInstance(stat);
    }
    public StatInstance getStat(ResourceLocation statId){
        Stat stat = AscensionRegistries.Stats.STATS_REGISTRY.get(statId);
        return getActiveFormData().getStatSheet().getStatInstance(stat);
    }

    //checks all locations
    public boolean hasSkill(ResourceLocation skill){
        for (IEntityFormData entityFormData : formData.values()){
            if(entityFormData.getHeldSkills().hasSkill(skill)) return true;
        }
        return false;
    }
    public EntityData getEntityData(LivingEntity livingEntity){
        return null;
    }
}
