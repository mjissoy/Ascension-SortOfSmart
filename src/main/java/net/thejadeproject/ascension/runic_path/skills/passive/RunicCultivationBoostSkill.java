package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.EmptySkillData;

public class RunicCultivationBoostSkill implements ISkill {

    private final Component title;
    private Component shortDescription = Component.empty();
    private Component description = Component.empty();

    public RunicCultivationBoostSkill(Component title) {
        this.title = title;
    }

    public RunicCultivationBoostSkill setShortDescription(Component shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public RunicCultivationBoostSkill setDescription(Component description) {
        this.description = description;
        return this;
    }

    @Override
    public void onFormAdded(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {}

    @Override
    public void onFormRemoved(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {}

    @Override
    public void onAdded(IEntityData attachedEntityData) {}

    @Override
    public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {}

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
        return new TextureData(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/icon/runic_cultivation_boost.png"),
                16, 16
        );
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