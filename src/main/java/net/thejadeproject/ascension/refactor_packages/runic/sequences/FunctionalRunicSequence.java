package net.thejadeproject.ascension.refactor_packages.runic.sequences;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.function.Consumer;

public class FunctionalRunicSequence extends GenericRunicSequence {

    private final Consumer<LivingEntity> castAction;

    public FunctionalRunicSequence(
            ResourceLocation id,
            List<ResourceLocation> requiredRunes,
            RunicSequenceTier tier,
            int minimumRunicRealm,
            int qiCost,
            boolean orderSensitive,
            Consumer<LivingEntity> castAction
    ) {
        super(id, requiredRunes, tier, minimumRunicRealm, qiCost, orderSensitive);
        this.castAction = castAction;
    }

    @Override
    public void cast(LivingEntity caster) {
        if (castAction != null) {
            castAction.accept(caster);
        }
    }
}