package net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.skill_data.GenericCultivationSkillData;

import java.util.Set;

public class GenericCultivationSkill implements ICastableSkill {


    private double baseRate;
    private ResourceLocation path;

    public GenericCultivationSkill(double baseRate,ResourceLocation path){
        this.baseRate = baseRate;
        this.path = path;
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
    public void finishedCooldown(IEntityData attachedEntityData, String identifier) {

    }




    @Override
    public IPersistentSkillData freshPersistentData(IEntityData heldEntity) {
        return new GenericCultivationSkillData(baseRate,Set.of());
    }

    @Override
    public IPersistentSkillData fromCompound(CompoundTag tag, IEntityData heldEntity) {
        return new GenericCultivationSkillData(tag);
    }

    @Override
    public IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf) {
        return new GenericCultivationSkillData(buf);
    }


    @Override
    public void onEquip(IEntityData entityData) {

    }

    @Override
    public void onUnEquip(IEntityData entityData, IPreCastData preCastData) {

    }

    @Override
    public void finalCast(CastEndData reason, Entity caster, ICastData castData) {

    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {

    }

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        return null;
    }

    @Override
    public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) {
        if(!caster.hasData(ModAttachments.INPUT_STATES)) return false;

        //TODO handle cultivation event here


        return caster.getData(ModAttachments.INPUT_STATES).isHeld("skill_cast");
    }

    @Override
    public int getCooldown(CastEndData castEndData) {
        return 0;
    }

    @Override
    public void selected(IEntityData entityData) {

    }

    @Override
    public void unselected(IEntityData entityData) {

    }

    @Override
    public IPreCastData freshPreCastData() {
        return null;
    }

    @Override
    public IPreCastData preCastDataFromCompound(CompoundTag tag) {
        return null;
    }

    @Override
    public IPreCastData preCastDataFromNetwork(RegistryFriendlyByteBuf buf) {
        return null;
    }

    @Override
    public ICastData freshCastData() {
        return null;
    }

    @Override
    public ICastData castDataFromCompound(CompoundTag tag) {
        return null;
    }

    @Override
    public ICastData castDataFromNetwork(RegistryFriendlyByteBuf buf) {
        return null;
    }

    @Override
    public IPersistentSkillData freshPersistentInstance() {
        return null;
    }

    @Override
    public IPersistentSkillData persistentInstanceFromCompound(CompoundTag tag) {
        return null;
    }

    @Override
    public IPersistentSkillData persistentInstanceFromNetwork(RegistryFriendlyByteBuf buf) {
        return null;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }
}
