package net.thejadeproject.ascension.progression.skills.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IPreCastSkillData {
    CompoundTag writeData();
    void readData(CompoundTag tag);
    void encode(RegistryFriendlyByteBuf buf);
    void decode(RegistryFriendlyByteBuf buf);
}
