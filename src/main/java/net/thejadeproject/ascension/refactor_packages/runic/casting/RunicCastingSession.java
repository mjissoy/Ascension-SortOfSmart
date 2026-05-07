package net.thejadeproject.ascension.refactor_packages.runic.casting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class RunicCastingSession {

    private final LivingEntity caster;
    private final int maxRuneSlots;
    private final int durationTicks;
    private final List<ResourceLocation> selectedRunes = new ArrayList<>();

    private int ageTicks;

    public RunicCastingSession(LivingEntity caster, int maxRuneSlots, int durationTicks) {
        this.caster = caster;
        this.maxRuneSlots = maxRuneSlots;
        this.durationTicks = durationTicks;
    }

    public LivingEntity getCaster() {
        return caster;
    }

    public int getMaxRuneSlots() {
        return maxRuneSlots;
    }

    public int getDurationTicks() {
        return durationTicks;
    }

    public int getAgeTicks() {
        return ageTicks;
    }

    public List<ResourceLocation> getSelectedRunes() {
        return selectedRunes;
    }

    public boolean addRune(ResourceLocation runeId) {
        if (selectedRunes.size() >= maxRuneSlots) {
            return false;
        }

        selectedRunes.add(runeId);
        return true;
    }

    public void removeLastRune() {
        if (!selectedRunes.isEmpty()) {
            selectedRunes.remove(selectedRunes.size() - 1);
        }
    }

    public void tick() {
        ageTicks++;
    }

    public boolean isExpired() {
        return ageTicks >= durationTicks;
    }

    public RunicCastingResult tryCast() {
        return RunicSequenceMatcher.tryCast(caster, selectedRunes);
    }
}