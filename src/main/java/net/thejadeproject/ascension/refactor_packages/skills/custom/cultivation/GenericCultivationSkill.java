package net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.skill_data.GenericCultivationSkillData;

import java.util.Set;

public class GenericCultivationSkill implements ISkill {


    private double baseRate;
    private ResourceLocation path;
    private Set<ResourceLocation> secondaryPaths;

    public GenericCultivationSkill(double baseRate,ResourceLocation path, Set<ResourceLocation> secondaryPaths){
        this.baseRate = baseRate;
        this.path = path;
        this.secondaryPaths = secondaryPaths;
    }

    @Override
    public void onAdded(IEntityData attachedEntityData) {
        System.out.println("added skill : "+ AscensionRegistries.Skills.SKILL_REGISTRY.getKey(this).toString());
    }

    @Override
    public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {
        System.out.println("removed skill : "+ AscensionRegistries.Skills.SKILL_REGISTRY.getKey(this).toString());
    }

    @Override
    public void onFormAdded(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {

    }

    @Override
    public void onFormRemoved(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {

    }

    @Override
    public void finishedCooldown(IEntityData attachedEntityData) {

    }

    @Override
    public IPersistentSkillData freshPersistentData(IEntityData heldEntity) {
        return new GenericCultivationSkillData(baseRate,path,secondaryPaths);
    }

    @Override
    public IPersistentSkillData fromCompound(CompoundTag tag, IEntityData heldEntity) {
        return new GenericCultivationSkillData(baseRate,path,secondaryPaths);
    }

    @Override
    public IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf, IEntityData heldEntity) {
        return new GenericCultivationSkillData(baseRate,path,secondaryPaths);
    }
}
