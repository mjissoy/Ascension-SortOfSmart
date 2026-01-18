package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.thejadeproject.ascension.AscensionCraft;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SpatialRingManager extends SavedData {
    private static final String NAME = AscensionCraft.MOD_ID + "_spatial_ring_data";
    private final HashMap<UUID, SpatialRingData> data = new HashMap<>();
    private static final SpatialRingManager CLIENT_INSTANCE = new SpatialRingManager();

    public HashMap<UUID, SpatialRingData> getMap() { return data; }

    public static SpatialRingManager get() {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            Level overworld = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
            if (overworld != null) {
                return ((ServerLevel) overworld).getDataStorage().computeIfAbsent(new Factory<>(SpatialRingManager::new, SpatialRingManager::load), NAME);
            }
            return new SpatialRingManager();
        } else {
            return CLIENT_INSTANCE;
        }
    }

    public Optional<SpatialRingData> getSpatialRing(UUID uuid) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
            return Optional.ofNullable(data.get(uuid));
        } else {
            // Client-side: check cache or return empty
            ClientSpatialRingCache.CachedRingData cached = ClientSpatialRingCache.getData(uuid);
            if (cached != null) {
                // Create a client-side data object from cache
                SpatialRingData clientData = new SpatialRingData(uuid);
                // Note: This is a simplified version for client-side display only
                return Optional.of(clientData);
            }
            return Optional.empty();
        }
    }

    public SpatialRingData getOrCreateSpatialring(UUID uuid) {
        if (Thread.currentThread().getThreadGroup() != SidedThreadGroups.SERVER) {
            throw new IllegalStateException("Cannot create spatial ring data on client!");
        }

        return data.computeIfAbsent(uuid, id -> {
            setDirty();
            return new SpatialRingData(id);
        });
    }

    public void removeSpatialring(UUID uuid) {
        getSpatialRing(uuid).ifPresent(backpack -> {
            data.remove(uuid);
            setDirty();
        });
    }

    public static SpatialRingManager load(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        SpatialRingManager manager = new SpatialRingManager();
        if (nbt.contains("Spatialrings")) {
            ListTag list = nbt.getList("Spatialrings", Tag.TAG_COMPOUND);
            list.forEach((backpackNBT) ->
                    SpatialRingData.fromNBT((CompoundTag) backpackNBT, pRegistries)
                            .ifPresent(backpack -> manager.data.put(backpack.getUuid(), backpack))
            );
        }
        return manager;
    }

    @Override
    @Nonnull
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider pRegistries) {
        ListTag backpacks = new ListTag();
        data.forEach(((uuid, backpackData) -> backpacks.add(backpackData.toNBT(pRegistries))));
        compound.put("Spatialrings", backpacks);
        return compound;
    }

    // Client-side sync methods
    public void syncToClient(ServerPlayer player, UUID ringUuid) {
        getSpatialRing(ringUuid).ifPresent(data -> data.syncToClient(player));
    }
}