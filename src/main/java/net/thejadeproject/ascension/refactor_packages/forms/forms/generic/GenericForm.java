package net.thejadeproject.ascension.refactor_packages.forms.forms.generic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

public class GenericForm implements IEntityForm {

    private final Component title;

    public GenericForm(Component title){
        this.title=title;
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
            genericFormData.getBloodline().onFormAdded(heldEntity,addedFormData.getEntityFormId(), genericFormData.getBloodlineData());
        }
        if(genericFormData.getPhysiqueData() != null){
            genericFormData.getPhysique().onFormAdded(heldEntity,addedFormData.getEntityFormId(),genericFormData.getPhysiqueData());
        }

        //TODO go through skills


    }

    @Override
    public void onFormRemoved(IEntityData heldEntity, IEntityFormData formData, IEntityFormData removedFormData) {
        //wrong form data type
        if(!(formData instanceof GenericFormData genericFormData)) return;

        //handle cultivation
        genericFormData.getAllPathData().forEach(pathData -> pathData.onFormRemoved(heldEntity,removedFormData));

        //handle physique and bloodline
        if(genericFormData.getBloodlineData() != null){
            genericFormData.getBloodline().onFormRemoved(heldEntity,removedFormData.getEntityFormId(), genericFormData.getBloodlineData());
        }
        if(genericFormData.getPhysiqueData() != null){
            genericFormData.getPhysique().onFormRemoved(heldEntity,removedFormData.getEntityFormId(),genericFormData.getPhysiqueData());
        }

        //TODO go through skills


    }

    @Override
    public IEntityFormData freshEntityFormData(IEntityData heldEntity) {
        return new GenericFormData(AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.getKey(this));
    }

    @Override
    public IEntityFormData fromCompound(CompoundTag tag, IEntityData heldEntity) {
        return new GenericFormData(AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.getKey(this));
    }

    @Override
    public IEntityFormData fromNetwork(RegistryFriendlyByteBuf buf, IEntityData heldEntity) {
        //TODO properly decode it
        return new GenericFormData(AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.getKey(this));
    }
}
