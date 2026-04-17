package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.ITextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.EmptySkillData;

public abstract class AbstractRunicPassiveSkill implements ISkill {

    protected final Component title;
    protected Component shortDescription = Component.empty();
    protected Component description = Component.empty();
    protected ITextureData icon;

    protected AbstractRunicPassiveSkill(Component title) {
        this.title = title;
    }



    // --- Fluent setters ---
    public AbstractRunicPassiveSkill setShortDescription(Component shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public AbstractRunicPassiveSkill setDescription(Component description) {
        this.description = description;
        return this;
    }

    public AbstractRunicPassiveSkill setIcon(ITextureData icon) {
        this.icon = icon;
        return this;
    }



    // --- Lifecycle hooks (centralized) ---
    @Override
    public void onAdded(IEntityData entityData) {
        apply(entityData);
    }

    @Override
    public void onRemoved(IEntityData entityData, IPersistentSkillData persistentData) {
        remove(entityData);
    }

    @Override
    public void onFormAdded(IEntityData entityData, ResourceLocation form, IPhysiqueData physiqueData) {
        apply(entityData);
    }

    @Override
    public void onFormRemoved(IEntityData entityData, ResourceLocation form, IPhysiqueData physiqueData) {
        remove(entityData);
    }



    // --- Subclass responsibilities ---
    protected abstract void apply(IEntityData entityData);

    protected abstract void remove(IEntityData entityData);



    // --- Boilerplate implementations ---
    @Override
    public void finishedCooldown(IEntityData attachedEntityData, String identifier) {}

    @Override
    public IPersistentSkillData freshPersistentData(IEntityData heldEntity) {
        return new EmptySkillData();
    }

    @Override
    public IPersistentSkillData fromCompound(CompoundTag tag, IEntityData heldEntity) {
        return new EmptySkillData();
    }

    @Override
    public IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf) {
        return new EmptySkillData();
    }

    @Override
    public ITextureData getIcon() {
        return icon;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public Component getDescription() {
        return description;
    }

    public Component getShortDescription() {
        return shortDescription;
    }
}