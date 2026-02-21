package net.thejadeproject.ascension.refactor_packages.entity_data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GenericEntityData implements IEntityData{
    private ResourceLocation activeForm;
    private UUID attachedEntity;

    private final HashMap<ResourceLocation, IEntityFormData> heldFormData = new HashMap<>();
    private final HashMap<ResourceLocation, UUID> tetheredFormData = new HashMap<>();

    private ResourceLocation physique;
    private ResourceLocation bloodline;

    private ResourceLocation physiqueForm; //the form holding the data of the physique
    private ResourceLocation bloodlineForm;//almost always body (but might have some rare circumstances where it is not)

    private final HashMap<ResourceLocation,ResourceLocation> techniques = new HashMap<>(); //Path -> current technique


    @Override
    public UUID getAttachedEntity() {
        return attachedEntity;
    }

    /*
        The active form can only be in heldFormData
     */
    @Override
    public IEntityFormData getActiveFormData() {
        return heldFormData.get(activeForm);
    }

    @Override
    public IEntityFormData removeEntityForm(ResourceLocation form) {
        return null; //TODO
    }

    @Override
    public IEntityFormData getEntityFormData(ResourceLocation form) {
        if(heldFormData.containsKey(form))return heldFormData.get(form);
        if(tetheredFormData.containsKey(form)) return EntityDataManager.getEntityFormData(tetheredFormData.get(form),form);
        return null;
    }

    @Override
    public IEntityFormData getEntityFormData(IEntityForm form) {
        return getEntityFormData(AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.getKey(form));
    }

    @Override
    public void addNewEntityForm(ResourceLocation form) {
        //TODO
    }

    @Override
    public void addExistingEntityForm(ResourceLocation form, IEntityFormData data) {
        //TODO
    }

    @Override
    public void changeActiveFormTo(ResourceLocation form) {
        //TODO
    }

    @Override
    public void moveFormToTetheredEntity(UUID entityId,ResourceLocation form) {
        //TODO
    }

    @Override
    public void moveFormOffTetheredEntity(ResourceLocation form) {

    }

    @Override
    public List<UUID> getTetheredEntities() {
        return List.of();//TODO
    }

    @Override
    public void addTemporaryForm(ResourceLocation form) {
//TODO
    }

    @Override
    public void addExistingTemporaryForm(ResourceLocation form, IEntityFormData data) {
//TODO
    }
}
