package net.thejadeproject.ascension.refactor_packages.techniques;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.UUID;

public interface ITechnique {


    void cultivate(IEntityData entityData);

    Component getDisplayTitle();
    Component getShortDescription();
    Component getDescription();

    //the path this technique is for
    ResourceLocation getPath();

    void onTechniqueAdded(IEntityData heldEntity);
    void onTechniqueRemoved(IEntityData heldEntity,ITechniqueData techniqueData);

    //if realm change covers a breakthrough, get stability from pathData
    void onRealmChange(IEntityData entityData,int oldMajorRealm,int oldMinorRealm,int newMajorRealm,int newMinorRealm);


    void onFormRemoved(IEntityData heldEntity, IEntityFormData removedForm,PathData pathData);
    void onFormAdded(IEntityData heldEntity,IEntityFormData addedForm,PathData pathData);


    //figure something out with compatibility
    boolean isCompatibleWith(ResourceLocation technique);

    default Component getMajorRealmName(int majorRealm){
        return AscensionRegistries.Paths.PATHS_REGISTRY.get(getPath()).getMajorRealmName(majorRealm);
    }
    default Component getMinorRealmName(int majorRealm,int minorRealm){
        return AscensionRegistries.Paths.PATHS_REGISTRY.get(getPath()).getMinorRealmName(majorRealm,minorRealm);
    }

    default int getMaxMajorRealm(){
        return AscensionRegistries.Paths.PATHS_REGISTRY.get(getPath()).getMaxMajorRealm();
    }
    default int getMaxMinorRealm(int majorRealm){
        return AscensionRegistries.Paths.PATHS_REGISTRY.get(getPath()).getMaxMinorRealm(majorRealm);
    }
    default double getMaxQiForRealm(int majorRealm,int minorRealm){
        return AscensionRegistries.Paths.PATHS_REGISTRY.get(getPath()).getMaxQiForRealm(majorRealm,minorRealm);
    }
    default boolean canBreakthrough(IEntityData entityData,int majorRealm,int minorRealm,double progress){
        return minorRealm == getMaxMinorRealm(majorRealm) && majorRealm < getMaxMajorRealm() && !entityData.isBreakingThrough(getPath()) && progress >= getMaxQiForRealm(majorRealm,minorRealm);
    }

    default ResourceLocation getDefaultForm(){
        return AscensionRegistries.Paths.PATHS_REGISTRY.get(getPath()).defaultForm();
    }

    ITechniqueData freshTechniqueData(IEntityData heldEntity);
    ITechniqueData fromCompound(CompoundTag tag, IEntityData heldEntity);
    ITechniqueData fromNetwork(RegistryFriendlyByteBuf buf, IEntityData heldEntity);
}
