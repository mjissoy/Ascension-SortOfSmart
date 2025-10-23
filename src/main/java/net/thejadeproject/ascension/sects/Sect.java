package net.thejadeproject.ascension.sects;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.thejadeproject.ascension.sects.missions.SectMission;

import java.util.*;
import java.util.stream.Collectors;

import static net.thejadeproject.ascension.sects.SectCommand.broadcastToSect;

public class Sect {
    private final String name;
    private UUID ownerId; // Remove final to make it mutable
    private final Map<UUID, SectMember> members;
    private final Set<String> allies;
    private boolean open;
    private final long createdTime;

    private final Map<UUID, SectMission> missions = new HashMap<>();
    private final Map<UUID, Integer> playerMerit = new HashMap<>();

    public Sect(String name, UUID ownerId, String ownerName) {
        this.name = name;
        this.ownerId = ownerId;
        this.members = new HashMap<>();
        this.allies = new HashSet<>();
        this.open = false;
        this.createdTime = System.currentTimeMillis();

        // Add owner as first member with SECT_MASTER rank
        members.put(ownerId, new SectMember(ownerId, ownerName, SectRank.SECT_MASTER, ""));
    }

    public String getName() { return name; }
    public UUID getOwnerId() { return ownerId; }
    public Map<UUID, SectMember> getMembers() { return members; }
    public Set<String> getAllies() { return allies; }
    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }

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

    // NEW: Proper setOwner method without reflection
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
                setMemberRank(playerId, SectRank.ELDER);
                broadcastToSect(this, null, "§6" + member.getPlayerName() + " has been automatically promoted to Elder for reaching 10000 merit points!");
            }
        }
    }

    public CompoundTag toNBT(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", name);
        tag.putUUID("ownerId", ownerId);
        tag.putBoolean("open", open);
        tag.putLong("createdTime", createdTime);

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

        return tag;
    }

    public static Sect fromNBT(CompoundTag tag, HolderLookup.Provider registries) {
        String name = tag.getString("name");
        UUID ownerId = tag.getUUID("ownerId");
        String ownerName = ""; // We'll set this from members

        Sect sect = new Sect(name, ownerId, ownerName);
        sect.open = tag.getBoolean("open");

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

        return sect;
    }

    public void disband() {
        // Clear all members
        members.clear();
        allies.clear();
    }
}