package net.thejadeproject.ascension.refactor_packages.runic.sequences;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.registries.AscensionRegistries;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class ModRunicSequences {

    public static final DeferredRegister<IRunicSequence> SEQUENCES = DeferredRegister.create(
            AscensionRegistries.RunicSequences.RUNIC_SEQUENCES_REGISTRY,
            AscensionCraft.MOD_ID
    );

    private static final Map<ResourceLocation, DeferredHolder<IRunicSequence, ? extends IRunicSequence>> BY_ID = new LinkedHashMap<>();

    public static final DeferredHolder<IRunicSequence, ? extends FunctionalRunicSequence> EMBER_MARK = register(
            "ember_mark",
            RunicSequenceTier.BASIC,
            0,
            8,
            false,
            RunicSequenceEffects::emberMark,
            ModRunicRunes.FLAME,
            ModRunicRunes.MARK
    );

    public static final DeferredHolder<IRunicSequence, ? extends FunctionalRunicSequence> CLEAR_WATER_MEND = register(
            "clear_water_mend",
            RunicSequenceTier.BASIC,
            0,
            10,
            false,
            RunicSequenceEffects::clearWaterMend,
            ModRunicRunes.WATER,
            ModRunicRunes.HEAL
    );

    public static final DeferredHolder<IRunicSequence, ? extends FunctionalRunicSequence> STONE_WARD = register(
            "stone_ward",
            RunicSequenceTier.BASIC,
            0,
            12,
            false,
            RunicSequenceEffects::stoneWard,
            ModRunicRunes.EARTH,
            ModRunicRunes.GUARD
    );

    public static final DeferredHolder<IRunicSequence, ? extends FunctionalRunicSequence> WIND_PUSH = register(
            "wind_push",
            RunicSequenceTier.BASIC,
            0,
            8,
            false,
            RunicSequenceEffects::windPush,
            ModRunicRunes.WIND,
            ModRunicRunes.PUSH
    );

    public static final DeferredHolder<IRunicSequence, ? extends FunctionalRunicSequence> FROST_BIND = register(
            "frost_bind",
            RunicSequenceTier.BASIC,
            1,
            15,
            false,
            RunicSequenceEffects::frostBind,
            ModRunicRunes.FROST,
            ModRunicRunes.BIND
    );

    public static final DeferredHolder<IRunicSequence, ? extends FunctionalRunicSequence> WIND_STEP = register(
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

    public static final DeferredHolder<IRunicSequence, ? extends FunctionalRunicSequence> THUNDER_CUT_BOLT = register(
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

    public static void register(IEventBus modEventBus) {
        SEQUENCES.register(modEventBus);
    }

    public static IRunicSequence get(ResourceLocation id) {
        DeferredHolder<IRunicSequence, ? extends IRunicSequence> holder = BY_ID.get(id);
        if (holder != null) {
            return holder.get();
        }

        return AscensionRegistries.getRegistryObject(
                id,
                AscensionRegistries.RunicSequences.RUNIC_SEQUENCES_REGISTRY
        );
    }

    public static Collection<IRunicSequence> values() {
        return BY_ID.values().stream()
                .map(holder -> (IRunicSequence) holder.get())
                .toList();
    }

    public static List<ResourceLocation> allSequenceIds() {
        return List.copyOf(BY_ID.keySet());
    }

    private static DeferredHolder<IRunicSequence, FunctionalRunicSequence> register(
            String path,
            RunicSequenceTier tier,
            int minimumRunicRealm,
            int qiCost,
            boolean orderSensitive,
            Consumer<LivingEntity> castAction,
            ResourceLocation... requiredRunes
    ) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, path);

        DeferredHolder<IRunicSequence, FunctionalRunicSequence> holder = SEQUENCES.register(path, () -> new FunctionalRunicSequence(
                id,
                List.of(requiredRunes),
                tier,
                minimumRunicRealm,
                qiCost,
                orderSensitive,
                castAction
        ));

        BY_ID.put(id, holder);
        return holder;
    }
}