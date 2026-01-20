package net.thejadeproject.ascension.sects.missions;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.sects.SectRank;

import java.util.*;

public class SectMission {
    private final UUID missionId;
    private String displayName;
    private SectRank targetRank;
    private final List<MissionRequirement> requirements;
    private int rewardMerit;
    private final UUID createdBy;
    private final long createdTime;
    private long expirationTime = 0;
    final Map<UUID, MissionProgress> acceptedBy = new HashMap<>();
    private ItemStack rewardItem;

    private boolean repeatable = false;
    public boolean isRepeatable() { return repeatable; }
    public void setRepeatable(boolean repeatable) { this.repeatable = repeatable; }


    public SectMission(String displayName, SectRank targetRank, List<MissionRequirement> requirements,
                       int rewardMerit, UUID createdBy) {
        this.missionId = UUID.randomUUID();
        this.displayName = displayName;
        this.targetRank = targetRank;
        this.requirements = requirements;
        this.rewardMerit = rewardMerit;
        this.createdBy = createdBy;
        this.createdTime = System.currentTimeMillis();
        this.rewardItem = ItemStack.EMPTY; // Initialize as empty
    }

    // Getters
    public ItemStack getRewardItem() {
        return rewardItem;
    }

    public void setRewardItem(ItemStack rewardItem) {
        this.rewardItem = rewardItem;
    }

    public UUID getMissionId() {
        return missionId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SectRank getTargetRank() {
        return targetRank;
    }

    public List<MissionRequirement> getRequirements() {
        return requirements;
    }

    public int getRewardMerit() {
        return rewardMerit;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean isExpired() {
        return expirationTime > 0 && System.currentTimeMillis() > expirationTime;
    }

    public String getTimeRemaining() {
        if (expirationTime <= 0) return "§aNo time limit";

        long remaining = expirationTime - System.currentTimeMillis();
        if (remaining <= 0) return "§cExpired";

        long days = remaining / (1000 * 60 * 60 * 24);
        long hours = (remaining % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (remaining % (1000 * 60 * 60)) / (1000 * 60);

        if (days > 0) {
            return String.format("§e%d day%s, %d hour%s", days, days == 1 ? "" : "s", hours, hours == 1 ? "" : "s");
        } else if (hours > 0) {
            return String.format("§e%d hour%s, %d minute%s", hours, hours == 1 ? "" : "s", minutes, minutes == 1 ? "" : "s");
        } else {
            return String.format("§e%d minute%s", minutes, minutes == 1 ? "" : "s");
        }
    }

    // Mission progress methods
    public boolean isAcceptedBy(UUID playerId) {
        return acceptedBy.containsKey(playerId);
    }

    public MissionProgress getProgress(UUID playerId) {
        return acceptedBy.get(playerId);
    }

    public void acceptMission(UUID playerId) {
        if (!acceptedBy.containsKey(playerId)) {
            acceptedBy.put(playerId, new MissionProgress());
        }
    }

    public void completeMission(UUID playerId) {
        acceptedBy.remove(playerId);
    }

    public boolean canAccept(UUID playerId) {
        if (isExpired()) return false;
        return !acceptedBy.containsKey(playerId);
    }

    public CompoundTag toNBT(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("missionId", missionId);
        tag.putString("displayName", displayName);
        tag.putString("targetRank", targetRank.name());
        tag.putInt("rewardMerit", rewardMerit);
        tag.putUUID("createdBy", createdBy);
        tag.putLong("createdTime", createdTime);
        tag.putBoolean("repeatable", repeatable);

        if (expirationTime > 0) {
            tag.putLong("expirationTime", expirationTime);
        }

        ListTag requirementsList = new ListTag();
        for (MissionRequirement req : requirements) {
            requirementsList.add(req.toNBT());
        }
        tag.put("requirements", requirementsList);

        ListTag acceptedList = new ListTag();
        for (Map.Entry<UUID, MissionProgress> entry : acceptedBy.entrySet()) {
            CompoundTag progressTag = new CompoundTag();
            progressTag.putUUID("playerId", entry.getKey());
            progressTag.put("progress", entry.getValue().toNBT());
            acceptedList.add(progressTag);
        }
        tag.put("acceptedBy", acceptedList);

        if (!rewardItem.isEmpty()) {
            CompoundTag itemTag = new CompoundTag();
            rewardItem.save(registries, itemTag);
            tag.put("rewardItem", itemTag);
        }

        return tag;
    }

    public static SectMission fromNBT(CompoundTag tag, HolderLookup.Provider registries) {
        String displayName = tag.getString("displayName");
        SectRank targetRank = SectRank.valueOf(tag.getString("targetRank"));

        List<MissionRequirement> requirements = new ArrayList<>();
        ListTag requirementsList = tag.getList("requirements", 10);
        for (int i = 0; i < requirementsList.size(); i++) {
            requirements.add(MissionRequirement.fromNBT(requirementsList.getCompound(i)));
        }

        int rewardMerit = tag.getInt("rewardMerit");
        UUID createdBy = tag.getUUID("createdBy");

        SectMission mission = new SectMission(displayName, targetRank, requirements, rewardMerit, createdBy);

        if (tag.contains("expirationTime")) {
            mission.expirationTime = tag.getLong("expirationTime");
        }

        ListTag acceptedList = tag.getList("acceptedBy", 10);
        for (int i = 0; i < acceptedList.size(); i++) {
            CompoundTag progressTag = acceptedList.getCompound(i);
            UUID playerId = progressTag.getUUID("playerId");
            MissionProgress progress = MissionProgress.fromNBT(progressTag.getCompound("progress"));
            mission.acceptedBy.put(playerId, progress);
        }

        if (tag.contains("rewardItem")) {
            CompoundTag itemTag = tag.getCompound("rewardItem");
            mission.rewardItem = ItemStack.parse(registries, itemTag).orElse(ItemStack.EMPTY);
        }

        if (tag.contains("repeatable")) mission.repeatable = tag.getBoolean("repeatable");

        return mission;
    }
}