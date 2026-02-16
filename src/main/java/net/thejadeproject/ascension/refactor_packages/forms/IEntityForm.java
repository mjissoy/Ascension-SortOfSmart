package net.thejadeproject.ascension.refactor_packages.forms;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface IEntityForm {

    void enterForm(LivingEntity entity, ResourceLocation previousForm);
    void leaveForm(LivingEntity entity);


    IEntityFormData freshEntityFormData();
    IEntityFormData fromCompound(CompoundTag tag);
    IEntityFormData fromNetwork(RegistryFriendlyByteBuf buf);
}
