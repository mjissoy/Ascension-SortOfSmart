package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.EmptySkillData;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;
import net.thejadeproject.ascension.runic_path.RunicScalingHelper;

public class RunicArmorSkill implements ISkill {

    private static final ResourceLocation ARMOR_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_armor_armor");

    private static final ResourceLocation TOUGHNESS_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_armor_toughness");

    private final Component title;
    private Component shortDescription = Component.empty();
    private Component description = Component.empty();

    public RunicArmorSkill(Component title) {
        this.title = title;
    }

    public RunicArmorSkill setShortDescription(Component shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public RunicArmorSkill setDescription(Component description) {
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

    @Override
    public void onAdded(IEntityData attachedEntityData) {
        apply(attachedEntityData);
    }

    @Override
    public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {
        remove(attachedEntityData);
    }

    private void apply(IEntityData entity) {
        if (entity == null) return;
        if (entity.getAscensionAttributeHolder() == null) return;

        remove(entity);

        double armorBonus = getArmorBonus(entity);
        double toughnessBonus = getToughnessBonus(entity);

        var holder = entity.getAscensionAttributeHolder();

        var armor = holder.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.addModifier(makeModifier(ARMOR_ID, armorBonus));
        }

        var toughness = holder.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (toughness != null) {
            toughness.addModifier(makeModifier(TOUGHNESS_ID, toughnessBonus));
        }

        holder.updateAttributes(entity);
    }

    private void remove(IEntityData entity) {
        if (entity == null) return;
        if (entity.getAscensionAttributeHolder() == null) return;

        var holder = entity.getAscensionAttributeHolder();

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

    private double getArmorBonus(IEntityData entity) {
        return RunicScalingHelper.scaleFlat(2.0, 1.0, entity);
    }

    private double getToughnessBonus(IEntityData entity) {
        return RunicScalingHelper.scaleFlat(1.0, 0.5, entity);
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
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/icon/runic_armor.png"),
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