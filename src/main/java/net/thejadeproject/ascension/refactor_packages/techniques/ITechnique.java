package net.thejadeproject.ascension.refactor_packages.techniques;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;

import java.util.UUID;

public interface ITechnique {

    void onRealmChange(IEntityData entityData,int oldMajorRealm,int oldMinorRealm,int newMajorRealm,int newMinorRealm);


    /*
        onEntityTethered is called when an entities data is tethered to another existing entity.
        not called when,for example, a fresh tethered vessel is created. Since in that instance data is only being
        moved around. nothing new is being added or removed

        same with Untethered. called when there is some sort of data being added or removed rather than just moved around

    */
    void onEntityTethered(IEntityData heldEntity, IEntityData tetheredEntity, PathData pathData);
    void onEntityUntethered(IEntityData heldEntity,IEntityData oldTetheredEntity,PathData pathData);


    //figure something out with compatibility
    boolean isCompatibleWith(ResourceLocation form);
}
