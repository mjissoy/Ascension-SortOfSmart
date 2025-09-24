package net.thejadeproject.ascension.items;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.thejadeproject.ascension.util.ModTags;

public class ModToolTiers {
    public static final Tier JADE = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_JADE_TOOL,
            1900, 10, 6f, 20, () -> Ingredient.of(ModItems.JADE));
}
