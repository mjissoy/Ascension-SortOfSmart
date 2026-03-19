package net.thejadeproject.ascension.refactor_packages.techniques;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;

import java.util.UUID;

public interface ITechnique {

    void onRealmChange(IEntityData entityData,int oldMajorRealm,int oldMinorRealm,int newMajorRealm,int newMinorRealm);


    void onFormRemoved(IEntityData heldEntity, IEntityFormData removedForm,PathData pathData);
    void onFormAdded(IEntityData heldEntity,IEntityFormData addedForm,PathData pathData);


    //figure something out with compatibility
    boolean isCompatibleWith(ResourceLocation form);
}
