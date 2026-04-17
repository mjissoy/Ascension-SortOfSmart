package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;
import net.thejadeproject.ascension.runic_path.RunicScalingHelper;

public class RunicVitalitySkill extends AbstractRunicPassiveSkill {

    private static final ResourceLocation VITALITY_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_vitality_stat");

    public RunicVitalitySkill(Component title) {
        super(title);
        this.icon = new TextureData(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/icon/runic_vitality.png"),
                16, 16
        );
    }

    @Override
    public RunicVitalitySkill setShortDescription(Component shortDescription) {
        super.setShortDescription(shortDescription);
        return this;
    }

    @Override
    public RunicVitalitySkill setDescription(Component description) {
        super.setDescription(description);
        return this;
    }

    @Override
    protected void apply(IEntityData entity) {
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

    @Override
    protected void remove(IEntityData entity) {
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
        return new ValueContainerModifier(value, ModifierOperation.ADD_BASE, id);
    }
}