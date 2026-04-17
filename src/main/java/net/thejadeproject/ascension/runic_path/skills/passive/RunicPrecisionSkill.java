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

public class RunicPrecisionSkill extends AbstractRunicPassiveSkill {

    private static final ResourceLocation PRECISION_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_precision");

    public RunicPrecisionSkill(Component title) {
        super(title);
        this.icon = new TextureData(
                ResourceLocation.fromNamespaceAndPath(
                        AscensionCraft.MOD_ID,
                        "textures/spells/icon/runic_precision.png"
                ),
                16, 16
        );
    }

    @Override
    protected void apply(IEntityData entity) {
        if (entity == null || entity.getAscensionAttributeHolder() == null) return;

        remove(entity);

        double bonus = getAttackSpeedBonus(entity);

        var attr = entity.getAscensionAttributeHolder().getAttribute(Attributes.ATTACK_SPEED);
        if (attr != null) {
            attr.addModifier(makeModifier(PRECISION_ID, bonus));
        }

        entity.getAscensionAttributeHolder().updateAttributes(entity);
    }

    @Override
    protected void remove(IEntityData entity) {
        if (entity == null || entity.getAscensionAttributeHolder() == null) return;

        var attr = entity.getAscensionAttributeHolder().getAttribute(Attributes.ATTACK_SPEED);
        if (attr != null) {
            attr.removeModifier(PRECISION_ID);
        }

        entity.getAscensionAttributeHolder().updateAttributes(entity);
    }

    private double getAttackSpeedBonus(IEntityData entity) {
        return RunicScalingHelper.scaleFlat(0.05, 0.025, entity);
    }

    private ValueContainerModifier makeModifier(ResourceLocation id, double value) {
        return new ValueContainerModifier(value, ModifierOperation.ADD_BASE, id);
    }
}