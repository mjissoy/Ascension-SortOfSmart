package net.thejadeproject.ascension.menus.spatialrings;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;
import net.thejadeproject.ascension.items.stones.SpatialStoneItem;

import java.util.Optional;
import java.util.UUID;

public class SpatialRingData {
    private final UUID uuid;
    private final ASCItemHandler inventory;
    private final Optional<IItemHandler> optional;
    public final Metadata meta = new Metadata();

    public static final int BASE_ROWS = 3;

    public IItemHandler getHandler() {
        return this.inventory;
    }

    public int getExtraRows() {
        return this.meta.extraRows;
    }

    public int getTotalRows() {
        return BASE_ROWS + this.meta.extraRows;
    }

    public int getTotalSlots() {
        return getTotalRows() * 9;
    }

    public void updateAccessRecords(String player, long time) {
        if (this.meta.firstAccessedTime == 0) {
            this.meta.firstAccessedTime = time;
            this.meta.firstAccessedPlayer = player;
        }
        this.meta.setLastAccessedTime(time);
        this.meta.setLastAccessedPlayer(player);
    }

    public SpatialRingData(UUID uuid) {
        this.uuid = uuid;
        this.inventory = new ASCItemHandler(getTotalSlots());
        this.optional = Optional.of(this.inventory);
    }

    public SpatialRingData(UUID uuid, CompoundTag incomingNBT, HolderLookup.Provider pRegistries) {
        this.uuid = uuid;
        this.inventory = new ASCItemHandler(getTotalSlots());
        if (incomingNBT.contains("Inventory")) {
            this.inventory.deserializeNBT(pRegistries, incomingNBT.getCompound("Inventory"));
        }
        this.optional = Optional.of(this.inventory);
        if (incomingNBT.contains("Metadata")) {
            this.meta.deserializeNBT(pRegistries, incomingNBT.getCompound("Metadata"));
            if (this.meta.extraRows > 0) {
                int newSize = getTotalSlots();
                if (newSize > this.inventory.getSlots()) {
                    this.inventory.upgrade(newSize);
                }
            }
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void updateUpgrades(NonNullList<ItemStack> upgradeItems) {
        int extraRows = 0;
        for (ItemStack stack : upgradeItems) {
            if (stack.getItem() instanceof SpatialStoneItem stone) {
                extraRows += stone.getRowsAdded();
            }
        }
        this.meta.extraRows = Math.min(extraRows, 10);
        this.meta.upgradeItems = upgradeItems;
        int newSize = getTotalSlots();
        if (newSize != this.inventory.getSlots()) {
            this.inventory.upgrade(newSize);
        }
        SpatialRingManager.get().setDirty();
    }

    public static Optional<SpatialRingData> fromNBT(CompoundTag nbt, HolderLookup.Provider pRegistries) {
        if (nbt.contains("UUID")) {
            UUID uuid = nbt.getUUID("UUID");
            return Optional.of(new SpatialRingData(uuid, nbt, pRegistries));
        }
        return Optional.empty();
    }

    public CompoundTag toNBT(HolderLookup.Provider pRegistries) {
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("UUID", this.uuid);
        nbt.putInt("ExtraRows", this.meta.extraRows);
        nbt.put("Inventory", this.inventory.serializeNBT(pRegistries));
        nbt.put("Metadata", this.meta.serializeNBT(pRegistries));
        return nbt;
    }

    public static class Metadata implements INBTSerializable<CompoundTag> {
        private String firstAccessedPlayer = "";
        private long firstAccessedTime = 0;
        private String lastAccessedPlayer = "";
        private long lastAccessedTime = 0;
        private int extraRows = 0;
        private NonNullList<ItemStack> upgradeItems = NonNullList.withSize(36, ItemStack.EMPTY);

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

        public int getExtraRows() {
            return this.extraRows;
        }

        public NonNullList<ItemStack> getUpgradeItems() {
            return this.upgradeItems;
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider pRegistries) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("firstPlayer", this.firstAccessedPlayer);
            nbt.putLong("firstTime", this.firstAccessedTime);
            nbt.putString("lastPlayer", this.lastAccessedPlayer);
            nbt.putLong("lastTime", this.lastAccessedTime);
            nbt.putInt("extraRows", this.extraRows);

            ListTag upgradeList = new ListTag();
            for (int i = 0; i < this.upgradeItems.size(); i++) {
                ItemStack stack = this.upgradeItems.get(i);
                if (!stack.isEmpty()) {
                    CompoundTag itemTag = (CompoundTag) stack.save(pRegistries, new CompoundTag());
                    itemTag.putInt("Slot", i); // Add slot info to the item tag
                    upgradeList.add(itemTag);
                }
            }
            nbt.put("upgradeItems", upgradeList);
            return nbt;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider pRegistries, CompoundTag nbt) {
            this.firstAccessedPlayer = nbt.getString("firstPlayer");
            this.firstAccessedTime = nbt.getLong("firstTime");
            this.lastAccessedPlayer = nbt.getString("lastPlayer");
            this.lastAccessedTime = nbt.getLong("lastTime");
            this.extraRows = nbt.getInt("extraRows");
            this.upgradeItems = NonNullList.withSize(36, ItemStack.EMPTY);

            if (nbt.contains("upgradeItems", Tag.TAG_LIST)) {
                ListTag upgradeList = nbt.getList("upgradeItems", Tag.TAG_COMPOUND);
                for (int i = 0; i < upgradeList.size(); i++) {
                    CompoundTag itemTag = upgradeList.getCompound(i);
                    if (itemTag.contains("Slot")) {
                        int slot = itemTag.getInt("Slot");
                        // Remove the Slot tag before parsing to avoid issues
                        itemTag.remove("Slot");
                        if (slot >= 0 && slot < this.upgradeItems.size()) {
                            ItemStack parsedStack = ItemStack.parse(pRegistries, itemTag).orElse(ItemStack.EMPTY);
                            if (!parsedStack.isEmpty()) {
                                this.upgradeItems.set(slot, parsedStack);
                            }
                        }
                    }
                }
            }
        }
    }
}
