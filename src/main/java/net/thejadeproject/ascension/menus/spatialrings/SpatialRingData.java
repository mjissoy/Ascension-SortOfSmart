package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.thejadeproject.ascension.items.artifacts.SpatialRing;

import java.util.Optional;
import java.util.UUID;

public class SpatialRingData {
    private final UUID uuid;
    private SpatialRing tier;
    private final ASCItemHandler inventory;
    private final Optional<IItemHandler> optional;
    public final Metadata meta = new Metadata();


    public Optional<IItemHandler> getOptional() {
        return this.optional;
    }

    public IItemHandler getHandler() {
        return this.inventory;
    }


    public SpatialRing getTier() {
        return this.tier;
    }

    public void updateAccessRecords(String player, long time) {
        if (this.meta.firstAccessedTime == 0) {
            //new Backpack, set creation data
            this.meta.firstAccessedTime = time;
            this.meta.firstAccessedPlayer = player;
        }

        this.meta.setLastAccessedTime(time);
        this.meta.setLastAccessedPlayer(player);
    }

    public SpatialRingData(UUID uuid, SpatialRing tier) {
        this.uuid = uuid;
        this.tier = tier;

        this.inventory = new ASCItemHandler(tier.slots);
        this.optional = Optional.of(this.inventory);
    }

    public SpatialRingData(UUID uuid, CompoundTag incomingNBT, HolderLookup.Provider pRegistries) {
        this.uuid = uuid;
        this.tier = SpatialRing.values()[Math.min(incomingNBT.getInt("Tier"), SpatialRing.JADE.ordinal())];

        this.inventory = new ASCItemHandler(this.tier.slots);

        // Fixes FTBTeam/FTB-Modpack-Issues #478 Part 2
        if (incomingNBT.getCompound("Inventory").contains("Size")) {
            if (incomingNBT.getCompound("Inventory").getInt("Size") != tier.slots)
                incomingNBT.getCompound("Inventory").putInt("Size", tier.slots);
        }
        this.inventory.deserializeNBT(pRegistries, incomingNBT.getCompound("Inventory"));
        this.optional = Optional.of(this.inventory);

        if (incomingNBT.contains("Metadata"))
            this.meta.deserializeNBT(RegistryAccess.EMPTY, incomingNBT.getCompound("Metadata"));
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public static Optional<SpatialRingData> fromNBT(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if (nbt.contains("UUID")) {
            UUID uuid = nbt.getUUID("UUID");
            return Optional.of(new SpatialRingData(uuid, nbt, pRegistries));
        }
        return Optional.empty();
    }

    public void upgrade(SpatialRing newTier) {
        if (newTier.ordinal() > this.tier.ordinal()) {
            this.tier = newTier;
            this.inventory.upgrade(this.tier.slots);
        }
    }

    public CompoundTag toNBT(HolderLookup.Provider pRegistries) {
        CompoundTag nbt = new CompoundTag();

        nbt.putUUID("UUID", this.uuid);
        nbt.putInt("Tier", this.tier.ordinal());

        nbt.put("Inventory", this.inventory.serializeNBT(pRegistries));

        nbt.put("Metadata", this.meta.serializeNBT(RegistryAccess.EMPTY));

        return nbt;
    }



    public static class Metadata implements INBTSerializable<CompoundTag> {
        private String firstAccessedPlayer = "";

        private long firstAccessedTime = 0;
        private String lastAccessedPlayer = "";
        private long lastAccessedTime = 0;
        public long getLastAccessedTime() {
            return this.lastAccessedTime;
        }

        public void setLastAccessedTime(long lastAccessedTime) {
            this.lastAccessedTime = lastAccessedTime;
        }

        public String getLastAccessedPlayer() {
            return this.lastAccessedPlayer;
        }

        public void setLastAccessedPlayer(String lastAccessedPlayer) {
            this.lastAccessedPlayer = lastAccessedPlayer;
        }

        public long getFirstAccessedTime() {
            return this.firstAccessedTime;
        }

        public String getFirstAccessedPlayer() {
            return this.firstAccessedPlayer;
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider pRegistries) {
            CompoundTag nbt = new CompoundTag();

            nbt.putString("firstPlayer", this.firstAccessedPlayer);
            nbt.putLong("firstTime", this.firstAccessedTime);
            nbt.putString("lastPlayer", this.lastAccessedPlayer);
            nbt.putLong("lastTime", this.lastAccessedTime);

            return nbt;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
            this.firstAccessedPlayer = nbt.getString("firstPlayer");
            this.firstAccessedTime = nbt.getLong("firstTime");
            this.lastAccessedPlayer = nbt.getString("lastPlayer");
            this.lastAccessedTime = nbt.getLong("lastTime");
        }
    }
}
