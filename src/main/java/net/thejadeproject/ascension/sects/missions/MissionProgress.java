package net.thejadeproject.ascension.sects.missions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.HashMap;
import java.util.Map;


public class MissionProgress {
    private Map<Integer, Integer> progress = new HashMap<>(); // requirement index -> current progress
    private boolean completed;

    public MissionProgress() {
        this.completed = false;
    }

    public int getProgress(int requirementIndex) {
        return progress.getOrDefault(requirementIndex, 0);
    }

    public void setProgress(Map<Integer, Integer> progress) {
        this.progress = new HashMap<>(progress);
    }

    public Map<Integer, Integer> getProgress() {
        return new HashMap<>(progress);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();

        // Save progress map
        ListTag progressList = new ListTag();
        for (Map.Entry<Integer, Integer> entry : progress.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putInt("index", entry.getKey());
            entryTag.putInt("progress", entry.getValue());
            progressList.add(entryTag);
        }
        tag.put("progress", progressList);

        tag.putBoolean("completed", completed);
        return tag;
    }

    public static MissionProgress fromNBT(CompoundTag tag) {
        MissionProgress missionProgress = new MissionProgress();

        // Load progress map
        if (tag.contains("progress")) {
            ListTag progressList = tag.getList("progress", 10);
            for (int i = 0; i < progressList.size(); i++) {
                CompoundTag entryTag = progressList.getCompound(i);
                int index = entryTag.getInt("index");
                int progressValue = entryTag.getInt("progress");
                missionProgress.progress.put(index, progressValue);
            }
        }

        missionProgress.completed = tag.getBoolean("completed");
        return missionProgress;
    }
}
