package net.thejadeproject.ascension.refactor_packages.forms.forms.mortal_vessel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;

public class MortalVesselForm implements IEntityForm {

    @Override
    public void enterForm(LivingEntity entity, IEntityFormData previousFormData) {

    }

    @Override
    public void leaveForm(LivingEntity entity) {

    }

    @Override
    public void newFormAddedPre(LivingEntity entity, ResourceLocation entityForm, IEntityFormData entityFormData) {

    }

    @Override
    public void newFormAddedPost(LivingEntity entity, ResourceLocation entityForm) {

    }

    @Override
    public IEntityFormData freshEntityFormData(LivingEntity entity) {
        return new MortalVesselFormData(entity,ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"mortal_vessel"));
    }

    @Override
    public IEntityFormData fromCompound(CompoundTag tag,LivingEntity entity) {
        return null;
    }

    @Override
    public IEntityFormData fromNetwork(RegistryFriendlyByteBuf buf,LivingEntity entity) {
        return null;
    }
}
