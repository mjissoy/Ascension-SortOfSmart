package net.thejadeproject.ascension;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.*;

public class AscensionCommonConfig {
    // Pill Cauldron
    public final ModConfigSpec.ConfigValue<List<? extends String>> PILL_CAULDRON_HEAT_ITEMS;
    public final ModConfigSpec.IntValue PILL_CAULDRON_HEAT_LOSS_INTERVAL;
    public final ModConfigSpec.IntValue PILL_CAULDRON_HEAT_LOSS_AMOUNT;
    public final ModConfigSpec.IntValue PILL_CAULDRON_MAX_HEAT;

    // Artifacts
    public final ModConfigSpec.IntValue REPAIR_INTERVAL;
    public final ModConfigSpec.IntValue REPAIR_AMOUNT;


    public AscensionCommonConfig(ModConfigSpec.Builder builder) {
        builder.push("PillCauldron");
        builder.comment("Heat items for Pill Cauldron",
                "Format: [\"modid:item_id,heat_value\", \"modid:item_id,heat_value\"]",
                "Example: [\"minecraft:coal,8\", \"minecraft:coal_block,72\", \"ascension:crimson_lotus_flame,300\"]");
        PILL_CAULDRON_HEAT_ITEMS = builder.defineList("heat_items",
                () -> new ArrayList<>(Arrays.asList(
                        "minecraft:coal,8",
                        "minecraft:coal_block,72",
                        "ascension:crimson_lotus_flame,300"
                )),
                this::validateHeatItemEntry
        );

        builder.comment("Heat loss settings for Pill Cauldron");
        PILL_CAULDRON_HEAT_LOSS_INTERVAL = builder
                .comment("Interval in ticks between heat loss (20 ticks = 1 second) [Default: 20]")
                .defineInRange("heat_loss_interval", 20, 1, Integer.MAX_VALUE);

        PILL_CAULDRON_HEAT_LOSS_AMOUNT = builder
                .comment("Amount of heat lost each interval [Default: 1]")
                .defineInRange("heat_loss_amount", 1, 0, Integer.MAX_VALUE);

        PILL_CAULDRON_MAX_HEAT = builder
                .comment("Maximum heat capacity of the Pill Cauldron [Default: 1000]")
                .defineInRange("max_heat", 1000, 100, Integer.MAX_VALUE);

        builder.pop();

        builder.push("Artifacts");
        builder.comment("Artifact Modifiers");
        REPAIR_INTERVAL = builder
                .comment("Interval in ticks between Repairs [Default: 100]")
                .defineInRange("repairInterval", 100, 1, Integer.MAX_VALUE);
        REPAIR_AMOUNT = builder
                .comment("Amount of durability to Repair each interval [Default: 2]")
                .defineInRange("repairAmount", 2, 1, Integer.MAX_VALUE);
        builder.pop();
    }

    private boolean validateHeatItemEntry(Object entry) {
        if (!(entry instanceof String)) return false;
        String entryStr = (String) entry;
        String[] parts = entryStr.split(",");
        if (parts.length != 2) return false;
        try {
            Integer.parseInt(parts[1].trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Map<String, Integer> getHeatItems() {
        Map<String, Integer> heatItems = new HashMap<>();
        for (String entry : PILL_CAULDRON_HEAT_ITEMS.get()) {
            String[] parts = entry.split(",");
            if (parts.length == 2) {
                try {
                    String itemId = parts[0].trim();
                    int heatValue = Integer.parseInt(parts[1].trim());
                    heatItems.put(itemId, heatValue);
                } catch (NumberFormatException e) {
                    AscensionCraft.LOGGER.warn("Invalid heat item entry: {}", entry);
                }
            }
        }
        return heatItems;
    }
}