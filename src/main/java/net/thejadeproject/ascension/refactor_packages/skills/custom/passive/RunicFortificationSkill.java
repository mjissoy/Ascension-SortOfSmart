package net.thejadeproject.ascension.refactor_packages.skills.custom.passive;

import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;

public class RunicFortificationSkill implements ISkill {

    private static final ResourceLocation ARMOR_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_fortification_armor");

    private static final ResourceLocation TOUGHNESS_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_fortification_toughness");

    private final Component title;
    private Component shortDescription = Component.empty();
    private Component description = Component.empty();

    public RunicFortificationSkill(Component title) {
        this.title = title;
    }

    public RunicFortificationSkill setShortDescription(Component shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public RunicFortificationSkill setDescription(Component description) {
        this.description = description;
        return this;
    }

    @Override
    public void onFormAdded(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {
        apply(heldEntity);
    }

    @Override
    public void onFormRemoved(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {
        remove(heldEntity);
    }

    @Override public void onAdded(IEntityData attachedEntityData) {}
    @Override public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {}

    private void apply(IEntityData entity) {
        var holder = entity.getAscensionAttributeHolder();
        if (holder == null) return;

        var armor = holder.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.addModifier(makeModifier(ARMOR_ID, 2.0));
        }

        var toughness = holder.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (toughness != null) {
            toughness.addModifier(makeModifier(TOUGHNESS_ID, 1.0));
        }

        holder.updateAttributes(entity);
    }


    private void remove(IEntityData entity) {
        var holder = entity.getAscensionAttributeHolder();
        if (holder == null) return;

        var armor = holder.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.removeModifier(ARMOR_ID);
        }

        var toughness = holder.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (toughness != null) {
            toughness.removeModifier(TOUGHNESS_ID);
        }

        holder.updateAttributes(entity);
    }

    private ValueContainerModifier makeModifier(ResourceLocation id, double value) {
        return new ValueContainerModifier(
                value,
                ModifierOperation.ADD_BASE,
                id
        );
    }

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
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"textures/spells/iron_body.png"),
                16,16
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