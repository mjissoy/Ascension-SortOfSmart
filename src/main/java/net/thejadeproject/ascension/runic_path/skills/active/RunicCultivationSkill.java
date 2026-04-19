package net.thejadeproject.ascension.runic_path.skills.active;

import net.lucent.easygui.gui.RenderableElement;
import net.lucent.easygui.gui.UIFrame;
import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.lucent.easygui.gui.textures.TextureDataSubsection;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.paths.ModPaths;
import net.thejadeproject.ascension.refactor_packages.paths.PathData;
import net.thejadeproject.ascension.refactor_packages.physiques.IPhysiqueData;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastEndData;
import net.thejadeproject.ascension.refactor_packages.skill_casting.casting.CastResult;
import net.thejadeproject.ascension.refactor_packages.skills.IPersistentSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.CastType;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastableSkill;
import net.thejadeproject.ascension.refactor_packages.skills.castable.IPreCastData;
import net.thejadeproject.ascension.runic_path.skills.active.skill_data.RunicCultivationSkillData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.skill_data.empty.EmptyCastData;
import net.thejadeproject.ascension.refactor_packages.skills.custom.cultivation.skill_data.empty.EmptyPreCastData;
import net.thejadeproject.ascension.refactor_packages.techniques.ITechnique;
import net.thejadeproject.ascension.runic_path.skills.passive.helpers.CultivationModifierHelper;
import net.thejadeproject.ascension.runic_path.technique.RunicTechnique;

public class RunicCultivationSkill implements ICastableSkill {

    private static final ResourceLocation PATH = ModPaths.RUNIC.getId();

    private final double baseRate;

    public RunicCultivationSkill(double baseRate) {
        this.baseRate = baseRate;
    }


    @Override
    public void onAdded(IEntityData attachedEntityData) {
        System.out.println("added skill : " + AscensionRegistries.Skills.SKILL_REGISTRY.getKey(this));
    }

    @Override
    public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {
        System.out.println("removed skill : " + AscensionRegistries.Skills.SKILL_REGISTRY.getKey(this));
    }

