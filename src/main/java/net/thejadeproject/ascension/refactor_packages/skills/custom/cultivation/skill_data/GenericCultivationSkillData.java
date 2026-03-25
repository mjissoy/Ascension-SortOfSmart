package net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.skill_data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.GenericCultivationSkill;

import java.util.Set;

public class GenericCultivationSkillData implements IPersistentSkillData {

    private double baseRate;
    private ResourceLocation path;
    private Set<ResourceLocation> secondaryPaths;

    public GenericCultivationSkillData(double baseRate,ResourceLocation path, Set<ResourceLocation> secondaryPaths){
        this.baseRate = baseRate;
        this.path = path;
        this.secondaryPaths = secondaryPaths;
    }


    @Override
    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        return tag;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {

    }
}
