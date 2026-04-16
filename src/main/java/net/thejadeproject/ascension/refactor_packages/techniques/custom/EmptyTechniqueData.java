package net.thejadeproject.ascension.refactor_packages.techniques.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechniqueData;

public class EmptyTechniqueData implements ITechniqueData {

    @Override
    public CompoundTag write() {
        return new CompoundTag();
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {}

}