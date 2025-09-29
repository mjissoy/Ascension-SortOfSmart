package net.thejadeproject.ascension.progression.skills.data;

import net.minecraft.nbt.CompoundTag;

public interface ISkillData {

    void writeData(CompoundTag tag);
    void readData(CompoundTag tag);
}
