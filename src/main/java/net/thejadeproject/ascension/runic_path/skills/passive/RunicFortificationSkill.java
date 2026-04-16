package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.ITextureData;
import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
import net.thejadeproject.ascension.runic_path.Rune;
import net.thejadeproject.ascension.runic_path.RuneEffectType;
import net.thejadeproject.ascension.runic_path.Runes;
import net.thejadeproject.ascension.runic_path.RunicRealm;

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

    @Override public void onAdded(IEntityData attachedEntityData) {
        apply(attachedEntityData);
    }
    @Override public void onRemoved(IEntityData attachedEntityData, IPersistentSkillData persistentData) {
        remove(attachedEntityData);
    }

    private void apply(IEntityData entity) {
        var holder = entity.getAscensionAttributeHolder();
        if (holder == null) return;

        remove(entity);

        double armorBonus = getArmorBonus(entity);
        double toughnessBonus = getToughnessBonus(entity);

        Rune rune = getTemporaryFleshRune(entity);

        var armor = holder.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.addModifier(makeModifier(ARMOR_ID, armorBonus));
        }

        var toughness = holder.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (toughness != null) {
            toughness.addModifier(makeModifier(TOUGHNESS_ID, toughnessBonus));
        }

        holder.updateAttributes(entity);

        if (entity.getAttachedEntity() instanceof ServerPlayer player && rune != null) {
            player.displayClientMessage(
                    Component.literal("Flesh Rune: ")
                            .append(rune.getDisplayName())
                            .append(Component.literal(" | Armor: " + armorBonus + " | Toughness: " + toughnessBonus)),
                    true
            );
        }
    }


    private double getArmorBonus(IEntityData entity) {
        int realm = 0;
        if (entity.hasPath(ModPaths.RUNIC.getId())) {
            realm = entity.getPathData(ModPaths.RUNIC.getId()).getMajorRealm();
        }

        double baseArmor = 2.0 + realm * 1.0;

        Rune rune = getTemporaryFleshRune(entity);
        if (rune != null && rune.getRealm() == RunicRealm.FLESH) {
            if (rune.getEffectType() == RuneEffectType.ARMOR) {
                baseArmor += rune.getBaseValue() * (realm + 1);
            }
        }

        return baseArmor;
    }

    private double getToughnessBonus(IEntityData entity) {
        int realm = 0;
        if (entity.hasPath(ModPaths.RUNIC.getId())) {
            realm = entity.getPathData(ModPaths.RUNIC.getId()).getMajorRealm();
        }

        double baseToughness = 1.0 + realm * 0.5;

        Rune rune = getTemporaryFleshRune(entity);
        if (rune != null && rune.getRealm() == RunicRealm.FLESH) {
            if (rune.getEffectType() == RuneEffectType.ARMOR_TOUGHNESS) {
                baseToughness += rune.getBaseValue() * (realm + 1);
            }
        }

        return baseToughness;
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

    private Rune getTemporaryFleshRune(IEntityData entity) {
        if (entity == null || entity.getAttachedEntity() == null) return null;

        CompoundTag ascensionTag = entity.getAttachedEntity().getPersistentData().getCompound("ascension_runes");
        net.thejadeproject.ascension.runic_path.RunicRuneData runeData =
                net.thejadeproject.ascension.runic_path.RunicRuneData.read(ascensionTag);

        // TEMP: Use the first unlocked Flesh rune found.
        if (runeData.hasRune(Runes.ARMOR.getId())) return Runes.ARMOR;
        if (runeData.hasRune(Runes.ENDURANCE.getId())) return Runes.ENDURANCE;
        if (runeData.hasRune(Runes.VITALITY.getId())) return Runes.VITALITY;
        if (runeData.hasRune(Runes.STRENGTH.getId())) return Runes.STRENGTH;

        return null;
    }



}