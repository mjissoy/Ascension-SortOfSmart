package net.thejadeproject.ascension.items.herbs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Resolves herb bonus effects by checking item tags on input herbs.
 *
 * Tag files to create (in data/ascension/tags/items/):
 *   herb_fire_affinity.json      → items with fire affinity bonus
 *   herb_qi_nourishing.json      → items with qi nourishing bonus
 *   herb_body_strengthening.json → items with body strengthening bonus
 *   herb_cold_essence.json       → items with cold essence bonus
 *   herb_spirit_clarity.json     → items with spirit clarity bonus
 *
 * Each tag is paired with the bonus effect ResourceLocation string.
 */
public class HerbBonusEffects {

    // ── Tag → Bonus Effect mapping ───────────────────────────────
    // Add new entries here as you add herb categories.
    public static final List<HerbBonusEntry> BONUS_ENTRIES = List.of(
            new HerbBonusEntry(
                    ResourceLocation.fromNamespaceAndPath("ascension", "herb_fire_affinity"),
                    "ascension:fire_affinity"
            ),
            new HerbBonusEntry(
                    ResourceLocation.fromNamespaceAndPath("ascension", "herb_qi_nourishing"),
                    "ascension:qi_nourishing"
            ),
            new HerbBonusEntry(
                    ResourceLocation.fromNamespaceAndPath("ascension", "herb_body_strengthening"),
                    "ascension:body_strengthening"
            ),
            new HerbBonusEntry(
                    ResourceLocation.fromNamespaceAndPath("ascension", "herb_cold_essence"),
                    "ascension:cold_essence"
            ),
            new HerbBonusEntry(
                    ResourceLocation.fromNamespaceAndPath("ascension", "herb_spirit_clarity"),
                    "ascension:spirit_clarity"
            )
    );

    /**
     * Scans a list of input herb stacks and rolls a random bonus effect.
     *
     * @param inputStacks   The herb stacks used in crafting.
     * @param bonusChance   Chance (0.0 - 1.0) that any bonus is applied.
     * @return              A bonus effect ID string, or empty string if none.
     */
    public static String rollBonusEffect(List<ItemStack> inputStacks, double bonusChance) {
        if (ThreadLocalRandom.current().nextDouble() > bonusChance) {
            return ""; // No bonus this craft
        }

        // Collect all bonus effects present among input herbs
        List<String> eligibleBonuses = new ArrayList<>();
        for (ItemStack stack : inputStacks) {
            if (stack.isEmpty()) continue;
            for (HerbBonusEntry entry : BONUS_ENTRIES) {
                if (stack.is(net.minecraft.tags.TagKey.create(
                        net.minecraft.core.registries.Registries.ITEM, entry.tagLocation()))) {
                    if (!eligibleBonuses.contains(entry.bonusEffectId())) {
                        eligibleBonuses.add(entry.bonusEffectId());
                    }
                }
            }
        }

        if (eligibleBonuses.isEmpty()) return "";
        // Pick one random eligible bonus
        return eligibleBonuses.get(ThreadLocalRandom.current().nextInt(eligibleBonuses.size()));
    }

    public record HerbBonusEntry(ResourceLocation tagLocation, String bonusEffectId) {}
}