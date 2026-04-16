package net.thejadeproject.ascension.runic_path;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class RunicRuneData {

    private final Set<ResourceLocation> unlockedRunes = new HashSet<>();
    private final Map<Integer, ResourceLocation> selectedRunes = new HashMap<>();

    public boolean unlockRune(ResourceLocation runeId) {
        return unlockedRunes.add(runeId);
    }

    public boolean hasRune(ResourceLocation runeId) {
        return unlockedRunes.contains(runeId);
    }

    public Set<ResourceLocation> getUnlockedRunes() {
        return Collections.unmodifiableSet(unlockedRunes);
    }

    public ResourceLocation getSelectedRune(int majorRealm) {
        return selectedRunes.get(majorRealm);
    }

    public Map<Integer, ResourceLocation> getSelectedRunes() {
        return Collections.unmodifiableMap(selectedRunes);
    }

    public boolean setSelectedRune(int majorRealm, ResourceLocation runeId) {
        if (runeId != null && !unlockedRunes.contains(runeId)) {
            return false;
        }

        if (runeId == null) {
            selectedRunes.remove(majorRealm);
        } else {
            selectedRunes.put(majorRealm, runeId);
        }
        return true;
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();

        ListTag unlockedList = new ListTag();
        for (ResourceLocation runeId : unlockedRunes) {
            unlockedList.add(StringTag.valueOf(runeId.toString()));
        }
        tag.put("unlocked_runes", unlockedList);

        CompoundTag selectedTag = new CompoundTag();
        for (Map.Entry<Integer, ResourceLocation> entry : selectedRunes.entrySet()) {
            selectedTag.putString(String.valueOf(entry.getKey()), entry.getValue().toString());
        }
        tag.put("selected_runes", selectedTag);

        return tag;
    }

    public static RunicRuneData read(CompoundTag tag) {
        RunicRuneData data = new RunicRuneData();

        ListTag unlockedList = tag.getList("unlocked_runes", Tag.TAG_STRING);
        for (int i = 0; i < unlockedList.size(); i++) {
            data.unlockRune(ResourceLocation.parse(unlockedList.getString(i)));
        }

        CompoundTag selectedTag = tag.getCompound("selected_runes");
        for (String key : selectedTag.getAllKeys()) {
            try {
                int majorRealm = Integer.parseInt(key);
                ResourceLocation runeId = ResourceLocation.parse(selectedTag.getString(key));
                data.setSelectedRune(majorRealm, runeId);
            } catch (Exception ignored) {
            }
        }

        return data;
    }
}