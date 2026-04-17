package net.thejadeproject.ascension.runic_path.network;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public final class ClientRunicData {

    private static final Set<ResourceLocation> UNLOCKED_RUNES = new LinkedHashSet<>();
    private static final Map<Integer, List<ResourceLocation>> SELECTED_RUNES = new HashMap<>();

    private ClientRunicData() {}

    public static void setUnlockedRunes(Set<ResourceLocation> runes) {
        UNLOCKED_RUNES.clear();
        UNLOCKED_RUNES.addAll(runes);
    }

    public static Set<ResourceLocation> getUnlockedRunes() {
        return Collections.unmodifiableSet(UNLOCKED_RUNES);
    }

    public static void setSelectedRunes(Map<Integer, List<ResourceLocation>> selectedRunes) {
        SELECTED_RUNES.clear();
        for (Map.Entry<Integer, List<ResourceLocation>> entry : selectedRunes.entrySet()) {
            SELECTED_RUNES.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
    }

    public static List<ResourceLocation> getSelectedRunes(int majorRealm) {
        List<ResourceLocation> selected = SELECTED_RUNES.get(majorRealm);
        if (selected == null) return List.of();
        return Collections.unmodifiableList(selected);
    }

    public static void clear() {
        UNLOCKED_RUNES.clear();
        SELECTED_RUNES.clear();
    }
}