package net.thejadeproject.ascension.refactor_packages.player_data;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.thejadeproject.ascension.refactor_packages.attributes.AscensionAttribute;
import net.thejadeproject.ascension.refactor_packages.attributes.AscensionAttributeInstance;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.stats.Stat;
import net.thejadeproject.ascension.refactor_packages.stats.StatInstance;

import java.util.HashMap;

public class EntityData {


    private ResourceLocation activeForm;
    private final LivingEntity entity;

    private final HashMap<ResourceLocation, IEntityFormData> formData = new HashMap<>();

    public EntityData(LivingEntity entity) {
        this.entity = entity;
    }

    public IEntityFormData getActiveFormData(){
        return formData.get(activeForm);
    }

    public IEntityFormData removeEntityForm(ResourceLocation form){
        return formData.remove(form);
    }
    public IEntityFormData getEntityFormData(ResourceLocation form){
        return formData.get(form);
    }
    public IEntityFormData getEntityFormData(IEntityForm form){
        return formData.get(AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.getKey(form));
    }

    public void addNewEntityForm(ResourceLocation form){
        if(formData.containsKey(form)) return;
        formData.put(form,
        AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(form).freshEntityFormData(entity));
    }
    public void addExistingEntityForm(ResourceLocation form,IEntityFormData data){
        if(formData.containsKey(form)) return; //for now we need to remove it first

        //pre
        //TODO make an event here
        for(ResourceLocation entityForm : formData.keySet()){
            AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(entityForm).newFormAddedPre(
                    entity,
                    form,
                    data
            );
        }
        //TODO update bloodline and physique on this entity data
        formData.put(form,data);
        data.setAttachedEntity(entity);
        //post
        //TODO make an event here
        for(ResourceLocation entityForm : formData.keySet()){
            AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(entityForm).newFormAddedPost(
                    entity,
                    form
            );
        }
    }



    public void changeFormTo(ResourceLocation form){
        if(form.equals(activeForm)) return;
        ResourceLocation previousForm = getActiveFormId();
        activeForm = form;
        AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(previousForm).leaveForm(this);
        AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(form).enterForm(entity,previousForm);

    }


    public ResourceLocation getActiveFormId(){
        return activeForm;
    }

    public AscensionAttributeInstance getAttribute(AscensionAttribute attribute){
        return formData.get(activeForm).getAttributeContainer().getAttribute(attribute);
    }
    public AscensionAttributeInstance getAttribute(Holder<Attribute> attributeHolder){
        return formData.get(activeForm).getAttributeContainer().getAttribute(attributeHolder);
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
    public static EntityData getEntityData(LivingEntity livingEntity){
        return null;
    }
}
