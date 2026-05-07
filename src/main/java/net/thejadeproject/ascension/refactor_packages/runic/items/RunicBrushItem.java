package net.thejadeproject.ascension.refactor_packages.runic.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class RunicBrushItem extends Item {

    private final int extraRuneSlots;
    private final ResourceLocation affinityRune;

    public RunicBrushItem(Properties properties, int extraRuneSlots, ResourceLocation affinityRune) {
        super(properties);
        this.extraRuneSlots = extraRuneSlots;
        this.affinityRune = affinityRune;
    }

    public int getExtraRuneSlots() {
        return extraRuneSlots;
    }

    public ResourceLocation getAffinityRune() {
        return affinityRune;
    }

    // TODO: Use this when opening the Runic Casting UI.
}