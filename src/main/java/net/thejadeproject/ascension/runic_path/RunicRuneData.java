package net.thejadeproject.ascension.runic_path;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class RunicRuneData {

    private final Set<ResourceLocation> unlockedRunes = new HashSet<>();
    private final Map<Integer, List<ResourceLocation>> selectedRunes = new HashMap<>();

    public boolean unlockRune(ResourceLocation runeId) {
        return unlockedRunes.add(runeId);
    }

    public boolean hasRune(ResourceLocation runeId) {
        return unlockedRunes.contains(runeId);
    }

    public Set<ResourceLocation> getUnlockedRunes() {
        return Collections.unmodifiableSet(unlockedRunes);
    }

    public List<ResourceLocation> getSelectedRunes(int majorRealm) {
        List<ResourceLocation> selected = selectedRunes.get(majorRealm);
        if (selected == null) return List.of();
        return Collections.unmodifiableList(selected);
    }

    public Map<Integer, List<ResourceLocation>> getSelectedRunes() {
        Map<Integer, List<ResourceLocation>> copy = new HashMap<>();
        for (Map.Entry<Integer, List<ResourceLocation>> entry : selectedRunes.entrySet()) {
            copy.put(entry.getKey(), Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }

    public int getSelectedRuneCount(int majorRealm) {
        return getSelectedRunes(majorRealm).size();
    }

    public boolean hasSelectedRune(int majorRealm, ResourceLocation runeId) {
        return getSelectedRunes(majorRealm).contains(runeId);
    }

    public boolean addSelectedRune(int majorRealm, ResourceLocation runeId) {
        if (runeId == null) return false;
        if (!unlockedRunes.contains(runeId)) return false;

        List<ResourceLocation> selected = selectedRunes.computeIfAbsent(majorRealm, k -> new ArrayList<>());
        if (selected.contains(runeId)) return false;

        selected.add(runeId);
        return true;
    }

    public boolean removeSelectedRune(int majorRealm, ResourceLocation runeId) {
        List<ResourceLocation> selected = selectedRunes.get(majorRealm);
        if (selected == null) return false;

        boolean removed = selected.remove(runeId);
        if (selected.isEmpty()) {
            selectedRunes.remove(majorRealm);
        }
        return removed;
    }

    public void clearSelectedRunes(int majorRealm) {
        selectedRunes.remove(majorRealm);
    }

    public void clearAllSelectedRunes() {
        selectedRunes.clear();
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();

        ListTag unlockedList = new ListTag();
        for (ResourceLocation runeId : unlockedRunes) {
            unlockedList.add(StringTag.valueOf(runeId.toString()));
        }
        tag.put("unlocked_runes", unlockedList);

        CompoundTag selectedTag = new CompoundTag();
        for (Map.Entry<Integer, List<ResourceLocation>> entry : selectedRunes.entrySet()) {
            ListTag realmList = new ListTag();
            for (ResourceLocation runeId : entry.getValue()) {
                realmList.add(StringTag.valueOf(runeId.toString()));
            }
            selectedTag.put(String.valueOf(entry.getKey()), realmList);
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

                Tag rawTag = selectedTag.get(key);
                if (rawTag instanceof ListTag realmList) {
                    for (int i = 0; i < realmList.size(); i++) {
                        ResourceLocation runeId = ResourceLocation.parse(realmList.getString(i));
                        data.addSelectedRune(majorRealm, runeId);
                    }
                }
                else if (rawTag instanceof StringTag) {
                    ResourceLocation runeId = ResourceLocation.parse(selectedTag.getString(key));
                    data.addSelectedRune(majorRealm, runeId);
                }
            } catch (Exception ignored) {
            }
        }

        return data;
    }
}