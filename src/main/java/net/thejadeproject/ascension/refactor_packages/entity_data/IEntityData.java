package net.thejadeproject.ascension.refactor_packages.entity_data;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface IEntityData {
    UUID getAttachedEntity();

    IEntityFormData getActiveFormData();
    IEntityFormData removeEntityForm(ResourceLocation form);
    IEntityFormData getEntityFormData(ResourceLocation form);
    IEntityFormData getEntityFormData(IEntityForm form);


    void addNewEntityForm(ResourceLocation form);
    void addExistingEntityForm(ResourceLocation form, IEntityFormData data);
    void changeActiveFormTo(ResourceLocation form);

    //this is called after the data is already moved
    //this way i can safely "remove" the data without calling the proper protocols
    void moveFormToTetheredEntity(UUID entityId);

    /*
        returns a list of all the currently tethered entities
        in most cases it is empty or 1, however some cases the primary entity data may have multiple
     */

    List<UUID> getTetheredEntities();

    /*
        used for forms that are not influenced by the player, so they are not intended to have their stats modified by
        skills and such
     */
    void addTemporaryForm(ResourceLocation form);
    void addExistingTemporaryForm(ResourceLocation form,IEntityFormData data);


}
