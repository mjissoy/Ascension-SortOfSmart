package net.thejadeproject.ascension.sects;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.thejadeproject.ascension.sects.missions.SectMission;

import java.util.*;
import java.util.stream.Collectors;

import static net.thejadeproject.ascension.sects.SectCommand.broadcastToSect;

public class Sect {
    private final String name;
    private UUID ownerId;
    private final Map<UUID, SectMember> members;
    private final Set<String> allies;
    private boolean open;
    private final long createdTime;
    private String description;
    private boolean friendlyFire = true;


    private final Map<Long, UUID> claimedChunks = new HashMap<>(); // chunkPos -> player who claimed it
    private int depositedMerit = 0; // Total merit deposited by members
    private final Set<String> enemies = new HashSet<>(); // Names of enemy sects

    private final Map<UUID, SectMission> missions = new HashMap<>();
    private final Map<UUID, List<ItemStack>> missionSubmissions = new HashMap<>();
    private final Map<UUID, Integer> playerMerit = new HashMap<>();
    private final Map<UUID, Set<UUID>> elderRecommendations = new HashMap<>();





    public Sect(String name, UUID ownerId, String ownerName) {
        this.name = name;
        this.ownerId = ownerId;
        this.members = new HashMap<>();
        this.allies = new HashSet<>();
        this.open = false;
        this.createdTime = System.currentTimeMillis();
        this.description = "A newly formed sect.";

        members.put(ownerId, new SectMember(ownerId, ownerName, SectRank.SECT_MASTER, ""));
    }

