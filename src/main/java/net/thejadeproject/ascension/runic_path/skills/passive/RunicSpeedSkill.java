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

public class RunicSpeedSkill extends AbstractRunicPassiveSkill {

    private static final ResourceLocation SPEED_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_speed");

    public RunicSpeedSkill(Component title) {
        super(title);
        this.icon = new TextureData(
                ResourceLocation.fromNamespaceAndPath(
                        AscensionCraft.MOD_ID,
                        "textures/spells/icon/runic_speed.png"
                ),
                16, 16
        );
    }

    @Override
    protected void apply(IEntityData entity) {
        if (entity == null || entity.getAscensionAttributeHolder() == null) return;

        remove(entity);

        double bonus = getSpeedBonus(entity);

        var attr = entity.getAscensionAttributeHolder().getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null) {
            attr.addModifier(makeModifier(SPEED_ID, bonus));
        }

        entity.getAscensionAttributeHolder().updateAttributes(entity);
    }

    @Override
    protected void remove(IEntityData entity) {
        if (entity == null || entity.getAscensionAttributeHolder() == null) return;

        var attr = entity.getAscensionAttributeHolder().getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null) {
            attr.removeModifier(SPEED_ID);
        }

        entity.getAscensionAttributeHolder().updateAttributes(entity);
    }

    private double getSpeedBonus(IEntityData entity) {
        return RunicScalingHelper.scaleFlat(0.015, 0.0075, entity);
    }

    private ValueContainerModifier makeModifier(ResourceLocation id, double value) {
        return new ValueContainerModifier(value, ModifierOperation.MULTIPLY_BASE, id);
    }
}