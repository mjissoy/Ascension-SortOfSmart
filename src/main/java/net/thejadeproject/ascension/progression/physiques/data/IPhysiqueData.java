package net.thejadeproject.ascension.progression.physiques.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface  IPhysiqueData {

    CompoundTag writeData();
    void readData(CompoundTag tag);
    void encode(RegistryFriendlyByteBuf buf);
    void decode(RegistryFriendlyByteBuf buf);


}
