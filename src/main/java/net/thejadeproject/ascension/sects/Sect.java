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
    private final Set<String> enemies;
    private boolean open;
    private final long createdTime;
    private String description;
    private boolean friendlyFire = false;

    // NEW: Settings
    private boolean announcePromotions = true;
    private boolean allowOuterInvite = false;
    private int maxMembers = 0; // 0 = unlimited

    private final Map<UUID, SectMission> missions = new HashMap<>();
    private final Map<UUID, List<ItemStack>> sectBank = new HashMap<>();
    final Map<UUID, Integer> playerMerit = new HashMap<>();
    final Map<UUID, Set<UUID>> elderRecommendations = new HashMap<>();

    public Sect(String name, UUID ownerId, String ownerName) {
        this.name = name;
        this.ownerId = ownerId;
        this.members = new HashMap<>();
        this.allies = new HashSet<>();
        this.enemies = new HashSet<>();
        this.open = false;
        this.createdTime = System.currentTimeMillis();
        this.description = "A newly formed sect.";
        members.put(ownerId, new SectMember(ownerId, ownerName, SectRank.SECT_MASTER, ""));
    }

    public String getName() { return name; }
    public UUID getOwnerId() { return ownerId; }
    public Map<UUID, SectMember> getMembers() { return members; }
    public Set<String> getAllies() { return allies; }
    public Set<String> getEnemies() { return enemies; }
    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // NEW: Getters and setters for settings
    public boolean isAnnouncePromotions() { return announcePromotions; }
    public void setAnnouncePromotions(boolean announcePromotions) { this.announcePromotions = announcePromotions; }

    public boolean isAllowOuterInvite() { return allowOuterInvite; }
    public void setAllowOuterInvite(boolean allowOuterInvite) { this.allowOuterInvite = allowOuterInvite; }

    public int getMaxMembers() { return maxMembers; }
    public void setMaxMembers(int maxMembers) { this.maxMembers = maxMembers; }

    public boolean isFull() {
        return maxMembers > 0 && members.size() >= maxMembers;
    }

    public void addEnemy(String sectName) { enemies.add(sectName); }
    public void removeEnemy(String sectName) { enemies.remove(sectName); }
    public boolean isEnemy(String sectName) { return enemies.contains(sectName); }

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
        playerMerit.remove(playerId);
        elderRecommendations.remove(playerId);
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
        sectBank.remove(missionId);
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

        SectMember member = getMember(playerId);
        if (member != null) {
            if (member.getRank() == SectRank.OUTER && getPlayerMerit(playerId) >= 2500) {
                setMemberRank(playerId, SectRank.INNER);
                // MODIFIED: Check announcePromotions setting
                if (announcePromotions) {
                    broadcastToSect(this, null, "§a" + member.getPlayerName() + " has been automatically promoted to Inner Sect for reaching 2500 merit points!");
                }
            } else if (member.getRank() == SectRank.INNER && getPlayerMerit(playerId) >= 10000) {
                int recommendationCount = getRecommendationCount(playerId);
                if (recommendationCount >= 3) {
                    setMemberRank(playerId, SectRank.ELDER);
                    clearRecommendations(playerId);
                    // MODIFIED: Check announcePromotions setting
                    if (announcePromotions) {
                        broadcastToSect(this, null, "§6" + member.getPlayerName() + " has been promoted to Elder for reaching 10000 merit points and receiving " + recommendationCount + " recommendations!");
                    }
                }
            }
        }
    }

    public void addMissionSubmission(UUID missionId, List<ItemStack> items) {
        List<ItemStack> nonEmptyItems = items.stream()
                .filter(stack -> !stack.isEmpty())
                .collect(Collectors.toList());
        if (!nonEmptyItems.isEmpty()) {
            sectBank.computeIfAbsent(missionId, k -> new ArrayList<>()).addAll(nonEmptyItems);
        }
    }

    public Map<UUID, List<ItemStack>> getSectBankItems() {
        return sectBank;
    }

    public List<ItemStack> removeMissionSubmission(UUID missionId) {
        return sectBank.remove(missionId);
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
            sectBank.remove(missionId);
        }
    }

    public CompoundTag toNBT(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putUUID("ownerId", ownerId);
        tag.putBoolean("open", open);
        tag.putLong("createdTime", createdTime);
        tag.putString("description", description);
        tag.putBoolean("friendlyFire", friendlyFire);

        // NEW: Save settings
        tag.putBoolean("announcePromotions", announcePromotions);
        tag.putBoolean("allowOuterInvite", allowOuterInvite);
        tag.putInt("maxMembers", maxMembers);

        ListTag enemiesList = new ListTag();
        for (String enemy : enemies) {
            enemiesList.add(StringTag.valueOf(enemy));
        }
        tag.put("enemies", enemiesList);

        ListTag membersList = new ListTag();
        for (SectMember member : members.values()) {
            membersList.add(member.toNBT());
        }
        tag.put("members", membersList);

        ListTag alliesList = new ListTag();
        for (String ally : allies) {
            alliesList.add(StringTag.valueOf(ally));
        }
        tag.put("allies", alliesList);

        ListTag missionsList = new ListTag();
        for (SectMission mission : missions.values()) {
            missionsList.add(mission.toNBT(registries));
        }
        tag.put("missions", missionsList);

        CompoundTag meritTag = new CompoundTag();
        for (Map.Entry<UUID, Integer> entry : playerMerit.entrySet()) {
            meritTag.putInt(entry.getKey().toString(), entry.getValue());
        }
        tag.put("playerMerit", meritTag);

        ListTag bankList = new ListTag();
        for (Map.Entry<UUID, List<ItemStack>> entry : sectBank.entrySet()) {
            CompoundTag bankEntry = new CompoundTag();
            bankEntry.putUUID("missionId", entry.getKey());
            ListTag itemsList = new ListTag();
            for (ItemStack stack : entry.getValue()) {
                itemsList.add(stack.save(registries, new CompoundTag()));
            }
            bankEntry.put("items", itemsList);
            bankList.add(bankEntry);
        }
        tag.put("sectBank", bankList);

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

        if (tag.contains("description")) {
            sect.description = tag.getString("description");
        }
        if (tag.contains("friendlyFire")) {
            sect.friendlyFire = tag.getBoolean("friendlyFire");
        }

        // NEW: Load settings with defaults for backward compatibility
        if (tag.contains("announcePromotions")) {
            sect.announcePromotions = tag.getBoolean("announcePromotions");
        }
        if (tag.contains("allowOuterInvite")) {
            sect.allowOuterInvite = tag.getBoolean("allowOuterInvite");
        }
        if (tag.contains("maxMembers")) {
            sect.maxMembers = tag.getInt("maxMembers");
        }

        if (tag.contains("enemies")) {
            ListTag enemiesList = tag.getList("enemies", Tag.TAG_STRING);
            for (int i = 0; i < enemiesList.size(); i++) {
                sect.enemies.add(enemiesList.getString(i));
            }
        }

        ListTag membersList = tag.getList("members", Tag.TAG_COMPOUND);
        for (int i = 0; i < membersList.size(); i++) {
            CompoundTag memberTag = membersList.getCompound(i);
            SectMember member = SectMember.fromNBT(memberTag);
            sect.members.put(member.getPlayerId(), member);
        }

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

        if (tag.contains("playerMerit")) {
            CompoundTag meritTag = tag.getCompound("playerMerit");
            for (String playerIdStr : meritTag.getAllKeys()) {
                sect.playerMerit.put(UUID.fromString(playerIdStr), meritTag.getInt(playerIdStr));
            }
        }

        if (tag.contains("sectBank")) {
            ListTag bankList = tag.getList("sectBank", 10);
            for (int i = 0; i < bankList.size(); i++) {
                CompoundTag bankEntry = bankList.getCompound(i);
                UUID missionId = bankEntry.getUUID("missionId");
                List<ItemStack> items = new ArrayList<>();
                ListTag itemsList = bankEntry.getList("items", 10);
                for (int j = 0; j < itemsList.size(); j++) {
                    ItemStack stack = ItemStack.parse(registries, itemsList.getCompound(j)).orElse(ItemStack.EMPTY);
                    if (!stack.isEmpty()) {
                        items.add(stack);
                    }
                }
                sect.sectBank.put(missionId, items);
            }
        }

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
        enemies.clear();
        sectBank.clear();
    }
}