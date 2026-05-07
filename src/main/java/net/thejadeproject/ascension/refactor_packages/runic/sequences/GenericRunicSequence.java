package net.thejadeproject.ascension.refactor_packages.runic.sequences;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class GenericRunicSequence implements IRunicSequence {

    private final ResourceLocation id;
    private final List<ResourceLocation> requiredRunes;
    private final RunicSequenceTier tier;
    private final int minimumRunicRealm;
    private final int qiCost;
    private final boolean orderSensitive;

    public GenericRunicSequence(
            ResourceLocation id,
            List<ResourceLocation> requiredRunes,
            RunicSequenceTier tier,
            int minimumRunicRealm,
            int qiCost,
            boolean orderSensitive
    ) {
        this.id = id;
        this.requiredRunes = List.copyOf(requiredRunes);
        this.tier = tier;
        this.minimumRunicRealm = minimumRunicRealm;
        this.qiCost = qiCost;
        this.orderSensitive = orderSensitive;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public List<ResourceLocation> getRequiredRunes() {
        return requiredRunes;
    }

    @Override
    public RunicSequenceTier getTier() {
        return tier;
    }

    @Override
    public int getMinimumRunicRealm() {
        return minimumRunicRealm;
    }

    @Override
    public int getQiCost() {
        return qiCost;
    }

    @Override
    public boolean isOrderSensitive() {
        return orderSensitive;
    }

    @Override
    public boolean canCast(LivingEntity caster) {
        return true;
    }

    @Override
    public void cast(LivingEntity caster) {
        if (caster instanceof ServerPlayer player) {
            player.sendSystemMessage(Component.translatable("ascension.runic.sequence.cast", id.toString()));
        }
    }
}