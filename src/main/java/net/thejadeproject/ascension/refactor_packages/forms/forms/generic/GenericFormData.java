package net.thejadeproject.ascension.refactor_packages.forms.forms.generic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.attributes.AscensionAttributesContainer;
import net.thejadeproject.ascension.refactor_packages.bloodlines.IBloodlineData;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityForm;
import net.thejadeproject.ascension.refactor_packages.forms.IEntityFormData;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skills.HeldSkills;
import net.thejadeproject.ascension.refactor_packages.stats.StatSheet;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GenericFormData implements IEntityFormData {
    private UUID attachedEntity;
    private final ResourceLocation formId;
    private final HashMap<ResourceLocation,PathData> pathData = new HashMap<>();

    private IPhysiqueData physiqueData;
    private IBloodlineData bloodlineData;

    private StatSheet statSheet;

    private HeldSkills heldSkills;
    public GenericFormData(ResourceLocation formId){
        this.formId = formId;
    }

    @Override
    public UUID getAttachedEntity() {
        return attachedEntity;
    }

    @Override
    public void setAttachedEntity(UUID entity) {
        this.attachedEntity = entity;
    }

    @Override
    public ResourceLocation getEntityFormId() {
        return formId;
    }

    @Override
    public IEntityForm getEntityForm() {
        return AscensionRegistries.EntityForms.ENTITY_FORMS_REGISTRY.get(formId);
    }

    @Override
    public PathData getPathData(ResourceLocation path) {
        return pathData.get(path);
    }

    @Override
    public Collection<PathData> getAllPathData() {
        return pathData.values();
    }

    @Override
    public boolean hasPathData(ResourceLocation path) {
        return pathData.containsKey(path);
    }

    @Override
    public HeldSkills getHeldSkills() {
        return heldSkills;
    }

    @Override
    public IPhysiqueData getPhysiqueData() {
        return physiqueData;
    }

    @Override
    public void setPhysiqueData(IPhysiqueData data) {
        this.physiqueData = data;
    }

    @Override
    public IBloodlineData getBloodlineData() {
        return bloodlineData;
    }

    @Override
    public void setBloodlineData(IBloodlineData data) {
        bloodlineData = data;
    }


    @Override
    public StatSheet getStatSheet() {
        return statSheet;
    }

    @Override
    public CompoundTag write() {
        return null;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {

    }
}
