package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.EmptySkillData;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;
import net.thejadeproject.ascension.runic_path.RunicScalingHelper;

public class RunicVitalitySkill implements ISkill {

    private static final ResourceLocation VITALITY_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_vitality_stat");

    private final Component title;
    private Component shortDescription = Component.empty();
    private Component description = Component.empty();

    public RunicVitalitySkill(Component title) {
        this.title = title;
    }

    public RunicVitalitySkill setShortDescription(Component shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public RunicVitalitySkill setDescription(Component description) {
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
        if (entity == null || entity.getActiveFormData() == null) return;
        if (entity.getActiveFormData().getStatSheet() == null) return;
        if (entity.getActiveFormData().getStatSheet().getStatInstance(ModStats.VITALITY.get()) == null) return;

        remove(entity);

        double vitalityBonus = getVitalityBonus(entity);

        entity.getActiveFormData()
                .getStatSheet()
                .getStatInstance(ModStats.VITALITY.get())
                .addModifier(makeModifier(VITALITY_ID, vitalityBonus));

        if (entity.getAttachedEntity() instanceof ServerPlayer serverPlayer) {
            entity.getActiveFormData().getStatSheet().sync(
                    serverPlayer,
                    entity.getActiveFormData().getEntityFormId()
            );
        }
    }

    private void remove(IEntityData entity) {
        if (entity == null || entity.getActiveFormData() == null) return;
        if (entity.getActiveFormData().getStatSheet() == null) return;
        if (entity.getActiveFormData().getStatSheet().getStatInstance(ModStats.VITALITY.get()) == null) return;

        entity.getActiveFormData()
                .getStatSheet()
                .getStatInstance(ModStats.VITALITY.get())
                .removeModifier(VITALITY_ID);

        if (entity.getAttachedEntity() instanceof ServerPlayer serverPlayer) {
            entity.getActiveFormData().getStatSheet().sync(
                    serverPlayer,
                    entity.getActiveFormData().getEntityFormId()
            );
        }
    }

    private double getVitalityBonus(IEntityData entity) {
        return RunicScalingHelper.scaleFlat(2.0, 1.0, entity);
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
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/icon/runic_vitality.png"),
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