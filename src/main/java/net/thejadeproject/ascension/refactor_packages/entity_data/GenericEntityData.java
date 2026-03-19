package net.thejadeproject.ascension.refactor_packages.entity_data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodlineData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.player_data.EntityData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.HeldSkills;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;

import java.nio.file.Path;
import java.util.*;

public class GenericEntityData implements IEntityData {
    private ResourceLocation activeForm;
    private UUID attachedEntity;

    private final HashMap<ResourceLocation, IEntityFormData> heldFormData = new HashMap<>();
    private final HashMap<ResourceLocation, UUID> tetheredFormData = new HashMap<>();


    private ResourceLocation physique;
    private ResourceLocation bloodline;

    private ResourceLocation physiqueForm; //the form holding the data of the physique
    private ResourceLocation bloodlineForm;//almost always body (but might have some rare circumstances where it is not)
    //not copied over to tethered entities
    private final HashMap<ResourceLocation, ResourceLocation> techniques = new HashMap<>(); //Path -> current technique

    boolean attachedEntityLoaded;

    //========================== FORM DATA HANDLING ==========================
    @Override
    public UUID getAttachedEntity() {
        return attachedEntity;
    }

    @Override
    public boolean isAttachedEntityLoaded() {
        return false; //TODO
    }

    @Override
    public void setAttachedEntityLoaded(boolean loaded) {
        this.attachedEntityLoaded = loaded;
    }

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
        if(heldFormData.containsKey(form)) return heldFormData.get(form);
        if(!tetheredFormData.containsKey(form)) return null;
        return EntityDataManager.getEntityFormData(tetheredFormData.get(form),form);
    }

    @Override
    public IEntityFormData getEntityFormData(IEntityForm form) {
        return getEntityFormData(AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.getKey(form));
    }

    @Override
    public List<IEntityFormData> getFormData() {
        ArrayList<IEntityFormData> finalList = new ArrayList<>();
        for(Map.Entry<ResourceLocation,UUID> tetheredForm:tetheredFormData.entrySet()){
            finalList.add(EntityDataManager.getEntityFormData(tetheredForm.getValue(),tetheredForm.getKey()));
        }
        finalList.addAll(getHeldFormData());
        return finalList;
    }

    @Override
    public List<IEntityFormData> getHeldFormData() {
        return (List<IEntityFormData>) heldFormData.values();
    }

    @Override
    public List<IEntityFormData> getFormData(Set<UUID> excludedTetheredEntities) {
        ArrayList<IEntityFormData> finalList = new ArrayList<>();
        for(Map.Entry<ResourceLocation,UUID> tetheredForm:tetheredFormData.entrySet()){
            if(excludedTetheredEntities.contains(tetheredForm.getValue())) continue;
            finalList.add(EntityDataManager.getEntityFormData(tetheredForm.getValue(),tetheredForm.getKey()));
        }
        finalList.addAll(getHeldFormData());
        return finalList;
    }

    @Override
    public void addNewEntityForm(ResourceLocation form) {
        IEntityForm formFactory = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(form);
        IEntityFormData formData = formFactory.freshEntityFormData(this);
        Set<Map.Entry<ResourceLocation,IEntityFormData>> forms = heldFormData.entrySet();

        heldFormData.put(form,formData);

        for(Map.Entry<ResourceLocation,IEntityFormData> heldForm : forms){
            IEntityForm heldFormFactory = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(heldForm.getKey());
            heldFormFactory.onFormAdded(this,heldForm.getValue(),formData);
        }
        for(Map.Entry<ResourceLocation,UUID> tetheredForm : tetheredFormData.entrySet()){
            IEntityForm tetheredFormFactory = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(tetheredForm.getKey());
            tetheredFormFactory.onFormAdded(this,EntityDataManager.getEntityFormData(tetheredForm.getValue(),tetheredForm.getKey()),formData);
        }
    }

    @Override
    public void addExistingEntityForm(ResourceLocation form, IEntityFormData data) {
        IEntityForm formFactory = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(form);

        Set<Map.Entry<ResourceLocation,IEntityFormData>> forms = heldFormData.entrySet();

        heldFormData.put(form,data);

        for(Map.Entry<ResourceLocation,IEntityFormData> heldForm : forms){
            IEntityForm heldFormFactory = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(heldForm.getKey());
            heldFormFactory.onFormAdded(this,heldForm.getValue(),data);
        }
        for(Map.Entry<ResourceLocation,UUID> tetheredForm : tetheredFormData.entrySet()){
            IEntityForm tetheredFormFactory = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(tetheredForm.getKey());
            tetheredFormFactory.onFormAdded(this,EntityDataManager.getEntityFormData(tetheredForm.getValue(),tetheredForm.getKey()),data);
        }
    }

    @Override
    public void changeActiveFormTo(ResourceLocation form) {
        //TODO
    }

    //========================== TETHERED ENTITY HANDLING======================


    //does not handle the removed form data, this is done by whatever method is "moving" the data
    @Override
    public void moveFormToTetheredEntity(UUID entityId, ResourceLocation form) {
        heldFormData.remove(form);
        tetheredFormData.put(form,entityId);
    }
    //does not try to access the tethered entity, for that reason we pass form data directly
    @Override
    public void moveFormOffTetheredEntity(ResourceLocation form,IEntityFormData formData) {
        tetheredFormData.remove(form);
        heldFormData.put(form,formData);
    }

    /**
        Handles the removal of a tethered entities and all that goes along with it (form removal, cultivation removal ect
     */
    @Override
    public void unTetherEntity(UUID entity) {
        IEntityData removedEntityData = EntityDataManager.getEntityData(entity);

        //first we get all forms that are on that entity
        //this also gets any other tethered forms(future proofing)
        List<IEntityFormData> removedForms = removedEntityData.getFormData(Set.of(getAttachedEntity()));

        List<IEntityFormData> leftoverForms = getFormData(Set.of(entity));
        for(IEntityFormData removedForm : removedForms){
            //remove the reference
            if(tetheredFormData.containsKey(removedForm.getEntityFormId())) tetheredFormData.remove(removedForm.getEntityFormId());

            for(IEntityFormData leftoverForm :leftoverForms){
                leftoverForm.getEntityForm().onFormRemoved(this,leftoverForm,removedForm);
            }
        }


    }

    @Override
    public List<UUID> getTetheredEntities() {
        return List.of(); //TODO
    }

    //============================ PHYSIQUE HANDLING =======================================
    @Override
    public void setPhysique(ResourceLocation physique) {
        //TODO
    }

    @Override
    public void setPhysiqueFromExisting(ResourceLocation physique, IPhysiqueData existingData) {
        //TODO
    }

    @Override
    public IPhysiqueData getPhysiqueData() {
        return null; //TODO
    }

    @Override
    public ResourceLocation getPhysiqueForm() {
        return null; //TODO
    }

    @Override
    public IPhysiqueData removePhysique() {
        return null; //TODO
    }

    //============================ BLOODLINE HANDLING =======================================
    @Override
    public void setBloodline(ResourceLocation bloodline) {
        //TODO
    }

    @Override
    public void setBloodlineFromExisting(ResourceLocation bloodline, IBloodlineData existingData) {
        //TODO
    }

    @Override
    public IBloodlineData getBloodlineData() {
        return null;//TODO
    }

    @Override
    public ResourceLocation getBloodlineForm() {
        return null;//TODO
    }

    @Override
    public IBloodlineData removeBloodline() {
        return null;//TODO
    }

    //============================ CULTIVATION DATA HANDLING ==================================
    @Override
    public boolean hasPath(ResourceLocation path) {
        return false;//TODO
    }

    @Override
    public boolean isCultivating() {
        return false;//TODO
    }

    @Override
    public boolean isCultivating(ResourceLocation path) {
        return false;//TODO
    }

    @Override
    public ResourceLocation getTechnique(ResourceLocation path) {
        return null;//TODO
    }

    @Override
    public ITechniqueData getTechniqueData(ResourceLocation path) {
        return null;//TODO
    }

    @Override
    public PathData getPathData(ResourceLocation path) {
        return null;//TODO
    }

    @Override
    public ITechniqueData removeTechnique(ResourceLocation path) {
        return null;//TODO
    }

    @Override
    public ITechniqueData setTechnique(ResourceLocation path) {
        return null;//TODO
    }

    @Override
    public void removePath(ResourceLocation path) {
        //TODO
    }
}