package net.thejadeproject.ascension.refactor_packages.runic.casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.refactor_packages.entity_data.IEntityData;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPathHelper;
import net.thejadeproject.ascension.refactor_packages.runic.RunicPlayerData;
import net.thejadeproject.ascension.refactor_packages.runic.sequences.IRunicSequence;
import net.thejadeproject.ascension.refactor_packages.runic.sequences.ModRunicSequences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class RunicSequenceMatcher {

    private RunicSequenceMatcher() {
    }

    public static RunicCastingResult tryCast(LivingEntity caster, List<ResourceLocation> inputRunes) {
        return tryCast(caster, inputRunes, ModRunicSequences.values());
    }

    public static RunicCastingResult tryCast(
            LivingEntity caster,
            List<ResourceLocation> inputRunes,
            Collection<IRunicSequence> availableSequences
    ) {
        if (caster == null) {
            return RunicCastingResult.failure("missing_caster");
        }

        if (inputRunes == null || inputRunes.isEmpty()) {
            return RunicCastingResult.failure("empty_sequence");
        }

        if (!caster.hasData(ModAttachments.ENTITY_DATA)) {
            return RunicCastingResult.failure("missing_entity_data");
        }

        IEntityData entityData = caster.getData(ModAttachments.ENTITY_DATA);

        if (!RunicPathHelper.hasEnteredRunicPath(entityData)) {
            return RunicCastingResult.failure("not_on_runic_path");
        }

        int slotCount = RunicPathHelper.getRuneSlotCount(entityData);

        if (inputRunes.size() > slotCount) {
            return RunicCastingResult.failure("too_many_runes");
        }

        RunicPlayerData runicData = RunicPathHelper.getRunicData(caster);

        for (ResourceLocation runeId : inputRunes) {
            if (!runicData.knowsRune(runeId)) {
                return RunicCastingResult.failure("unknown_rune:" + runeId);
            }

            if (!RunicPathHelper.canUseRune(entityData, runeId)) {
                return RunicCastingResult.failure("rune_locked:" + runeId);
            }
        }

        IRunicSequence sequence = findMatchingSequence(inputRunes, availableSequences);

        if (sequence == null) {
            return RunicCastingResult.failure("no_matching_sequence");
        }

        int majorRealm = RunicPathHelper.getRunicMajorRealm(entityData);

        if (majorRealm < sequence.getMinimumRunicRealm()) {
            return RunicCastingResult.failure("sequence_locked:" + sequence.getId());
        }

        if (!sequence.canCast(caster)) {
            return RunicCastingResult.failure("sequence_cannot_cast:" + sequence.getId());
        }

        // TODO: Consume qi here once the runic system is linked to EntityQiContainer.
        sequence.cast(caster);

        runicData.addDiscoveredSequence(sequence.getId());
        RunicPathHelper.saveRunicData(caster, runicData);

        return RunicCastingResult.success(sequence.getId());
    }

    public static IRunicSequence findMatchingSequence(
            List<ResourceLocation> inputRunes,
            Collection<IRunicSequence> availableSequences
    ) {
        if (inputRunes == null || availableSequences == null) {
            return null;
        }

        return availableSequences.stream()
                .filter(sequence -> matches(inputRunes, sequence))
                .max(Comparator.comparingInt(sequence -> sequence.getRequiredRunes().size()))
                .orElse(null);
    }

    public static boolean matches(List<ResourceLocation> inputRunes, IRunicSequence sequence) {
        List<ResourceLocation> requiredRunes = sequence.getRequiredRunes();

        if (inputRunes.size() != requiredRunes.size()) {
            return false;
        }

        if (sequence.isOrderSensitive()) {
            return inputRunes.equals(requiredRunes);
        }

        List<ResourceLocation> inputCopy = new ArrayList<>(inputRunes);
        List<ResourceLocation> requiredCopy = new ArrayList<>(requiredRunes);

        inputCopy.sort(Comparator.comparing(ResourceLocation::toString));
        requiredCopy.sort(Comparator.comparing(ResourceLocation::toString));

        return inputCopy.equals(requiredCopy);
    }
}