package net.thejadeproject.ascension.progression.breakthrough;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface IBreakthroughData {

    CompoundTag writeBreakthroughData();
     void readBreakthroughData(CompoundTag tag);
}
