package net.thejadeproject.ascension.refactor_packages.runic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RunicPlayerData {

    private static final String KEY_KNOWN_RUNES = "known_runes";
    private static final String KEY_OBSERVED_RUNES = "observed_runes";
    private static final String KEY_GLIMPSED_RUNES = "glimpsed_runes";
    private static final String KEY_OBSERVATION_PROGRESS = "observation_progress";
    private static final String KEY_DISCOVERED_SEQUENCES = "discovered_sequences";

    private final Set<ResourceLocation> knownRunes = new HashSet<>();
    private final Set<ResourceLocation> observedRunes = new HashSet<>();
    private final Set<ResourceLocation> glimpsedRunes = new HashSet<>();

    private final Map<ResourceLocation, Float> observationProgress = new HashMap<>();
    private final Set<ResourceLocation> discoveredSequences = new HashSet<>();

    public Set<ResourceLocation> getKnownRunes() {
        return knownRunes;
    }

    public Set<ResourceLocation> getObservedRunes() {
        return observedRunes;
    }

    public Set<ResourceLocation> getGlimpsedRunes() {
        return glimpsedRunes;
    }

    public Map<ResourceLocation, Float> getObservationProgress() {
        return observationProgress;
    }

    public Set<ResourceLocation> getDiscoveredSequences() {
        return discoveredSequences;
    }

    public boolean knowsRune(ResourceLocation runeId) {
        return knownRunes.contains(runeId);
    }

    public boolean hasDiscoveredSequence(ResourceLocation sequenceId) {
        return discoveredSequences.contains(sequenceId);
    }

    public RunicLearningState getLearningState(ResourceLocation runeId) {
        if (knownRunes.contains(runeId)) {
            return RunicLearningState.KNOWN;
        }

        if (observedRunes.contains(runeId)) {
            return RunicLearningState.OBSERVED;
        }

        if (glimpsedRunes.contains(runeId)) {
            return RunicLearningState.GLIMPSED;
        }

        return RunicLearningState.UNSEEN;
    }

    public void addKnownRune(ResourceLocation runeId) {
        knownRunes.add(runeId);
        observedRunes.remove(runeId);
        glimpsedRunes.remove(runeId);
        observationProgress.remove(runeId);
    }

    public void addObservedRune(ResourceLocation runeId) {
        if (!knownRunes.contains(runeId)) {
            observedRunes.add(runeId);
            glimpsedRunes.remove(runeId);
        }
    }

    public void addGlimpsedRune(ResourceLocation runeId) {
        if (!knownRunes.contains(runeId) && !observedRunes.contains(runeId)) {
            glimpsedRunes.add(runeId);
        }
    }

    public void setObservationProgress(ResourceLocation runeId, float progress) {
        if (knownRunes.contains(runeId)) {
            observationProgress.remove(runeId);
            return;
        }

        observationProgress.put(runeId, Math.max(0.0F, Math.min(1.0F, progress)));
    }

    public void addDiscoveredSequence(ResourceLocation sequenceId) {
        discoveredSequences.add(sequenceId);
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        tag.put(KEY_KNOWN_RUNES, writeIdSet(knownRunes));
        tag.put(KEY_OBSERVED_RUNES, writeIdSet(observedRunes));
        tag.put(KEY_GLIMPSED_RUNES, writeIdSet(glimpsedRunes));
        tag.put(KEY_DISCOVERED_SEQUENCES, writeIdSet(discoveredSequences));

        CompoundTag progressTag = new CompoundTag();
        observationProgress.forEach((id, progress) -> progressTag.putFloat(id.toString(), progress));
        tag.put(KEY_OBSERVATION_PROGRESS, progressTag);

        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        knownRunes.clear();
        observedRunes.clear();
        glimpsedRunes.clear();
        observationProgress.clear();
        discoveredSequences.clear();

        readIdSet(tag, KEY_KNOWN_RUNES, knownRunes);
        readIdSet(tag, KEY_OBSERVED_RUNES, observedRunes);
        readIdSet(tag, KEY_GLIMPSED_RUNES, glimpsedRunes);
        readIdSet(tag, KEY_DISCOVERED_SEQUENCES, discoveredSequences);

        CompoundTag progressTag = tag.getCompound(KEY_OBSERVATION_PROGRESS);

        for (String key : progressTag.getAllKeys()) {
            ResourceLocation runeId = ResourceLocation.tryParse(key);

            if (runeId != null) {
                observationProgress.put(runeId, progressTag.getFloat(key));
            }
        }
    }

    private static ListTag writeIdSet(Set<ResourceLocation> ids) {
        ListTag list = new ListTag();

        ids.stream()
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .forEach(id -> list.add(StringTag.valueOf(id.toString())));

        return list;
    }

    private static void readIdSet(CompoundTag tag, String key, Set<ResourceLocation> output) {
        ListTag list = tag.getList(key, net.minecraft.nbt.Tag.TAG_STRING);

        for (int i = 0; i < list.size(); i++) {
            ResourceLocation id = ResourceLocation.tryParse(list.getString(i));

            if (id != null) {
                output.add(id);
            }
        }
    }
}