package net.thejadeproject.ascension.progression.breakthrough;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IBreakthroughData {

    CompoundTag writeBreakthroughData();
    void readBreakthroughData(CompoundTag tag);

    void encode(RegistryFriendlyByteBuf buf);
    void decode(RegistryFriendlyByteBuf buf);

    CompoundTag serialize();

    void deserialize(CompoundTag tag);
}