    public String getName() { return name; }
    public UUID getOwnerId() { return ownerId; }
    public Map<UUID, SectMember> getMembers() { return members; }
    public Set<String> getAllies() { return allies; }
    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Power methods
    public int getMaxPower() {
        return maxPower;
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public int getTotalDeposited() {
        return totalDeposited;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    public void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
    }

    public void setTotalDeposited(int totalDeposited) {
        this.totalDeposited = totalDeposited;
    }

    public void addPower(int amount) {
        this.currentPower += amount;
        this.maxPower += amount; // Increase max power when depositing
        this.totalDeposited += amount;
    }

    public boolean usePower(int amount) {
        if (this.currentPower >= amount) {
            this.currentPower -= amount;
            return true;
        }
        return false;
    }

    private int maxPower;  // Maximum power capacity
    private int currentPower; // Current available power
    private int totalDeposited; // Total power ever deposited (for tracking)



    public Set<String> getEnemies() { return enemies; }

    public void addEnemy(String sectName) { enemies.add(sectName); }

    public void removeEnemy(String sectName) { enemies.remove(sectName); }

    public boolean isEnemy(String sectName) { return enemies.contains(sectName); }

    public Map<Long, UUID> getClaimedChunks() { return claimedChunks; }

    public void claimChunk(long chunkPos, UUID claimerId) {
        claimedChunks.put(chunkPos, claimerId);
    }

    public void unclaimChunk(long chunkPos) {
        claimedChunks.remove(chunkPos);
    }

    public boolean isChunkClaimed(long chunkPos) {
        return claimedChunks.containsKey(chunkPos);
    }

    public UUID getChunkClaimer(long chunkPos) {
        return claimedChunks.get(chunkPos);
    }

    public int getClaimedChunkCount() {
        return claimedChunks.size();
    }

    // For claiming chunks - check if we have enough power
    public boolean canClaimChunk() {
        // Example: 1 power per chunk claim, adjust as needed
        return this.currentPower >= 1;
    }

    // For overclaiming - check if we have more power than another sect's max power
    public boolean canOverclaim(Sect otherSect) {
        return this.currentPower > otherSect.getMaxPower();
    }

    // Update your existing getPower() method if it exists
    public int getPower() {
        return this.currentPower; // For backward compatibility
    }

    public void refundPower(int amount) {
        this.currentPower += amount;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }


    public void addMember(UUID playerId, String playerName, SectRank rank) {
        members.put(playerId, new SectMember(playerId, playerName, rank, ""));
    }

    public void removeMember(UUID playerId) {
        members.remove(playerId);
    }

    public SectMember getMember(UUID playerId) {
        return members.get(playerId);
    }

    public void setMemberRank(UUID playerId, SectRank rank) {
        SectMember member = members.get(playerId);
        if (member != null) {
            members.put(playerId, new SectMember(member.getPlayerId(), member.getPlayerName(), rank, member.getTitle()));
        }
    }

    public void setMemberTitle(UUID playerId, String title) {
        SectMember member = members.get(playerId);
        if (member != null) {
            members.put(playerId, new SectMember(member.getPlayerId(), member.getPlayerName(), member.getRank(), title));
        }
    }

    public void addAlly(String sectName) {
        allies.add(sectName);
    }

    public void removeAlly(String sectName) {
        allies.remove(sectName);
    }

    public boolean hasPermission(UUID playerId, SectPermission permission) {
        SectMember member = members.get(playerId);
        if (member == null) return false;

        return member.getRank().hasPermission(permission);
    }

    public void setOwner(UUID newOwnerId) {
        this.ownerId = newOwnerId;
    }

    public void addMission(SectMission mission) {
        missions.put(mission.getMissionId(), mission);
    }

    public void removeMission(UUID missionId) {
        missions.remove(missionId);
    }

    public Collection<SectMission> getMissions() {
        return missions.values();
    }

    public SectMission getMission(UUID missionId) {
        return missions.get(missionId);
    }

    public List<SectMission> getMissionsForRank(SectRank rank) {
        return missions.values().stream()
                .filter(mission -> mission.getTargetRank() == rank)
                .collect(Collectors.toList());
    }

    // Merit point methods
    public int getPlayerMerit(UUID playerId) {
        return playerMerit.getOrDefault(playerId, 0);
    }

    public void addRecommendation(UUID targetPlayerId, UUID recommenderId) {
        elderRecommendations.computeIfAbsent(targetPlayerId, k -> new HashSet<>()).add(recommenderId);
    }

    public Set<UUID> getRecommendations(UUID playerId) {
        return elderRecommendations.getOrDefault(playerId, new HashSet<>());
    }

    public int getRecommendationCount(UUID playerId) {
        return getRecommendations(playerId).size();
    }

    public void clearRecommendations(UUID playerId) {
        elderRecommendations.remove(playerId);
    }

    public void addPlayerMerit(UUID playerId, int merit) {
        int current = playerMerit.getOrDefault(playerId, 0);
        playerMerit.put(playerId, current + merit);

        // Auto-promotion check
        SectMember member = getMember(playerId);
        if (member != null) {
            if (member.getRank() == SectRank.OUTER && getPlayerMerit(playerId) >= 2500) {
                setMemberRank(playerId, SectRank.INNER);
                broadcastToSect(this, null, "§a" + member.getPlayerName() + " has been automatically promoted to Inner Sect for reaching 2500 merit points!");
            } else if (member.getRank() == SectRank.INNER && getPlayerMerit(playerId) >= 10000) {
                // Check if they have enough recommendations (3) from Elders or Sect Master
                int recommendationCount = getRecommendationCount(playerId);
                if (recommendationCount >= 3) {
                    setMemberRank(playerId, SectRank.ELDER);
                    clearRecommendations(playerId); // Clear recommendations after promotion
                    broadcastToSect(this, null, "§6" + member.getPlayerName() + " has been promoted to Elder for reaching 10000 merit points and receiving " + recommendationCount + " recommendations!");
                }
                // If not enough recommendations, they stay as Inner even with enough merit
            }
        }
    }

    public void addMissionSubmission(UUID missionId, List<ItemStack> items) {
        List<ItemStack> nonEmptyItems = items.stream()
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());

        if (!nonEmptyItems.isEmpty()) {
            missionSubmissions.put(missionId, nonEmptyItems);
        }
    }

