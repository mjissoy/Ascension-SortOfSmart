package net.thejadeproject.ascension.refactor_packages.entity_data;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodlineData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
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
    List<IEntityFormData> getHeldFormData();
    List<IEntityFormData> getFormData(Set<UUID> excludedTetheredEntities);
    void addNewEntityForm(ResourceLocation form);
    void addExistingEntityForm(ResourceLocation form, IEntityFormData data);
    void changeActiveFormTo(ResourceLocation form);

    //========================== TETHERED ENTITY HANDLING======================

    //this is called after the data is already moved
    //this way i can safely "remove" the data without calling the proper protocols
    void moveFormToTetheredEntity(UUID entityId,ResourceLocation form);

    //this does not call any untether methods. since it would be used when merging a tethered entity back into the entity data
    void moveFormOffTetheredEntity(ResourceLocation form,IEntityFormData formData);

    /*
        returns a list of all the currently tethered entities
        in most cases it is empty or 1, however some cases the primary entity data may have multiple
     */

    //handles the removal of data that occurs
    void unTetherEntity(UUID entity);


    List<UUID> getTetheredEntities();


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


}
