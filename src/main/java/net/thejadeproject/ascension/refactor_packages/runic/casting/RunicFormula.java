package net.thejadeproject.ascension.refactor_packages.runic.casting;

import net.minecraft.resources.ResourceLocation;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.refactor_packages.runic.runes.IRunicRune;

import java.util.ArrayList;
import java.util.List;

public record RunicFormula(
        IRunicRune source,
        IRunicRune intent,
        IRunicRune form,
        List<IRunicRune> modifiers,
        List<ResourceLocation> inputRunes
) {
    public boolean isValid() {
        return source != null && intent != null;
    }

    public String sourcePath() {
        return source == null ? "" : source.getId().getPath();
    }

    public String intentPath() {
        return intent == null ? "" : intent.getId().getPath();
    }

    public String formPath() {
        return form == null ? "bolt" : form.getId().getPath();
    }

    public boolean hasModifier(String path) {
        for (IRunicRune modifier : modifiers) {
            if (modifier.getId().getPath().equals(path)) {
                return true;
            }
        }

        return false;
    }

    public ResourceLocation getFormulaId() {
        List<String> parts = new ArrayList<>();

        for (ResourceLocation runeId : inputRunes) {
            parts.add(runeId.getPath());
        }

        return ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,
                "formula/" + String.join("_", parts)
        );
    }
}