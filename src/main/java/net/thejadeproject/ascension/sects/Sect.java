package net.thejadeproject.ascension.sects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Sect {
    private final String name;
    private UUID ownerId; // Remove final to make it mutable
    private final Map<UUID, SectMember> members;
    private final Set<String> allies;
    private boolean open;
    private final long createdTime;

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

    public CompoundTag toNBT() {
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

        return tag;
    }

    public static Sect fromNBT(CompoundTag tag) {
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

        return sect;
    }

    public void disband() {
        // Clear all members
        members.clear();
        allies.clear();
    }
}