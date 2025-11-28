package net.thejadeproject.ascension.progression.skills.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface ISkillData {

    void writeData(CompoundTag tag);
    void readData(CompoundTag tag);
    void encode(RegistryFriendlyByteBuf buf);
}
