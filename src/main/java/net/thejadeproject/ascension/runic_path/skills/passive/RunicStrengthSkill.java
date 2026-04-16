package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.ISkill;
import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.EmptySkillData;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;

public class RunicStrengthSkill implements ISkill {

    private static final ResourceLocation STRENGTH_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_strength_strength");

    private final Component title;
    private Component shortDescription = Component.empty();
    private Component description = Component.empty();

    public RunicStrengthSkill(Component title) {
        this.title = title;
    }

    public RunicStrengthSkill setShortDescription(Component shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public RunicStrengthSkill setDescription(Component description) {
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
        if (entity.getActiveFormData().getStatSheet().getStatInstance(ModStats.STRENGTH.get()) == null) return;

        remove(entity);

        double strengthBonus = getStrengthBonus(entity);

        entity.getActiveFormData()
                .getStatSheet()
                .getStatInstance(ModStats.STRENGTH.get())
                .addModifier(makeModifier(STRENGTH_ID, strengthBonus));

        entity.getActiveFormData().getStatSheet().sync(
                (net.minecraft.server.level.ServerPlayer) entity.getAttachedEntity(),
                entity.getActiveFormData().getEntityFormId()
        );
    }

    private double getStrengthBonus(IEntityData entity) {
        int realm = 0;
        if (entity.hasPath(ModPaths.RUNIC.getId())) {
            realm = entity.getPathData(ModPaths.RUNIC.getId()).getMajorRealm();
        }

        return 2.0 * (realm + 1);
    }

    private void remove(IEntityData entity) {
        if (entity == null || entity.getActiveFormData() == null) return;
        if (entity.getActiveFormData().getStatSheet() == null) return;
        if (entity.getActiveFormData().getStatSheet().getStatInstance(ModStats.STRENGTH.get()) == null) return;

        entity.getActiveFormData()
                .getStatSheet()
                .getStatInstance(ModStats.STRENGTH.get())
                .removeModifier(STRENGTH_ID);

        if (entity.getAttachedEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            entity.getActiveFormData().getStatSheet().sync(
                    serverPlayer,
                    entity.getActiveFormData().getEntityFormId()
            );
        }
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
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/icon/runic_strength.png"),
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