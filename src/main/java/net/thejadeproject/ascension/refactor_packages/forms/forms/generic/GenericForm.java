package net.thejadeproject.ascension.refactor_packages.forms.forms.generic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;

public class GenericForm implements IEntityForm {
    @Override
    public void onEntityTethered(IEntityData heldEntity, IEntityData tetheredEntity) {

    }

    @Override
    public void onEntityUntethered(IEntityData heldEntity, IEntityData oldTetheredEntity) {

    }

    @Override
    public void enterForm(IEntityData heldEntity, ResourceLocation previousForm) {

    }

    @Override
    public void leaveForm(IEntityData heldEntity) {

    }

    @Override
    public void onAdded(IEntityData heldEntity) {

    }

    @Override
    public void onRemoved(IEntityData heldEntity, IEntityFormData formData) {

    }

    @Override
    public void onFormAdded(IEntityData heldEntity, IEntityFormData formData, IEntityFormData addedFormData) {
        //wrong form data type
        if(!(formData instanceof GenericFormData genericFormData)) return;

        //handle cultivation
        genericFormData.getAllPathData().forEach(pathData -> pathData.onFormAdded(heldEntity,addedFormData));

        //handle physique and bloodline
        if(genericFormData.getBloodlineData() != null){
            //TODO
        }
        if(genericFormData.getPhysiqueData() != null){
            //TODO
        }

    }

    @Override
    public void onFormRemoved(IEntityData heldEntity, IEntityFormData formData, IEntityFormData removedFormData) {
        //wrong form data type
        if(!(formData instanceof GenericFormData genericFormData)) return;

        //handle cultivation
        genericFormData.getAllPathData().forEach(pathData -> pathData.onFormRemoved(heldEntity,removedFormData));

        //handle physique and bloodline
        if(genericFormData.getBloodlineData() != null){
            //TODO
        }
        if(genericFormData.getPhysiqueData() != null){
            //TODO
        }


    }

    @Override
    public IEntityFormData freshEntityFormData(IEntityData heldEntity) {
        return null;
    }

    @Override
    public IEntityFormData fromCompound(CompoundTag tag, IEntityData heldEntity) {
        return null;
    }

    @Override
    public IEntityFormData fromNetwork(RegistryFriendlyByteBuf buf, IEntityData heldEntity) {
        return null;
    }
}
