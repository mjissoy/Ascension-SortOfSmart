package net.thejadeproject.ascension.refactor_packages.forms;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

import java.util.UUID;

public interface IEntityForm {



    void enterForm(IEntityData heldEntity, ResourceLocation previousForm);
    void leaveForm(IEntityData heldEntity);

    void onAdded(IEntityData heldEntity);
    void onRemoved(IEntityData heldEntity,IEntityFormData formData);

    /*
        handles when forms are added and removed. is also called if an untethered entity held forms
     */
    void onFormAdded(IEntityData heldEntity,IEntityFormData formData, IEntityFormData addedFormData);
    void onFormRemoved(IEntityData heldEntity,IEntityFormData formData,IEntityFormData removedFormData);

    IEntityFormData freshEntityFormData(IEntityData heldEntity);
    IEntityFormData fromCompound(CompoundTag tag,IEntityData heldEntity);
    IEntityFormData fromNetwork(RegistryFriendlyByteBuf buf);
    void encode(RegistryFriendlyByteBuf buf,IEntityFormData formData);
}
