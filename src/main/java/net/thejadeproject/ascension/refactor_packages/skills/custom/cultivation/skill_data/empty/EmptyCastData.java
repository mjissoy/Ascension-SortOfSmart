package net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.skill_data.empty;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;

public class EmptyCastData implements ICastData {

    @Override
    public CompoundTag write() {
        return new CompoundTag();
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
    }
}