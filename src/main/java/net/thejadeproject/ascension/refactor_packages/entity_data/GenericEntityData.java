package net.thejadeproject.ascension.refactor_packages.entity_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodline;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodlineData;
import net.thejadeproject.ascension.refactor_packages.events.PhysiqueChangeEvent;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysique;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;

import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.HeldSkill;
import net.thejadeproject.ascension.refactor_packages.skills.HeldSkills;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechnique;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;

import java.nio.file.Path;
import java.util.*;

public class GenericEntityData implements IEntityData {
    private ResourceLocation activeForm;
    private UUID attachedEntity;

    private final HashMap<ResourceLocation, IEntityFormData> heldFormData = new HashMap<>();

    private final HashMap<ResourceLocation,ResourceLocation> pathDataLocation = new HashMap<>();

    private ResourceLocation physiqueForm; //the form holding the data of the physique
    private ResourceLocation bloodlineForm;//almost always body (but might have some rare circumstances where it is not)

    boolean attachedEntityLoaded;


    //used during loading to temporarily store data, and when we save ignore.
    //important because during simulation something might exist before we actually made any data for it
    private HashMap<ResourceLocation,IEntityFormData> cachedFormData = new HashMap<>();
    private HashMap<ResourceLocation, IPersistentSkillData> cachedSkillData = new HashMap<>();


    //========================== SAVE DATA HANDLING ==========================
    public GenericEntityData(Entity attachedEntity){
        this.attachedEntity = attachedEntity.getUUID();
        //TODO add mortal vessel
        heldFormData.put(ModForms.MORTAL_VESSEL.getId(),ModForms.MORTAL_VESSEL.get().freshEntityFormData(this));

        //TODO add soul form
        heldFormData.put(ModForms.SOUL_FORM.getId(),ModForms.SOUL_FORM.get().freshEntityFormData(this));

    }
    public GenericEntityData(Entity attachedEntity, CompoundTag tag){
        System.out.println("creating player data");
        this.attachedEntity = attachedEntity.getUUID();
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
            System.out.println("creating mortal vessel");
            heldFormData.put(ModForms.MORTAL_VESSEL.getId(),
                    cachedFormData.containsKey(ModForms.MORTAL_VESSEL.getId()) ?
                            cachedFormData.get(ModForms.MORTAL_VESSEL.getId()) : ModForms.MORTAL_VESSEL.get().freshEntityFormData(this));
        }

        heldFormData.put(ModForms.SOUL_FORM.getId(),
                cachedFormData.containsKey(ModForms.SOUL_FORM.getId()) ?
                        cachedFormData.get(ModForms.SOUL_FORM.getId()) : ModForms.SOUL_FORM.get().freshEntityFormData(this));


