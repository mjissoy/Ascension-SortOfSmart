package net.thejadeproject.ascension.refactor_packages.runic.sequences;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.minecraft.world.entity.LivingEntity;

public final class ModRunicSequences {

    private static final Map<ResourceLocation, IRunicSequence> BY_ID = new LinkedHashMap<>();

    public static final IRunicSequence EMBER_MARK = register(
            "ember_mark",
            RunicSequenceTier.BASIC,
            0,
            8,
            false,
            RunicSequenceEffects::emberMark,
            ModRunicRunes.FLAME,
            ModRunicRunes.MARK
    );

    public static final IRunicSequence CLEAR_WATER_MEND = register(
            "clear_water_mend",
            RunicSequenceTier.BASIC,
            0,
            10,
            false,
            RunicSequenceEffects::clearWaterMend,
            ModRunicRunes.WATER,
            ModRunicRunes.HEAL
    );

    public static final IRunicSequence STONE_WARD = register(
            "stone_ward",
            RunicSequenceTier.BASIC,
            0,
            12,
            false,
            RunicSequenceEffects::stoneWard,
            ModRunicRunes.EARTH,
            ModRunicRunes.GUARD
    );

    public static final IRunicSequence WIND_PUSH = register(
            "wind_push",
            RunicSequenceTier.BASIC,
            0,
            8,
            false,
            RunicSequenceEffects::windPush,
            ModRunicRunes.WIND,
            ModRunicRunes.PUSH
    );

    public static final IRunicSequence FROST_BIND = register(
            "frost_bind",
            RunicSequenceTier.BASIC,
            1,
            15,
            false,
            RunicSequenceEffects::frostBind,
            ModRunicRunes.FROST,
            ModRunicRunes.BIND
    );

    public static final IRunicSequence WIND_STEP = register(
            "wind_step",
            RunicSequenceTier.INTERMEDIATE,
            1,
            18,
            false,
            RunicSequenceEffects::windStep,
            ModRunicRunes.WIND,
            ModRunicRunes.PUSH,
            ModRunicRunes.QUICKEN
    );

    public static final IRunicSequence THUNDER_CUT_BOLT = register(
            "thunder_cut_bolt",
            RunicSequenceTier.INTERMEDIATE,
            2,
            25,
            true,
            RunicSequenceEffects::thunderCutBolt,
            ModRunicRunes.LIGHTNING,
            ModRunicRunes.CUT,
            ModRunicRunes.BOLT
    );

    private ModRunicSequences() {
    }

    public static IRunicSequence get(ResourceLocation id) {
        return BY_ID.get(id);
    }

    public static Collection<IRunicSequence> values() {
        return List.copyOf(BY_ID.values());
    }

    private static IRunicSequence register(
            String path,
            RunicSequenceTier tier,
            int minimumRunicRealm,
            int qiCost,
            boolean orderSensitive,
            Consumer<LivingEntity> castAction,
            ResourceLocation... requiredRunes
    ) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, path);

        IRunicSequence sequence = new FunctionalRunicSequence(
                id,
                List.of(requiredRunes),
                tier,
                minimumRunicRealm,
                qiCost,
                orderSensitive,
                castAction
        );

        BY_ID.put(id, sequence);
        return sequence;
    }
}