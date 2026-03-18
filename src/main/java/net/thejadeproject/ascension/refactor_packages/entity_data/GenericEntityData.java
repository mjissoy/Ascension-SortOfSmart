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

public class GenericEntityData implements IEntityData{
    private ResourceLocation activeForm;
    private UUID attachedEntity;

    private final HashMap<ResourceLocation, IEntityFormData> heldFormData = new HashMap<>();
    private final HashMap<ResourceLocation, UUID> tetheredFormData = new HashMap<>();

    private ResourceLocation physique;
    private ResourceLocation bloodline;

    private ResourceLocation physiqueForm; //the form holding the data of the physique
    private ResourceLocation bloodlineForm;//almost always body (but might have some rare circumstances where it is not)
    //not copied over to tethered entities
    private final HashMap<ResourceLocation,ResourceLocation> techniques = new HashMap<>(); //Path -> current technique

    boolean attachedEntityLoaded;

    @Override
    public UUID getAttachedEntity() {
        return attachedEntity;
    }

    @Override
    public boolean isAttachedEntityLoaded() {
        return attachedEntityLoaded;
    }

    @Override
    public void setAttachedEntityLoaded(boolean attachedEntityLoaded) {
        this.attachedEntityLoaded = attachedEntityLoaded;
    }

    @Override
    public void unTetherEntity(UUID entity) {
        //FIRST CHECK IF THAT ENTITY HAS OUR PHYSIQUE AND BLOODLINE
        //THEN RUN REMOVE FOR FORM ON ALL CULTIVATION METHODS
        //MAKE SURE TO DO THIS FOR ALL TETHERED ENTITIES THAT ARE NOT THE REMOVED ENTITY
        IEntityData unTetheredEntity = EntityDataManager.getEntityData(entity);
        if(!heldFormData.containsKey(bloodlineForm) && tetheredFormData.get(bloodlineForm).equals(entity)){
            //TODO
            // proceed with bloodline removal
        }
        if(!heldFormData.containsKey(physiqueForm) && tetheredFormData.get(physiqueForm).equals(entity)){
            //TODO
            // proceed with physique removal
        }


       for(Map.Entry<ResourceLocation,UUID> form : tetheredFormData.entrySet()){
           if(!form.equals(entity)) continue;
           IEntityFormData formData = EntityDataManager.getEntityFormData(form.getValue(),form.getKey());
           IEntityForm formObj = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(form.getKey());
           formObj.removeForm(getAttachedEntity(),formData);
           tetheredFormData.remove(form.getKey());
       }

        //gets all related form data, exclude this entity so i do not acidentaly remove a technique i have on me
        //two entities should not tether to a shared entity and eachother
        for(IEntityFormData formData : unTetheredEntity.getFormData(Set.of(getAttachedEntity()))){
            //handle removal of held skills and pathData
            List<PathData> allPathData = formData.getAllPathData();
            for(PathData pathData : allPathData){
                pathData.removeForEntity(this);
            }
            HeldSkills heldSkills = formData.getHeldSkills();
            heldSkills.removeAllSkillsFromEntityData(this);
        }

    }

    /*
        The active form can only be in heldFormData
     */
    @Override
    public IEntityFormData getActiveFormData() {
        return heldFormData.get(activeForm);
    }

    @Override
    public List<IEntityFormData> getFormData() {
        return getFormData(Set.of());
    }

    @Override
    public  List<IEntityFormData> getHeldFormData() {
        return (List<IEntityFormData>) heldFormData.values();
    }

    @Override
    public List<IEntityFormData> getFormData(Set<UUID> excludedTetheredEntities) {
        ArrayList<IEntityFormData> fullFormData = new ArrayList<>();
        fullFormData.addAll(heldFormData.values());
        for (Map.Entry<ResourceLocation,UUID> tetheredForm : tetheredFormData.entrySet()){
            if(excludedTetheredEntities.contains(tetheredForm.getValue())) continue;
            fullFormData.add(EntityDataManager.getEntityFormData(tetheredForm.getValue(),tetheredForm.getKey()));
        }
        return fullFormData;
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

    /*
        removes the form data from this entity data instance, then adds a reference to the tethered entity
        the actual form data should be handled by whatever is creating the tethered entity
     */
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
    public void setPhysique(ResourceLocation physique) {

    }

    @Override
    public void setPhysiqueFromExisting(ResourceLocation physique, IPhysiqueData existingData) {

    }

    @Override
    public IPhysiqueData getPhysiqueData() {
        return null;
    }

    @Override
    public ResourceLocation getPhysiqueForm() {
        return null;
    }

    @Override
    public IPhysiqueData removePhysique() {
        return null;
    }

    @Override
    public void setBloodline(ResourceLocation bloodline) {

    }

    @Override
    public void setBloodlineFromExisting(ResourceLocation bloodline, IBloodlineData existingData) {

    }

    @Override
    public IBloodlineData getBloodlineData() {
        return null;
    }

    @Override
    public ResourceLocation getBloodlineForm() {
        return null;
    }

    @Override
    public IBloodlineData removeBloodline() {
        return null;
    }

    @Override
    public boolean hasPath(ResourceLocation path) {
        return false;
    }

    @Override
    public boolean isCultivating() {
        return false;
    }

    @Override
    public boolean isCultivating(ResourceLocation path) {
        return false;
    }

    @Override
    public ResourceLocation getTechnique(ResourceLocation path) {
        return null;
    }

    @Override
    public ITechniqueData getTechniqueData(ResourceLocation path) {
        return null;
    }

    @Override
    public PathData getPathData(ResourceLocation path) {
        return null;
    }

    @Override
    public ITechniqueData removeTechnique(ResourceLocation path) {
        return null;
    }

    @Override
    public ITechniqueData setTechnique(ResourceLocation path) {
        return null;
    }

    @Override
    public void removePath(ResourceLocation path) {

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
