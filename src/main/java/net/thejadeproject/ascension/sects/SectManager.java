package net.thejadeproject.ascension.sects;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SectManager extends SavedData {
    final Map<String, Sect> sects = new ConcurrentHashMap<>();
    final Map<UUID, String> playerSects = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> chatToggles = new ConcurrentHashMap<>();
    public final Map<String, Set<String>> pendingInvites = new ConcurrentHashMap<>();
    final Map<String, Set<String>> allyRequests = new ConcurrentHashMap<>();
    private final String worldId;

    public Map<String, Sect> getAllSects() {
        return new HashMap<>(sects); // Return a copy to prevent modification
    }

    public SectManager(String worldId) {
        this.worldId = worldId;
    }

    public List<String> getPendingInvites(String playerName) {
        List<String> invitedSects = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : pendingInvites.entrySet()) {
            if (entry.getValue().contains(playerName.toLowerCase())) {
                invitedSects.add(entry.getKey());
            }
        }
        return invitedSects;
    }

    // Add this method to SectManager.java
    public List<String> getPendingAllyRequests(String sectName) {
        List<String> requestingSects = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : allyRequests.entrySet()) {
            if (entry.getValue().contains(sectName)) {
                requestingSects.add(entry.getKey());
            }
        }
        return requestingSects;
    }

    public static SectManager get(MinecraftServer server, String worldId) {
        if (server == null || worldId == null) return null;

        // Use world-specific file name
        String fileName = "sects_" + worldId.replaceAll("[^a-zA-Z0-9-_]", "_");

        return server.overworld().getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(() -> new SectManager(worldId),
                        (tag, registries) -> load(tag, registries, worldId)),
                fileName
        );
    }

    public static SectManager load(CompoundTag tag, HolderLookup.Provider registries, String worldId) {
        SectManager manager = new SectManager(worldId);

        // Load sects
        if (tag.contains("sects")) {
            CompoundTag sectsTag = tag.getCompound("sects");
            for (String sectName : sectsTag.getAllKeys()) {
                CompoundTag sectTag = sectsTag.getCompound(sectName);
                Sect sect = Sect.fromNBT(sectTag, registries);
                manager.sects.put(sectName, sect);

                // Rebuild playerSects map
                for (SectMember member : sect.getMembers().values()) {
                    manager.playerSects.put(member.getPlayerId(), sectName);
                }
            }
        }

        // Load chat toggles
        if (tag.contains("chatToggles")) {
            CompoundTag togglesTag = tag.getCompound("chatToggles");
            for (String playerId : togglesTag.getAllKeys()) {
                manager.chatToggles.put(UUID.fromString(playerId), togglesTag.getBoolean(playerId));
            }
        }

        return manager;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        // Save sects
        CompoundTag sectsTag = new CompoundTag();
        for (Map.Entry<String, Sect> entry : sects.entrySet()) {
            sectsTag.put(entry.getKey(), entry.getValue().toNBT(registries));
        }
        tag.put("sects", sectsTag);

        // Save chat toggles
        CompoundTag togglesTag = new CompoundTag();
        for (Map.Entry<UUID, Boolean> entry : chatToggles.entrySet()) {
            togglesTag.putBoolean(entry.getKey().toString(), entry.getValue());
        }
        tag.put("chatToggles", togglesTag);

        return tag;
    }

    // Add this method to manually trigger saving
    public void save() {
        this.setDirty();
    }

    // NEW: Public method to remove player from all sect data
    public void removePlayerFromSect(UUID playerId) {
        playerSects.remove(playerId);
        chatToggles.remove(playerId);
        setDirty();
    }

    public void removeInvite(String sectName, String playerName) {
        Set<String> invites = pendingInvites.get(sectName);
        if (invites != null) {
            invites.remove(playerName.toLowerCase());
            if (invites.isEmpty()) {
                pendingInvites.remove(sectName);
            }
            setDirty();
        }
    }

    // NEW: Public method to remove a sect completely
    public void removeSect(String sectName) {
        Sect sect = sects.get(sectName);
        if (sect != null) {
            // Remove all members from playerSects and chatToggles
            for (SectMember member : sect.getMembers().values()) {
                playerSects.remove(member.getPlayerId());
                chatToggles.remove(member.getPlayerId());
            }
            sects.remove(sectName);
            setDirty();
        }
    }

    // Sect management methods
    public boolean createSect(String name, UUID ownerId, String ownerName) {
        if (sects.containsKey(name) || playerSects.containsKey(ownerId)) {
            return false;
        }

        Sect sect = new Sect(name, ownerId, ownerName);
        sects.put(name, sect);
        playerSects.put(ownerId, name);
        setDirty();
        return true;
    }

    public Sect getSect(String name) {
        return sects.get(name);
    }

    public Sect getPlayerSect(UUID playerId) {
        String sectName = playerSects.get(playerId);
        return sectName != null ? sects.get(sectName) : null;
    }

    public void addPlayerToSect(UUID playerId, String playerName, String sectName) {
        Sect sect = sects.get(sectName);
        if (sect != null) {
            sect.addMember(playerId, playerName, SectRank.OUTER);
            playerSects.put(playerId, sectName);
            setDirty();
        }
    }

    public void setChatToggle(UUID playerId, boolean enabled) {
        chatToggles.put(playerId, enabled);
        setDirty();
    }

    public boolean isChatToggled(UUID playerId) {
        return chatToggles.getOrDefault(playerId, false);
    }

    public void addInvite(String sectName, String playerName) {
        pendingInvites.computeIfAbsent(sectName, k -> new HashSet<>()).add(playerName.toLowerCase());
    }

    public boolean hasInvite(String sectName, String playerName) {
        Set<String> invites = pendingInvites.get(sectName);
        return invites != null && invites.contains(playerName.toLowerCase());
    }

    public void addAllyRequest(String fromSect, String toSect) {
        allyRequests.computeIfAbsent(toSect, k -> new HashSet<>()).add(fromSect);
    }

    public boolean hasAllyRequest(String fromSect, String toSect) {
        Set<String> requests = allyRequests.get(toSect);
        return requests != null && requests.contains(fromSect);
    }

    public void removeAllyRequest(String fromSect, String toSect) {
        Set<String> requests = allyRequests.get(toSect);
        if (requests != null) {
            requests.remove(fromSect);
        }
    }

    public String getWorldId() {
        return worldId;
    }
}