package net.thejadeproject.ascension.refactor_packages.entity_data;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;

import java.util.List;
import java.util.UUID;

public class RemoteEntityData implements IEntityData{
    private UUID attachedEntity;


    @Override
    public UUID getAttachedEntity() {
        return attachedEntity;
    }

    @Override
    public IEntityFormData getActiveFormData() {
        return EntityDataManager.getEntityData(attachedEntity).getActiveFormData();
    }

    @Override
    public IEntityFormData removeEntityForm(ResourceLocation form) {
        return EntityDataManager.getEntityData(attachedEntity).removeEntityForm(form);
    }

    @Override
    public IEntityFormData getEntityFormData(ResourceLocation form) {
        return EntityDataManager.getEntityData(attachedEntity).getEntityFormData(form);
    }

    @Override
    public IEntityFormData getEntityFormData(IEntityForm form) {
        return getEntityFormData(AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.getKey(form));
    }

    @Override
    public void addNewEntityForm(ResourceLocation form) {

    }

    @Override
    public void addExistingEntityForm(ResourceLocation form, IEntityFormData data) {

    }

    @Override
    public void changeActiveFormTo(ResourceLocation form) {

    }

    @Override
    public void moveFormToTetheredEntity(UUID entityId) {

    }

    @Override
    public List<UUID> getTetheredEntities() {
        return List.of();
    }

    @Override
    public void addTemporaryForm(ResourceLocation form) {

    }

    @Override
    public void addExistingTemporaryForm(ResourceLocation form, IEntityFormData data) {

    }
}
