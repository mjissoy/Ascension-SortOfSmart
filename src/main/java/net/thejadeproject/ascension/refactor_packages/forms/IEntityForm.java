package net.thejadeproject.ascension.refactor_packages.forms;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;

import java.util.UUID;

public interface IEntityForm {

    /*
        onEntityTethered is called when an entities data is tethered to another existing entity.
        not called when,for example, a fresh tethered vessel is created. Since in that instance data is only being
        moved around. nothing new is being added or removed

        same with Untethered. called when there is some sort of data being added or removed rather than just moved around
     */
    void onEntityTethered(IEntityData heldEntity,IEntityData tetheredEntity);
    void onEntityUntethered(IEntityData heldEntity,IEntityData oldTetheredEntity);


    void enterForm(IEntityData heldEntity, ResourceLocation previousForm);
    void leaveForm(IEntityData heldEntity);

    void onAdded(IEntityData heldEntity);
    void onRemoved(IEntityData heldEntity,IEntityFormData formData);

    /*
        handles when forms are added and removed. is also called if an untethered entity held forms
     */
    void onFormAdded(IEntityData heldEntity,ResourceLocation form);
    void onFormRemoved(IEntityData heldEntity,ResourceLocation form);

    IEntityFormData freshEntityFormData(LivingEntity entity);
    IEntityFormData fromCompound(CompoundTag tag,LivingEntity entity);
    IEntityFormData fromNetwork(RegistryFriendlyByteBuf buf,LivingEntity entity);
}
