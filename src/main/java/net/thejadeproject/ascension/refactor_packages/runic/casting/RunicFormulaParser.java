package net.thejadeproject.ascension.refactor_packages.runic.casting;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.refactor_packages.runic.runes.IRunicRune;
import net.thejadeproject.ascension.refactor_packages.runic.runes.ModRunicRunes;
import net.thejadeproject.ascension.refactor_packages.runic.runes.RunicRuneType;

import java.util.ArrayList;
import java.util.List;

public final class RunicFormulaParser {

    private RunicFormulaParser() {
    }

    public static RunicFormula parse(List<ResourceLocation> inputRunes) {
        IRunicRune source = null;
        IRunicRune intent = null;
        IRunicRune form = null;
        List<IRunicRune> modifiers = new ArrayList<>();

        for (ResourceLocation runeId : inputRunes) {
            IRunicRune rune = ModRunicRunes.get(runeId);

            if (rune == null) {
                continue;
            }

            if (rune.getType() == RunicRuneType.SOURCE && source == null) {
                source = rune;
            } else if (rune.getType() == RunicRuneType.INTENT && intent == null) {
                intent = rune;
            } else if (rune.getType() == RunicRuneType.FORM && form == null) {
                form = rune;
            } else if (rune.getType() == RunicRuneType.MODIFIER) {
                modifiers.add(rune);
            }
        }

        return new RunicFormula(source, intent, form, modifiers, List.copyOf(inputRunes));
    }
}