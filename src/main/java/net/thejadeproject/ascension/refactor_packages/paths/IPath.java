package net.thejadeproject.ascension.refactor_packages.paths;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import org.apache.commons.lang3.tuple.Pair;

public interface IPath {

    Component getDisplayTitle();
    Component getDescription();


    Component getDisplayPathInteractions();

    Double getInteractionValue(ResourceLocation path);
    PathInteraction getInteractionType(ResourceLocation path);

    Component getMajorRealmName(int majorRealm);
    Component getMinorRealmName(int majorRealm,int minorRealm);


    int getMaxMajorRealm();
    int getMaxMinorRealm(int majorRealm);
    int getMaxQiForRealm(int majorRealm,int minorRealm);
    //the default form that holds this
    ResourceLocation defaultForm();

    PathData freshPathData(IEntityData heldEntity);
    //expected to simulate progression
    PathData fromCompound(CompoundTag tag, IEntityData heldEntity);
    PathData fromNetwork(RegistryFriendlyByteBuf buf);
}
