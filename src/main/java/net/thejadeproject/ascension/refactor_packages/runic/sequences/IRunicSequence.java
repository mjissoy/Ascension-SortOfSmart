package net.thejadeproject.ascension.refactor_packages.runic.sequences;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface IRunicSequence {

    ResourceLocation getId();

    List<ResourceLocation> getRequiredRunes();

    RunicSequenceTier getTier();

    int getMinimumRunicRealm();

    int getQiCost();

    boolean isOrderSensitive();

    boolean canCast(LivingEntity caster);

    void cast(LivingEntity caster);
}