package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;
import net.thejadeproject.ascension.runic_path.RunicScalingHelper;

public class RunicArmorSkill extends AbstractRunicPassiveSkill {

    private static final ResourceLocation ARMOR_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_armor_armor");

    private static final ResourceLocation TOUGHNESS_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_armor_toughness");

    public RunicArmorSkill(Component title) {
        super(title);
        this.icon = new TextureData(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/icon/runic_armor.png"),
                16, 16
        );
    }

    @Override
    public RunicArmorSkill setShortDescription(Component shortDescription) {
        super.setShortDescription(shortDescription);
        return this;
    }

    @Override
    public RunicArmorSkill setDescription(Component description) {
        super.setDescription(description);
        return this;
    }

    @Override
    protected void apply(IEntityData entity) {
        if (entity == null || entity.getAscensionAttributeHolder() == null) return;

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

    @Override
    protected void remove(IEntityData entity) {
        if (entity == null || entity.getAscensionAttributeHolder() == null) return;

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
        return new ValueContainerModifier(value, ModifierOperation.ADD_BASE, id);
    }
}