        String rawPhysique = tag.getString("physique");
        //the user has no physique
        if(!rawPhysique.equals("none")){
            ResourceLocation physique = ResourceLocation.bySeparator(rawPhysique,':');
            IPhysique physiqueInstance = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physique);
            IPhysiqueData physiqueData = physiqueInstance.fromCompound(tag.getCompound("physique_data"),this);
            setPhysique(physique,physiqueData);
        }
        String rawBloodline = tag.getString("bloodline");
        //no bloodline
        if(!rawBloodline.equals("none")){
            ResourceLocation bloodline = ResourceLocation.bySeparator(rawBloodline,':');
            IBloodline bloodlineInstance = AscensionRegistries.Bloodlines.BLOODLINE_REGISTRY.get(bloodline);
            IBloodlineData bloodlineData = bloodlineInstance.fromCompound(tag.getCompound("bloodline_data"),this);
            //TODO add to mortal vessel, then run proper physique addition code
        }

        //TODO add cultivation
    }


    @Override
    public void write(CompoundTag tag) {
        //if the player losses their vessel we do not want to accidentally make a new one
        tag.putBoolean("vessel_flag",heldFormData.containsKey(ModForms.MORTAL_VESSEL.getId()));

        tag.putString("physique",physiqueForm == null ? "none" : heldFormData.get(physiqueForm).getPhysiqueKey().toString());
        if (physiqueForm != null && heldFormData.get(physiqueForm).getPhysiqueData() != null) {
            tag.put("physique_data",heldFormData.get(physiqueForm).getPhysiqueData().write());
        }
        tag.putString("bloodline",bloodlineForm == null ? "none" : heldFormData.get(bloodlineForm).getBloodlineKey().toString());
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
            if(skills != null){
                for(HeldSkill heldSkill : skills.getSkills()){

                    if(heldSkill.getPersistentData() == null) continue;
                    if(visitedSkills.contains(heldSkill.getKey())) continue;

                    visitedSkills.add(heldSkill.getKey());
                    CompoundTag skillTag = new CompoundTag();
                    skillTag.putString("skill",heldSkill.getKey().toString());
                    skillTag.put("data",heldSkill.getPersistentData().write());
                    skillTags.add(skillTag);
                }
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
    public boolean setPhysique(ResourceLocation physique) {
        return setPhysique(physique,AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physique).freshPhysiqueData(this));
    }

    @Override
    public boolean setPhysique(ResourceLocation physique, IPhysiqueData existingData) {
        return setPhysique(physique,existingData,ModForms.MORTAL_VESSEL.getId());
    }

    @Override
    public boolean setPhysique(ResourceLocation physique, IPhysiqueData physiqueData,ResourceLocation form) {

        if(!heldFormData.containsKey(form)) return false;


        ResourceLocation oldPhysique = null;
        IPhysiqueData oldPhysiqueData = null;
        if(physiqueForm != null){
            //no old physique just replace directly
            oldPhysique = heldFormData.get(physiqueForm).getPhysiqueKey();
            oldPhysiqueData = heldFormData.get(physiqueForm).getPhysiqueData();
            heldFormData.get(physiqueForm).getPhysique().onPhysiqueRemoved(this,oldPhysiqueData,physique);
            heldFormData.get(physiqueForm).setPhysique(null);
        }
        System.out.println("trying to replace :"+(oldPhysique == null ? "none" : oldPhysique.toString()));

        physiqueForm = form;

        heldFormData.get(physiqueForm).setPhysique(physique,physiqueData);
        heldFormData.get(physiqueForm).getPhysique().onPhysiqueAdded(this,oldPhysique,oldPhysiqueData);

        PhysiqueChangeEvent event = new PhysiqueChangeEvent(oldPhysique,oldPhysiqueData,physique,physiqueData,this);
        System.out.println("changed physique to : "+heldFormData.get(physiqueForm).getPhysique().getDisplayTitle().getString());
        NeoForge.EVENT_BUS.post(event);

        return true;
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

    @Override
    public IPhysique getPhysique() {
        return heldFormData.get(physiqueForm).getPhysique();
    }

    @Override
    public void movePhysique(ResourceLocation form) {
        if(!heldFormData.containsKey(form))return;
        ResourceLocation physique = heldFormData.get(physiqueForm).getPhysiqueKey();
        IPhysiqueData physiqueData = heldFormData.get(physique).getPhysiqueData();
        heldFormData.get(physiqueForm).setPhysique(null);
        heldFormData.get(form).setPhysique(physique,physiqueData);
        physiqueForm = form;
    }

    //============================ BLOODLINE HANDLING =======================================
    @Override
    public void setBloodline(ResourceLocation bloodline) {
        //TODO
    }

    @Override
    public void setBloodline(ResourceLocation bloodline, IBloodlineData existingData) {
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

    @Override
    public IBloodline getBloodline() {
        return heldFormData.get(bloodlineForm).getBloodline();
    }

    @Override
    public void moveBloodline(ResourceLocation form) {

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
    public boolean setTechnique(ResourceLocation technique) {
        return setTechnique(technique,AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique).freshTechniqueData(this));
    }

    @Override
    public boolean setTechnique(ResourceLocation technique, ITechniqueData techniqueData) {
        ITechnique techniqueInstance = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(technique);
        ResourceLocation path = techniqueInstance.getPath();
        PathData pathData = heldFormData.get(pathDataLocation.get(path)).getPathData(path);
        if(pathData == null) return false;
        ITechnique oldTechnique = null;
        if(pathData.getLastUsedTechnique() != null){

            if(technique.equals(pathData.getLastUsedTechnique())){
                return false; //we have already learned this technique
            }
            oldTechnique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(pathData.getLastUsedTechnique());
            oldTechnique.onTechniqueRemoved(this,pathData.getTechniqueData(pathData.getLastUsedTechnique()));
        }

        //TODO then check compatibility with technique history, if even 1 is not compatible we reset cultivation data
        if(oldTechnique != null){
            //there was a previous technique so check for compatibility
            pathData.removeLastUsedTechnique();
            for(ResourceLocation usedTechnique : pathData.getTechniqueHistory()){
                if(techniqueInstance.isCompatibleWith(usedTechnique)) continue;

                pathData.resetCultivation();

                break;
            }

        }
        pathData.setLastUsedTechnique(technique);
        pathData.addTechniqueData(technique,techniqueData);

        techniqueInstance.onTechniqueAdded(this);

        return true;
    }

    @Override
    public void removePath(ResourceLocation path) {
        //TODO
    }
    //============================ BREAKTHROUGH HANDLING ==================================


    @Override
    public boolean isBreakingThrough(ResourceLocation path) {
        if(!pathDataLocation.containsKey(path)) return false;
        if(!heldFormData.containsKey(pathDataLocation.get(path))) return false;

        PathData data = heldFormData.get(pathDataLocation.get(path)).getPathData(path);
        if(data == null) return false;
        return data.isBreakingThrough();
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