package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.items.ModItems;

public enum SpatialRing {
    IRON("Iron", Rarity.COMMON, 18, 2, 9, "iron_gui.png", 176, 150, 7, 67, 8, 57, ModItems.IRON_SPATIAL_RING),
    GOLD("Gold", Rarity.UNCOMMON, 33, 3, 11, "gold_gui.png", 212, 168, 25, 85, 8, 75, ModItems.GOLD_SPATIAL_RING),
    DIAMOND("Diamond", Rarity.RARE, 66, 6, 11, "diamond_gui.png", 212, 222, 25, 139, 8, 129, ModItems.DIAMOND_SPATIAL_RING),
    NETHERITE("Netherite", Rarity.RARE, 99, 9, 11, "netherite_gui.png", 212, 276, 25, 193, 8, 183, ModItems.NETHERITE_SPATIAL_RING),
    JADE("Jade", Rarity.EPIC, 158, 13, 16, "jade_gui.png", 302, 258, 71, 175, 71, 165, ModItems.JADE_SPATIAL_RING);

    public final Rarity rarity;
    public final int slots;
    public final ResourceLocation texture;
    public final int xSize;
    public final int ySize;
    public final int slotXOffset;
    public final int slotYOffset;
    public final int inventoryLabelX; // New field
    public final int inventoryLabelY; // New field
    public final int slotRows;
    public final int slotCols;
    public final String name;
    public final DeferredItem<Item> item;

    SpatialRing(String name, Rarity rarity, int slots, int rows, int cols, String location, int xSize, int ySize,
                int slotXOffset, int slotYOffset, int inventoryLabelX, int inventoryLabelY, DeferredItem<Item> itemIn) {
        this.name = name;
        this.rarity = rarity;
        this.slots = slots;
        this.slotRows = rows;
        this.slotCols = cols;
        this.texture = ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "textures/gui/spatial_rings/" + location);
        this.xSize = xSize;
        this.ySize = ySize;
        this.slotXOffset = slotXOffset;
        this.slotYOffset = slotYOffset;
        this.inventoryLabelX = inventoryLabelX; // Initialize new field
        this.inventoryLabelY = inventoryLabelY; // Initialize new field
        this.item = itemIn;
    }
}