package net.thejadeproject.ascension.refactor_packages.entity_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodline;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodlineData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.player_data.EntityData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.HeldSkill;
import net.thejadeproject.ascension.refactor_packages.skills.HeldSkills;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;

import java.nio.file.Path;
import java.util.*;

public class GenericEntityData implements IEntityData {
    private ResourceLocation activeForm;
    private UUID attachedEntity;

    private final HashMap<ResourceLocation, IEntityFormData> heldFormData = new HashMap<>();


    private ResourceLocation physique;
    private ResourceLocation bloodline;

    private ResourceLocation physiqueForm; //the form holding the data of the physique
    private ResourceLocation bloodlineForm;//almost always body (but might have some rare circumstances where it is not)
    //not copied over to tethered entities
    private final HashMap<ResourceLocation, ResourceLocation> techniques = new HashMap<>(); //Path -> current technique

    boolean attachedEntityLoaded;


    //used during loading to temporarily store data, and when we save ignore.
    //important because during simulation something might exist before we actually made any data for it
    private HashMap<ResourceLocation,IEntityFormData> cachedFormData = new HashMap<>();
    private HashMap<ResourceLocation, IPersistentSkillData> cachedSkillData = new HashMap<>();


    //========================== SAVE DATA HANDLING ==========================

    public GenericEntityData(CompoundTag tag){
        //TODO load cached form data
        ListTag formDataTags = tag.getList("form_data", Tag.TAG_COMPOUND);
        ListTag skillDataTags = tag.getList("skill_data",Tag.TAG_COMPOUND);
        ListTag pathDataTags = tag.getList("path_progress",Tag.TAG_COMPOUND);

        for(int i=0;i<formDataTags.size();i++){
            CompoundTag formDataTag = formDataTags.getCompound(i);
            ResourceLocation formId = ResourceLocation.bySeparator(formDataTag.getString("form"),':');
            IEntityForm form = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(formId);
            IEntityFormData formData = form.fromCompound(formDataTag.getCompound("data"),this);
            cachedFormData.put(formId,formData);
        }
        for(int i=0;i<skillDataTags.size();i++){
            CompoundTag skillDataTag = skillDataTags.getCompound(i);
            ResourceLocation skillId = ResourceLocation.bySeparator(skillDataTag.getString("skill"),':');
            ISkill skill = AscensionRegistries.Skills.SKILL_REGISTRY.get(skillId);
            IPersistentSkillData skillData = skill.fromCompound(skillDataTag.getCompound("data"),this);
            cachedSkillData.put(skillId,skillData);
        }

        if(tag.getBoolean("vessel_flag")){
            //TODO add mortal vessel
        }
        //TODO add soul form



        ResourceLocation physique = ResourceLocation.bySeparator(tag.getString("physique"),':');
        //the user has no physique
        if(!physique.getPath().equals("none")){
            IPhysique physiqueInstance = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physique);
            IPhysiqueData physiqueData = physiqueInstance.fromCompound(tag.getCompound("physique_data"),this);
        }
        ResourceLocation bloodline = ResourceLocation.bySeparator(tag.getString("bloodline"),':');
        //no bloodline
        if(bloodline.getPath().equals("none")){
            IBloodline bloodlineInstance = AscensionRegistries.Bloodlines.BLOODLINE_REGISTRY.get(bloodline);
            IBloodlineData bloodlineData = bloodlineInstance.fromCompound(tag.getCompound("bloodline_data"),this);
        }

        //TODO add cultivation
    }


    @Override
    public void write(CompoundTag tag) {
        //if the player losses their vessel we do not want to accidentally make a new one
        tag.putBoolean("vessel_flag",heldFormData.containsKey(ResourceLocation.bySeparator("mortal_vessel",':')));

        tag.putString("physique",physique.toString());
        if (physiqueForm != null && heldFormData.get(physiqueForm).getPhysiqueData() != null) {
            tag.put("physique_data",heldFormData.get(physiqueForm).getPhysiqueData().write());
        }
        tag.putString("bloodline",bloodline.toString());
        if (bloodlineForm != null && heldFormData.get(bloodlineForm).getBloodlineData() != null) {
            tag.put("bloodline",heldFormData.get(bloodlineForm).getBloodlineData().write());
        }

        ListTag formTags = new ListTag();
        //since this is only for caching only cache forms that have data
        //for skills keep track of what i have already saved, so i don't accidentally do dupes
        //only write skills that have skill data, otherwise don't
        HashSet<ResourceLocation> visitedSkills = new HashSet<>();
        ListTag skillTags = new ListTag();

        ListTag pathDataTags = new ListTag();
        for(ResourceLocation form : heldFormData.keySet()){
            CompoundTag formTag = new CompoundTag();
            formTag.putString("form",form.toString());
            formTag.put("data",heldFormData.get(form).write());
            formTags.add(formTag);

            HeldSkills skills = heldFormData.get(form).getHeldSkills();
            for(HeldSkill heldSkill : skills.getSkills()){

                if(heldSkill.getPersistentData() == null) continue;
                if(visitedSkills.contains(heldSkill.getKey())) continue;

                visitedSkills.add(heldSkill.getKey());
                CompoundTag skillTag = new CompoundTag();
                skillTag.putString("skill",heldSkill.getKey().toString());
                skillTag.put("data",heldSkill.getPersistentData().write());
                skillTags.add(skillTag);
            }

            for(PathData pathData:heldFormData.get(form).getAllPathData()){
                CompoundTag pathDataTag = new CompoundTag();
                pathDataTag.putString("path",pathData.getPath().toString());
                pathDataTag.put("data",pathData.write());

                pathDataTags.add(pathDataTag);
            }
        }
        tag.put("form_data",formTags);
        tag.put("skill_data",skillTags);
        tag.put("path_progress",pathDataTags);



        //path data, make sure to also hold the path
    }

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
    public void setAttachedEntity(UUID attachedEntity) {
        this.attachedEntity = attachedEntity;
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
        return heldFormData.get(form);
    }

    @Override
    public IEntityFormData getEntityFormData(IEntityForm form) {
        return getEntityFormData(AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.getKey(form));
    }

    @Override
    public List<IEntityFormData> getFormData() {
        return (List<IEntityFormData>) heldFormData.values();
    }

    @Override
    public void addEntityForm(ResourceLocation form) {
        IEntityForm formFactory = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(form);
        IEntityFormData formData = formFactory.freshEntityFormData(this);
        addEntityForm(form,formData);

    }

    @Override
    public void addEntityForm(ResourceLocation form, IEntityFormData formData) {
        IEntityForm formFactory = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(form);
        Set<Map.Entry<ResourceLocation,IEntityFormData>> forms = heldFormData.entrySet();
        heldFormData.put(form,formData);

        for(Map.Entry<ResourceLocation,IEntityFormData> heldForm : forms){
            IEntityForm heldFormFactory = AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(heldForm.getKey());
            heldFormFactory.onFormAdded(this,heldForm.getValue(),formData);
        }
    }


    @Override
    public void setActiveForm(ResourceLocation activeForm) {
        this.activeForm = activeForm;
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
    //============================ SKILL DATA HANDLING ==================================
    @Override
    public void giveSkill(ResourceLocation skill, ResourceLocation form) {
        
    }

    @Override
    public void giveSkill(ResourceLocation skill, IPersistentSkillData skillData, ResourceLocation form) {

    }

    @Override
    public void removeSkill(ResourceLocation skill, ResourceLocation form) {

    }

}