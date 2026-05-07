package net.thejadeproject.ascension.refactor_packages.skills.custom.active.utility;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.forms.forms.ModForms;
import net.thejadeproject.ascension.refactor_packages.gui.elements.info_elements.DescriptionDisplayContainer;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.ModSkills;
import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.debuff.skill_data.DebuffSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.passive.debuff.skill_data.DebuffSkillHelper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class PurifyingMantraSkill implements ICastableSkill {

    private static final double VANILLA_EFFECT_QI_COST = 40.0D;
    private static final double TEMP_DEBUFF_QI_COST = 80.0D;
    private static final double PERMANENT_DEBUFF_BASE_QI_COST = 600.0D;
    private static final int COOLDOWN_TICKS = 20 * 20;

    private static final List<ResourceLocation> PURGEABLE_DEBUFFS = List.of(
            ModSkills.CRACKED_MERIDIANS.getId(),
            ModSkills.BLINDED_SENSES.getId(),
            ModSkills.PARALYZED_BODY.getId(),
            ModSkills.VENOMOUS_MERIDIANS.getId(),
            ModSkills.SCORCHING_YANG_POISON.getId(),
            ModSkills.QI_DEVOURING_POISON.getId(),
            ModSkills.CORROSIVE_POISON_DEBUFF.getId(),
            ModSkills.FROST_SILKWORM_POISON_TEMP.getId(),
            ModSkills.FROST_SILKWORM_POISON.getId()
    );

    private static final Set<ResourceLocation> PERMANENT_DEBUFFS = Set.of(
            ModSkills.FROST_SILKWORM_POISON.getId()
    );

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        if (caster.level().isClientSide()) {
            return new CastResult(CastResult.Type.SUCCESS);
        }

        if (!(caster instanceof ServerPlayer player)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        if (!player.hasData(ModAttachments.ENTITY_DATA)) {
            return new CastResult(CastResult.Type.FAILURE);
        }

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        CleanseTarget target = findCleanseTarget(player, entityData);
        if (target == null) {
            return new CastResult(
                    CastResult.Type.FAILURE,
                    Component.translatable("ascension.skill.purifying_mantra.no_impurity")
            );
        }

        if (!entityData.getQiContainer().hasQi(target.qiCost())) {
            return new CastResult(
                    CastResult.Type.FAILURE,
                    Component.translatable("ascension.skill.purifying_mantra.not_enough_qi")
            );
        }

        return new CastResult(CastResult.Type.SUCCESS);
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {
        if (!(caster instanceof ServerPlayer player)) return;
        if (!player.hasData(ModAttachments.ENTITY_DATA)) return;

        IEntityData entityData = player.getData(ModAttachments.ENTITY_DATA);

        CleanseTarget target = findCleanseTarget(player, entityData);
        if (target == null) {
            player.sendSystemMessage(Component.translatable("ascension.skill.purifying_mantra.no_impurity"));
            return;
        }

        if (!entityData.getQiContainer().tryConsumeQi(target.qiCost())) {
            player.sendSystemMessage(Component.translatable("ascension.skill.purifying_mantra.not_enough_qi"));
            return;
        }

        target.cleanse(player, entityData);

        player.sendSystemMessage(Component.translatable("ascension.skill.purifying_mantra.success"));
    }

    private CleanseTarget findCleanseTarget(ServerPlayer player, IEntityData entityData) {
        CleanseTarget vanilla = findVanillaEffect(player);
        if (vanilla != null) return vanilla;

        CleanseTarget temporaryDebuff = findDebuffSkill(player, entityData, false);
        if (temporaryDebuff != null) return temporaryDebuff;

        return findDebuffSkill(player, entityData, true);
    }

    private CleanseTarget findVanillaEffect(ServerPlayer player) {
        MobEffectInstance effect = player.getActiveEffects().stream()
                .filter(instance -> !instance.getEffect().value().isBeneficial())
                .max(Comparator.comparingInt(instance ->
                        instance.getDuration() * (instance.getAmplifier() + 1)
                ))
                .orElse(null);

        if (effect == null) return null;

        return new CleanseTarget(
                VANILLA_EFFECT_QI_COST,
                (p, data) -> p.removeEffect(effect.getEffect())
        );
    }

    private CleanseTarget findDebuffSkill(ServerPlayer player, IEntityData entityData, boolean permanent) {
        for (ResourceLocation debuffId : PURGEABLE_DEBUFFS) {
            if (!entityData.hasSkill(debuffId)) continue;

            DebuffSkillData data = DebuffSkillHelper.getDebuffData(entityData, debuffId);

            boolean isPermanent = PERMANENT_DEBUFFS.contains(debuffId);

            if (isPermanent != permanent) continue;

            double cost = permanent
                    ? getPermanentCleanseCost(entityData)
                    : TEMP_DEBUFF_QI_COST;

            return new CleanseTarget(
                    cost,
                    (p, entity) -> entity.removeSkill(debuffId, ModForms.MORTAL_VESSEL.getId())
            );
        }

        return null;
    }

    private double getPermanentCleanseCost(IEntityData entityData) {
        return PERMANENT_DEBUFF_BASE_QI_COST;
    }

    private record CleanseTarget(
            double qiCost,
            CleanseAction action
    ) {
        void cleanse(ServerPlayer player, IEntityData entityData) {
            action.cleanse(player, entityData);
        }
    }

    @FunctionalInterface
    private interface CleanseAction {
        void cleanse(ServerPlayer player, IEntityData entityData);
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderableElement getCastElement(UIFrame frame) {
        return null;
    }

    @Override
    public int getCooldown(CastEndData castEndData) {
        return COOLDOWN_TICKS;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("ascension.skill.purifying_mantra");
    }

    @Override
    public Component getDescription() {
        return Component.translatable("ascension.skill.purifying_mantra.description");
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderableElement getInformationContainer(UIFrame frame) {
        return new DescriptionDisplayContainer(frame, getTitle(), getDescription());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextureData getIcon() {
        return new TextureData(
                ResourceLocation.fromNamespaceAndPath(
                        AscensionCraft.MOD_ID,
                        "textures/spells/icon/placeholder_gold.png"
                ),
                16,
                16
        );
    }

    @Override public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) { return false; }
    @Override public void finalCast(CastEndData reason, Entity caster, ICastData castData) {}
    @Override public void onEquip(IEntityData entityData) {}
    @Override public void onUnEquip(IEntityData entityData, IPreCastData preCastData) {}
    @Override public void selected(IEntityData entityData) {}
    @Override public void unselected(IEntityData entityData) {}
    @Override public IPreCastData freshPreCastData() { return null; }
    @Override public IPreCastData preCastDataFromCompound(CompoundTag tag) { return null; }
    @Override public IPreCastData preCastDataFromNetwork(RegistryFriendlyByteBuf buf) { return null; }
    @Override public ICastData freshCastData() { return null; }
    @Override public ICastData castDataFromCompound(CompoundTag tag) { return null; }
    @Override public ICastData castDataFromNetwork(RegistryFriendlyByteBuf buf) { return null; }
    @Override public IPersistentSkillData freshPersistentInstance() { return null; }
    @Override public IPersistentSkillData persistentInstanceFromCompound(CompoundTag tag) { return null; }
    @Override public IPersistentSkillData persistentInstanceFromNetwork(RegistryFriendlyByteBuf buf) { return null; }
    @Override public void onAdded(IEntityData attachedEntityData) {}
    @Override public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {}

    @Override
    public void onFormAdded(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {

    }

    @Override
    public void onFormRemoved(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {

    }

    @Override
    public void finishedCooldown(IEntityData attachedEntityData, String identifier) {

    }

    @Override public IPersistentSkillData freshPersistentData(IEntityData heldEntity) { return null; }
    @Override public IPersistentSkillData fromCompound(CompoundTag tag, IEntityData heldEntity) { return null; }
    @Override public IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf) { return null; }
}