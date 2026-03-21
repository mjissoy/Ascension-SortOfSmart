package net.thejadeproject.ascension.refactor_packages.paths;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;

public interface IPath {

    //the default form that holds this
    ResourceLocation defaultForm();

    PathData freshPathData(IEntityData heldEntity);
    //expected to simulate progression
    PathData fromCompound(CompoundTag tag, IEntityData heldEntity);
    PathData fromNetwork(RegistryFriendlyByteBuf buf, IEntityData heldEntity);
}
