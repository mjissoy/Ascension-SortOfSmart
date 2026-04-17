package net.thejadeproject.ascension.runic_path.skills.passive;

import net.lucent.easygui.gui.textures.TextureData;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.stats.custom.ModStats;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ModifierOperation;
import net.thejadeproject.ascension.refactor_packages.util.value_modifiers.ValueContainerModifier;
import net.thejadeproject.ascension.runic_path.RunicScalingHelper;

public class RunicStrengthSkill extends AbstractRunicPassiveSkill {

    private static final ResourceLocation STRENGTH_ID =
            ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "runic_strength_strength");

    public RunicStrengthSkill(Component title) {
        super(title);
        this.icon = new TextureData(
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/spells/icon/runic_strength.png"),
                16,
                16
        );
    }

    @Override
    public RunicStrengthSkill setShortDescription(Component shortDescription) {
        super.setShortDescription(shortDescription);
        return this;
    }

    @Override
    public RunicStrengthSkill setDescription(Component description) {
        super.setDescription(description);
        return this;
    }

    @Override
    protected void apply(IEntityData entity) {
        if (entity == null || entity.getActiveFormData() == null) return;
        if (entity.getActiveFormData().getStatSheet() == null) return;
        if (entity.getActiveFormData().getStatSheet().getStatInstance(ModStats.STRENGTH.get()) == null) return;

        remove(entity);

        double strengthBonus = getStrengthBonus(entity);

        entity.getActiveFormData()
                .getStatSheet()
                .getStatInstance(ModStats.STRENGTH.get())
                .addModifier(makeModifier(STRENGTH_ID, strengthBonus));

        if (entity.getAttachedEntity() instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
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

    private double getStrengthBonus(IEntityData entity) {
        int realm = RunicScalingHelper.getMajorRealm(entity);
        return 2.0 * (realm + 1);
    }

    private ValueContainerModifier makeModifier(ResourceLocation id, double value) {
        return new ValueContainerModifier(
                value,
                ModifierOperation.ADD_BASE,
                id
        );
    }
}