    public void cleanupExpiredMissions() {
        List<UUID> missionsToRemove = new ArrayList<>();

        for (SectMission mission : missions.values()) {
            if (mission.isExpired()) {
                missionsToRemove.add(mission.getMissionId());
            }
        }

        for (UUID missionId : missionsToRemove) {
            missions.remove(missionId);
            missionSubmissions.remove(missionId);
        }
    }

    public Map<UUID, List<ItemStack>> getMissionSubmissions() {
        return missionSubmissions;
    }

    public List<ItemStack> removeMissionSubmission(UUID missionId) {
        return missionSubmissions.remove(missionId);
    }

    public void clearAllSubmissions() {
        missionSubmissions.clear();
    }

    public CompoundTag toNBT(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putUUID("ownerId", ownerId);
        tag.putBoolean("open", open);
        tag.putLong("createdTime", createdTime);
        tag.putString("description", description);
        tag.putBoolean("friendlyFire", friendlyFire);


        // Save deposited merit
        tag.putInt("depositedMerit", depositedMerit);

        // Save enemies
        ListTag enemiesList = new ListTag();
        for (String enemy : enemies) {
            enemiesList.add(StringTag.valueOf(enemy));
        }
        tag.put("enemies", enemiesList);

        // Save claimed chunks
        CompoundTag chunksTag = new CompoundTag();
        for (Map.Entry<Long, UUID> entry : claimedChunks.entrySet()) {
            chunksTag.putUUID(entry.getKey().toString(), entry.getValue());
        }
        tag.put("claimedChunks", chunksTag);

        // Save members
        ListTag membersList = new ListTag();
        for (SectMember member : members.values()) {
            membersList.add(member.toNBT());
        }
        tag.put("members", membersList);

        // Save allies
        ListTag alliesList = new ListTag();
        for (String ally : allies) {
            alliesList.add(StringTag.valueOf(ally));
        }
        tag.put("allies", alliesList);

        // Save missions
        ListTag missionsList = new ListTag();
        for (SectMission mission : missions.values()) {
            missionsList.add(mission.toNBT(registries));
        }
        tag.put("missions", missionsList);

        // Save merit points
        CompoundTag meritTag = new CompoundTag();
        for (Map.Entry<UUID, Integer> entry : playerMerit.entrySet()) {
            meritTag.putInt(entry.getKey().toString(), entry.getValue());
        }
        tag.put("playerMerit", meritTag);

        // Save mission submissions
        ListTag submissionsList = new ListTag();
        for (Map.Entry<UUID, List<ItemStack>> entry : missionSubmissions.entrySet()) {
            CompoundTag submissionTag = new CompoundTag();
            submissionTag.putUUID("missionId", entry.getKey());

            ListTag itemsList = new ListTag();
            for (ItemStack stack : entry.getValue()) {
                itemsList.add(stack.save(registries, new CompoundTag()));
            }
            submissionTag.put("items", itemsList);
            submissionsList.add(submissionTag);
        }
        tag.put("missionSubmissions", submissionsList);

        // Save recommendations
        CompoundTag recommendationsTag = new CompoundTag();
        for (Map.Entry<UUID, Set<UUID>> entry : elderRecommendations.entrySet()) {
            ListTag recommenderList = new ListTag();
            for (UUID recommenderId : entry.getValue()) {
                recommenderList.add(StringTag.valueOf(recommenderId.toString()));
            }
            recommendationsTag.put(entry.getKey().toString(), recommenderList);
        }
        tag.put("elderRecommendations", recommendationsTag);

        return tag;
    }

