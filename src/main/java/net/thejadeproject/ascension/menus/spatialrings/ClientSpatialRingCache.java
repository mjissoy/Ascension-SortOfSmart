package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.util.thread.SidedThreadGroups;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSpatialRingCache {
    private static final Map<UUID, CachedRingData> CACHE = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> LAST_ACCESS = new ConcurrentHashMap<>();
    private static final long CACHE_CLEANUP_INTERVAL = 60000; // 1 minute

    public static void updateInventory(UUID ringUuid, NonNullList<ItemStack> items, int scrollOffset, int totalRows) {
        CACHE.compute(ringUuid, (key, existing) -> {
            CachedRingData data = existing != null ? existing : new CachedRingData();
            data.inventory = items;
            data.scrollOffset = scrollOffset;
            data.totalRows = totalRows;
            data.lastUpdated = System.currentTimeMillis();
            return data;
        });
        LAST_ACCESS.put(ringUuid, System.currentTimeMillis());
    }

    public static void updateUpgrades(UUID ringUuid, NonNullList<ItemStack> upgradeItems) {
        CACHE.compute(ringUuid, (key, existing) -> {
            CachedRingData data = existing != null ? existing : new CachedRingData();
            data.upgradeItems = upgradeItems;
            data.lastUpdated = System.currentTimeMillis();
            return data;
        });
        LAST_ACCESS.put(ringUuid, System.currentTimeMillis());
    }

    @Nullable
    public static CachedRingData getData(UUID ringUuid) {
        Long lastAccess = LAST_ACCESS.get(ringUuid);
        if (lastAccess != null && System.currentTimeMillis() - lastAccess < 300000) { // 5 minutes
            return CACHE.get(ringUuid);
        }
        return null;
    }

    public static void cleanupOldCache() {
        long currentTime = System.currentTimeMillis();
        LAST_ACCESS.entrySet().removeIf(entry ->
                currentTime - entry.getValue() > 300000); // Remove entries older than 5 minutes
        CACHE.keySet().removeIf(uuid -> !LAST_ACCESS.containsKey(uuid));
    }

    public static class CachedRingData {
        public NonNullList<ItemStack> inventory = NonNullList.create();
        public NonNullList<ItemStack> upgradeItems = NonNullList.create();
        public int scrollOffset = 0;
        public int totalRows = 3;
        public long lastUpdated = 0;
    }

    // Call this periodically (e.g., from client tick event)
    public static void tick(Level level) {
        if (level != null && !level.isClientSide()) return;
        if (System.currentTimeMillis() % CACHE_CLEANUP_INTERVAL == 0) {
            cleanupOldCache();
        }
    }
}