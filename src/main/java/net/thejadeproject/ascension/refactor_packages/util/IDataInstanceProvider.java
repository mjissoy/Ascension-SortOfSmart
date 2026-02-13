package net.thejadeproject.ascension.refactor_packages.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;

public interface IDataInstanceProvider {

    IDataInstance fromCompound(CompoundTag tag);
    IDataInstance fromNetwork(RegistryFriendlyByteBuf buf);
}