    @Override
    public void onFormAdded(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {
    }

    @Override
    public void onFormRemoved(IEntityData heldEntity, ResourceLocation form, IPhysiqueData physiqueData) {
    }

    @Override
    public void finishedCooldown(IEntityData attachedEntityData, String identifier) {
    }

    @Override
    public IPersistentSkillData freshPersistentData(IEntityData heldEntity) {
        return new RunicCultivationSkillData(baseRate);
    }

    @Override
    public IPersistentSkillData fromCompound(CompoundTag tag, IEntityData heldEntity) {
        return new RunicCultivationSkillData(tag);
    }

    @Override
    public IPersistentSkillData fromNetwork(RegistryFriendlyByteBuf buf) {
        return new RunicCultivationSkillData(buf);
    }

    @Override
    public ITextureData getIcon() {
        return new TextureData(
                ResourceLocation.fromNamespaceAndPath(
                        AscensionCraft.MOD_ID,
                        "textures/spells/icon/runic_cultivation.png"
                ),
                16, 16
        );
    }

    @Override
    public Component getTitle() {
        return Component.translatable("ascension.skill.runic_cultivation");
    }

    @Override
    public Component getDescription() {
        return Component.translatable("ascension.skill.runic_cultivation.description");
    }

    @Override
    public void onEquip(IEntityData entityData) {
    }

    @Override
    public void onUnEquip(IEntityData entityData, IPreCastData preCastData) {
    }

    @Override
    public void finalCast(CastEndData reason, Entity caster, ICastData castData) {
    }

    @Override
    public void initialCast(Entity caster, IPreCastData preCastData) {

    }

    @Override
    public CastResult canCast(Entity caster, IPreCastData preCastData) {
        if (!caster.hasData(ModAttachments.ENTITY_DATA)) {
            return new CastResult(CastResult.Type.FAILURE, Component.literal("No entity data."));
        }

        IEntityData entityData = caster.getData(ModAttachments.ENTITY_DATA);
        if (!entityData.hasPath(PATH)) {
            return new CastResult(CastResult.Type.FAILURE, Component.literal("You do not possess the Runic Path."));
        }

        PathData pathData = entityData.getPathData(PATH);
        if (pathData == null) {
            return new CastResult(CastResult.Type.FAILURE, Component.literal("Runic path data missing."));
        }

        ResourceLocation techniqueId = pathData.getLastUsedTechnique();
        if (techniqueId == null) {
            return new CastResult(CastResult.Type.FAILURE, Component.literal("No Runic technique selected."));
        }

        if (!AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.containsKey(techniqueId)) {
            return new CastResult(CastResult.Type.FAILURE, Component.literal("Invalid Runic technique."));
        }

        return new CastResult(CastResult.Type.SUCCESS);
    }

    @Override
    public boolean continueCasting(int ticksElapsed, Entity caster, ICastData castData) {
        if (!caster.hasData(ModAttachments.INPUT_STATES)) return false;
        if (!caster.getData(ModAttachments.INPUT_STATES).isHeld("skill_cast")) return false;

        if (!caster.level().isClientSide()) {
            IEntityData entityData = caster.getData(ModAttachments.ENTITY_DATA);
            if (entityData == null || !entityData.hasPath(PATH)) return false;

            PathData pathData = entityData.getPathData(PATH);
            if (pathData == null) return false;

            ResourceLocation techniqueId = pathData.getLastUsedTechnique();
            if (techniqueId == null) return false;
            if (!AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.containsKey(techniqueId)) return false;

            ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(techniqueId);

            int majorRealm = pathData.getMajorRealm();
            int minorRealm = pathData.getMinorRealm();

            if (majorRealm > technique.getMaxMajorRealm()) {
                return false;
            }

            double techniqueMultiplier = 1.0;
            if (technique instanceof RunicTechnique runicTechnique) {
                if (!runicTechnique.canCultivate(entityData)) {
                    return false;
                }
                techniqueMultiplier = runicTechnique.getCultivationMultiplier(entityData);
            }

            double amount = getRunicDrawAmount(ticksElapsed, pathData, technique)
                    * CultivationModifierHelper.getMultiplierForPath(entityData, PATH)
                    * techniqueMultiplier;

            RunicCultivationSkillData persistentData = getPersistentRunicData(entityData);
            if (persistentData != null && amount > 0) {
                persistentData.addRunesDrawn(amount);
            }

            double realmCap = technique.getMaxQiForRealm(majorRealm, minorRealm);
            double current = pathData.getCurrentRealmProgress();
            double newProgress = Math.min(realmCap, current + amount);

            pathData.setCurrentRealmProgress(newProgress);

            if (newProgress >= realmCap) {
                handleRunicRealmCapReached(entityData, pathData, technique);
            }

            if (caster instanceof Player player) {
                pathData.sync(player);
            }
        }

        return true;
    }

    private double getRunicDrawAmount(int ticksElapsed, PathData pathData, ITechnique technique) {
        int majorRealm = pathData.getMajorRealm();
        double techniqueRate = getTechniqueRateOrBase(technique);

        return switch (majorRealm) {
            case 0 -> techniqueRate;
            case 1 -> techniqueRate * 0.75;
            case 2 -> techniqueRate * 0.55;
            case 3 -> techniqueRate * 0.35;
            default -> 0.0;
        };
    }

    private double getTechniqueRateOrBase(ITechnique technique) {
        return baseRate;
    }

    private void handleRunicRealmCapReached(IEntityData entityData, PathData pathData, ITechnique technique) {
        int majorRealm = pathData.getMajorRealm();
        int minorRealm = pathData.getMinorRealm();

        if (minorRealm < technique.getMaxMinorRealm(majorRealm) &&
                technique.canBreakthroughMinorRealm(
                        entityData,
                        majorRealm,
                        minorRealm,
                        pathData.getCurrentRealmProgress()
                )) {

            pathData.handleRealmChange(majorRealm, minorRealm + 1, entityData);
            return;
        }

        if (majorRealm < technique.getMaxMajorRealm() && entityData.isBreakingThrough(PATH)) {
            pathData.setBreakingThrough(false);
            pathData.handleRealmChange(majorRealm + 1, 0, entityData);
            return;
        }

        if (majorRealm < technique.getMaxMajorRealm()
                && technique.getStabilityHandler() != null
                && pathData.getCurrentRealmStability() < technique.getStabilityHandler().getMaxCultivationTicks()) {
            pathData.setCurrentRealmStability(pathData.getCurrentRealmStability() + 1);
        }
    }


    private RunicCultivationSkillData getPersistentRunicData(IEntityData entityData) {
        IPersistentSkillData raw = entityData.getSkillData(
                AscensionRegistries.Skills.SKILL_REGISTRY.getKey(this)
        );
        if (raw instanceof RunicCultivationSkillData runicData) {
            return runicData;
        }
        return null;
    }

    @Override
    public int getCooldown(CastEndData castEndData) {
        return 0;
    }

    @Override
    public void selected(IEntityData entityData) {
    }

    @Override
    public void unselected(IEntityData entityData) {
    }

    @Override
    public IPreCastData freshPreCastData() {
        return new EmptyPreCastData();
    }

    @Override
    public IPreCastData preCastDataFromCompound(CompoundTag tag) {
        return new EmptyPreCastData();
    }

    @Override
    public IPreCastData preCastDataFromNetwork(RegistryFriendlyByteBuf buf) {
        return new EmptyPreCastData();
    }

    @Override
    public ICastData freshCastData() {
        return new EmptyCastData();
    }

    @Override
    public ICastData castDataFromCompound(CompoundTag tag) {
        return new EmptyCastData();
    }

    @Override
    public ICastData castDataFromNetwork(RegistryFriendlyByteBuf buf) {
        return new EmptyCastData();
    }

    @Override
    public IPersistentSkillData freshPersistentInstance() {
        return new RunicCultivationSkillData(baseRate);
    }

    @Override
    public IPersistentSkillData persistentInstanceFromCompound(CompoundTag tag) {
        return new RunicCultivationSkillData(tag);
    }

    @Override
    public IPersistentSkillData persistentInstanceFromNetwork(RegistryFriendlyByteBuf buf) {
        return new RunicCultivationSkillData(buf);
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderableElement getCastElement(UIFrame frame) {
        return new net.thejadeproject.ascension.gui.elements.skills.cultivation.CultivationProgressBar(
                frame,
                new TextureDataSubsection(
                        ResourceLocation.fromNamespaceAndPath(
                                AscensionCraft.MOD_ID,
                                "textures/gui/overlay/overlays_all.png"
                        ),
                        256,
                        256,
                        0,
                        0,
                        65,
                        7
                ),
                PATH
        );
    }
}