package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
    private static final HashMap<UUID, SpatialRingData> data = new HashMap<>();
    public static final SpatialRingManager blankClient = new SpatialRingManager();

    public HashMap<UUID, SpatialRingData> getMap() { return data; }

    public static SpatialRingManager get() {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(new Factory<>(SpatialRingManager::new, SpatialRingManager::load), NAME);
        else
            return blankClient;
    }

    public Optional<SpatialRingData> getSpatialRing(UUID uuid) {
        return Optional.ofNullable(data.get(uuid));
    }

    public SpatialRingData getOrCreateSpatialring(UUID uuid) {
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
        if (nbt.contains("Spatialrings")) {
            ListTag list = nbt.getList("Spatialrings", Tag.TAG_COMPOUND);
            list.forEach((backpackNBT) -> SpatialRingData.fromNBT((CompoundTag) backpackNBT, pRegistries).ifPresent((backpack) -> data.put(backpack.getUuid(), backpack)));
        }
        return new SpatialRingManager();
    }

    @Override
    @Nonnull
    public CompoundTag save(CompoundTag compound, HolderLookup.Provider pRegistries) {
        ListTag backpacks = new ListTag();
        data.forEach(((uuid, backpackData) -> backpacks.add(backpackData.toNBT(pRegistries))));
        compound.put("Spatialrings", backpacks);
        return compound;
    }
}