    public static Sect fromNBT(CompoundTag tag, HolderLookup.Provider registries) {
        String name = tag.getString("name");
        UUID ownerId = tag.getUUID("ownerId");

        Sect sect = new Sect(name, ownerId, "Loading...");
        sect.open = tag.getBoolean("open");

        // Load description
        if (tag.contains("description")) {
            sect.description = tag.getString("description");
        }

        // Load friendly fire setting
        if (tag.contains("friendlyFire")) {
            sect.friendlyFire = tag.getBoolean("friendlyFire");
        }

        // Load members
        ListTag membersList = tag.getList("members", Tag.TAG_COMPOUND);
        for (int i = 0; i < membersList.size(); i++) {
            CompoundTag memberTag = membersList.getCompound(i);
            SectMember member = SectMember.fromNBT(memberTag);
            sect.members.put(member.getPlayerId(), member);
        }

        // Load allies
        ListTag alliesList = tag.getList("allies", Tag.TAG_STRING);
        for (int i = 0; i < alliesList.size(); i++) {
            sect.allies.add(alliesList.getString(i));
        }

        if (tag.contains("missions")) {
            ListTag missionsList = tag.getList("missions", 10);
            for (int i = 0; i < missionsList.size(); i++) {
                SectMission mission = SectMission.fromNBT(missionsList.getCompound(i), registries);
                sect.missions.put(mission.getMissionId(), mission);
            }
        }

        // Load merit points
        if (tag.contains("playerMerit")) {
            CompoundTag meritTag = tag.getCompound("playerMerit");
            for (String playerIdStr : meritTag.getAllKeys()) {
                sect.playerMerit.put(UUID.fromString(playerIdStr), meritTag.getInt(playerIdStr));
            }
        }

        // Load mission submissions
        if (tag.contains("missionSubmissions")) {
            ListTag submissionsList = tag.getList("missionSubmissions", 10);
            for (int i = 0; i < submissionsList.size(); i++) {
                CompoundTag submissionTag = submissionsList.getCompound(i);
                UUID missionId = submissionTag.getUUID("missionId");

                List<ItemStack> items = new ArrayList<>();
                ListTag itemsList = submissionTag.getList("items", 10);
                for (int j = 0; j < itemsList.size(); j++) {
                    ItemStack stack = ItemStack.parse(registries, itemsList.getCompound(j)).orElse(ItemStack.EMPTY);
                    if (!stack.isEmpty()) {
                        items.add(stack);
                    }
                }
                sect.missionSubmissions.put(missionId, items);
            }
        }

        // Load recommendations
        if (tag.contains("elderRecommendations")) {
            CompoundTag recommendationsTag = tag.getCompound("elderRecommendations");
            for (String playerIdStr : recommendationsTag.getAllKeys()) {
                UUID playerId = UUID.fromString(playerIdStr);
                ListTag recommenderList = recommendationsTag.getList(playerIdStr, Tag.TAG_STRING);
                Set<UUID> recommenders = new HashSet<>();
                for (int i = 0; i < recommenderList.size(); i++) {
                    recommenders.add(UUID.fromString(recommenderList.getString(i)));
                }
                sect.elderRecommendations.put(playerId, recommenders);
            }
        }
        // Load deposited merit
        if (tag.contains("depositedMerit")) {
            sect.depositedMerit = tag.getInt("depositedMerit");
        }

        // Load enemies
        if (tag.contains("enemies")) {
            ListTag enemiesList = tag.getList("enemies", Tag.TAG_STRING);
            for (int i = 0; i < enemiesList.size(); i++) {
                sect.enemies.add(enemiesList.getString(i));
            }
        }

        // Load claimed chunks
        if (tag.contains("claimedChunks")) {
            CompoundTag chunksTag = tag.getCompound("claimedChunks");
            for (String chunkPosStr : chunksTag.getAllKeys()) {
                try {
                    long chunkPos = Long.parseLong(chunkPosStr);
                    UUID claimerId = chunksTag.getUUID(chunkPosStr);
                    sect.claimedChunks.put(chunkPos, claimerId);
                } catch (NumberFormatException e) {
                    // Skip invalid chunk positions
                }
            }
        }

        return sect;
    }

    public boolean deductPlayerMerit(UUID playerId, int amount) {
        int current = playerMerit.getOrDefault(playerId, 0);
        if (current >= amount) {
            playerMerit.put(playerId, current - amount);
            return true;
        }
        return false;
    }

    public void disband() {
        members.clear();
        allies.clear();
    }
}