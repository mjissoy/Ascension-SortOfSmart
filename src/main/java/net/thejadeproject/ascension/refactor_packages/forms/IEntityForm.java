package net.thejadeproject.ascension.refactor_packages.forms;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public interface IEntityForm {

    void enterForm(LivingEntity entity, IEntityFormData previousFormData);
    void leaveForm(LivingEntity entity);

    void newFormAddedPre(LivingEntity entity,ResourceLocation entityForm,IEntityFormData entityFormData);
    void newFormAddedPost(LivingEntity entity,ResourceLocation entityForm);
    IEntityFormData freshEntityFormData(LivingEntity entity);
    IEntityFormData fromCompound(CompoundTag tag,LivingEntity entity);
    IEntityFormData fromNetwork(RegistryFriendlyByteBuf buf,LivingEntity entity);
}
