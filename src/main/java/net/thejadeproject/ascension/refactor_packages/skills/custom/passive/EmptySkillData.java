package net.thejadeproject.ascension.refactor_packages.skills.custom.passive;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;

public class EmptySkillData implements IPersistentSkillData {

    @Override
    public CompoundTag write() {
        return new CompoundTag();
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        // no data
    }

    public static EmptySkillData fromCompound(CompoundTag tag) {
        return new EmptySkillData();
    }

    public static EmptySkillData fromNetwork(RegistryFriendlyByteBuf buf) {
        return new EmptySkillData();
    }

    @Override
    public IPersistentSkillData copy() {
        return null;
    }

    @Override
    public IPersistentSkillData merge(IPersistentSkillData other) {
        return null;
    }
}