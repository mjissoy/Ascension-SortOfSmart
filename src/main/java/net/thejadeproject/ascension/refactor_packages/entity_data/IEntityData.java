package net.thejadeproject.ascension.refactor_packages.entity_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodlineData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Stores all the data an entity would need that relates to cultivation and stats
 *
 * does not hold a direct reference to the entity holding it, only the id. this is because there may be scenarios where this data is not stored on the entity directly
 * or the entity is not loaded but data is. for this reason you either access the entity through EntityDataManger OR you get the entity reference from the method
 * being used
 */
public interface IEntityData {
    UUID getAttachedEntity();
    boolean isAttachedEntityLoaded(); //in scenarios where data is stored separately from the entity itself
    void setAttachedEntityLoaded(boolean loaded); //useful for implementations that rely on a manager(mine)

    //========================== FORM DATA HANDLING ==========================

    IEntityFormData getActiveFormData();
    //calls all on removed stuff,so should not be used for tethered entities
    IEntityFormData removeEntityForm(ResourceLocation form);
    IEntityFormData getEntityFormData(ResourceLocation form);
    IEntityFormData getEntityFormData(IEntityForm form);

    List<IEntityFormData> getFormData();


    void addEntityForm(ResourceLocation form);
    void addEntityForm(ResourceLocation form, IEntityFormData formData);

    void setActiveForm(ResourceLocation activeForm);



    //============================ PHYSIQUE HANDLING =======================================
    void setPhysique(ResourceLocation physique);
    //calls a separate method so the physique can update things properly
    void setPhysiqueFromExisting(ResourceLocation physique, IPhysiqueData existingData);
    IPhysiqueData getPhysiqueData();
    ResourceLocation getPhysiqueForm();
    IPhysiqueData removePhysique();

    //============================ BLOODLINE HANDLING =======================================
    void setBloodline(ResourceLocation bloodline);
    void setBloodlineFromExisting(ResourceLocation bloodline, IBloodlineData existingData);
    IBloodlineData getBloodlineData();
    ResourceLocation getBloodlineForm();
    IBloodlineData removeBloodline();

    //============================ CULTIVATION DATA HANDLING ==================================

    boolean hasPath(ResourceLocation path);
    //only checks held forms, not tethered
    boolean isCultivating();
    //only checks held forms, not tethered
    boolean isCultivating(ResourceLocation path);
    ResourceLocation getTechnique(ResourceLocation path);
    ITechniqueData getTechniqueData(ResourceLocation path);
    PathData getPathData(ResourceLocation path);
    ITechniqueData removeTechnique(ResourceLocation path);
    //techniques will check if the last used technique is compatible, if not will remove all cultivation
    //give some sort of warning beforehand?
    ITechniqueData setTechnique(ResourceLocation path);
    //a shortcut for removing cultivation, anything more complex must be done through the path data and path
    void removePath(ResourceLocation path);

    //============================ SKILL HANDLING ===================================
    void giveSkill(ResourceLocation skill,ResourceLocation form);
    void giveSkill(ResourceLocation skill, IPersistentSkillData skillData, ResourceLocation form);

    //only removes from that specific form.
    void removeSkill(ResourceLocation skill,ResourceLocation form);


    //============================= DATA HANDLING ====================================
    void write(CompoundTag tag);

}
