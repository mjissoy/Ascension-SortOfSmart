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
    private SectRank targetRank; // Which rank can see/accept this mission
    private final List<MissionRequirement> requirements;
    private ItemStack rewardItem;
    private int rewardMerit;
    private boolean repeatable;
    private final UUID createdBy;
    private final long createdTime;

    // Track accepted missions
    final Map<UUID, MissionProgress> acceptedBy = new HashMap<>(); // PlayerId -> Progress

    public SectMission(String displayName, SectRank targetRank, List<MissionRequirement> requirements,
                       ItemStack rewardItem, int rewardMerit, boolean repeatable, UUID createdBy) {
        this.missionId = UUID.randomUUID();
        this.displayName = displayName;
        this.targetRank = targetRank;
        this.requirements = requirements;
        this.rewardItem = rewardItem;
        this.rewardMerit = rewardMerit;
        this.repeatable = repeatable;
        this.createdBy = createdBy;
        this.createdTime = System.currentTimeMillis();
    }

    // Getters
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

    public ItemStack getRewardItem() {
        return rewardItem;
    }

    public int getRewardMerit() {
        return rewardMerit;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public long getCreatedTime() {
        return createdTime;
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
        return !acceptedBy.containsKey(playerId) || repeatable;
    }

    // NBT serialization - Updated for NeoForge 1.21.1
    public CompoundTag toNBT(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("missionId", missionId);
        tag.putString("displayName", displayName);
        tag.putString("targetRank", targetRank.name());
        tag.put("rewardItem", rewardItem.save(registries, new CompoundTag()));
        tag.putInt("rewardMerit", rewardMerit);
        tag.putBoolean("repeatable", repeatable);
        tag.putUUID("createdBy", createdBy);
        tag.putLong("createdTime", createdTime);

        // Save requirements
        ListTag requirementsList = new ListTag();
        for (MissionRequirement req : requirements) {
            requirementsList.add(req.toNBT());
        }
        tag.put("requirements", requirementsList);

        // Save accepted missions
        ListTag acceptedList = new ListTag();
        for (Map.Entry<UUID, MissionProgress> entry : acceptedBy.entrySet()) {
            CompoundTag progressTag = new CompoundTag();
            progressTag.putUUID("playerId", entry.getKey());
            progressTag.put("progress", entry.getValue().toNBT());
            acceptedList.add(progressTag);
        }
        tag.put("acceptedBy", acceptedList);

        return tag;
    }

    public static SectMission fromNBT(CompoundTag tag, HolderLookup.Provider registries) {
        String displayName = tag.getString("displayName");
        SectRank targetRank = SectRank.valueOf(tag.getString("targetRank"));

        List<MissionRequirement> requirements = new ArrayList<>();
        ListTag requirementsList = tag.getList("requirements", 10); // 10 = TAG_Compound
        for (int i = 0; i < requirementsList.size(); i++) {
            requirements.add(MissionRequirement.fromNBT(requirementsList.getCompound(i)));
        }

        ItemStack rewardItem = ItemStack.parse(registries, tag.getCompound("rewardItem")).orElse(ItemStack.EMPTY);
        int rewardMerit = tag.getInt("rewardMerit");
        boolean repeatable = tag.getBoolean("repeatable");
        UUID createdBy = tag.getUUID("createdBy");

        SectMission mission = new SectMission(displayName, targetRank, requirements, rewardItem, rewardMerit, repeatable, createdBy);

        // Load accepted missions
        ListTag acceptedList = tag.getList("acceptedBy", 10);
        for (int i = 0; i < acceptedList.size(); i++) {
            CompoundTag progressTag = acceptedList.getCompound(i);
            UUID playerId = progressTag.getUUID("playerId");
            MissionProgress progress = MissionProgress.fromNBT(progressTag.getCompound("progress"));
            mission.acceptedBy.put(playerId, progress);
        }

        return mission;
    }
}