package net.thejadeproject.ascension.runic_path;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class ClientRunicData {

    private static final Set<ResourceLocation> UNLOCKED_RUNES = new LinkedHashSet<>();

    private ClientRunicData() {}

    public static void setUnlockedRunes(Set<ResourceLocation> runes) {
        UNLOCKED_RUNES.clear();
        UNLOCKED_RUNES.addAll(runes);
    }

    public static Set<ResourceLocation> getUnlockedRunes() {
        return Collections.unmodifiableSet(UNLOCKED_RUNES);
    }

    public static void clear() {
        UNLOCKED_RUNES.clear();
    }
}