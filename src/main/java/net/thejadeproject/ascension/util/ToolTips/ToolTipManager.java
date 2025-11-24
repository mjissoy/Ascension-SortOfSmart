package net.thejadeproject.ascension.util.ToolTips;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.items.ModItems;

public class ToolTipManager {
    public static void registerAllTooltips() {
        // Example tooltip registrations
        /*ToolTipHandler.registerTooltip(
                ModItems.EXAMPLE_ITEM.get(),
                "This is a simple tooltip",
                "Second line of tooltip"
        );*/

        // Tooltip with custom formatting


        //Medicinal Pills
        ToolTipHandler.registerTooltip(ModItems.PILL_RESIDUE.get(), Component.literal("Waste").withStyle(ChatFormatting.DARK_RED), Component.literal("☆☆☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.REGENERATION_PILL.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.NEUTRALITY_PILL.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.FASTING_PILL_T1.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.FASTING_PILL_T2.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.FASTING_PILL_T3.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.REBIRTH_PILL.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.AQUA));
        ToolTipHandler.registerAnimatedTooltip(ModItems.REBIRTH_PILL.get(), "Be Cautious This Pill Resets Everything!", 0.001f);

        ToolTipHandler.registerTooltip(ModItems.ANTIDOTE_PILL_T1.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("Qi Devouring Parasite Antidote").withStyle(ChatFormatting.GREEN), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.ANTIDOTE_PILL_T2.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.ANTIDOTE_PILL_T3.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));

        ToolTipHandler.registerTooltip(ModItems.CLEANSING_PILL_T1.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.CLEANSING_PILL_T2.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.CLEANSING_PILL_T3.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.CLEANSING_PILL_T4.get(), Component.literal("Medicinal").withStyle(ChatFormatting.RED), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.DARK_RED));

        //Cultivation Pills
        ToolTipHandler.registerTooltip(ModItems.INNER_REINFORCEMENT_PILL_T1.get(), Component.literal("Cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.INNER_REINFORCEMENT_PILL_T2.get(), Component.literal("Cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★★☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.INNER_REINFORCEMENT_PILL_T3.get(), Component.literal("Cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));

        //Poison Pills
        ToolTipHandler.registerTooltip(ModItems.QI_DEVOURING_PARASITE_PILL.get(), Component.literal("Poisonous").withStyle(ChatFormatting.GREEN), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));

        //Herbs
        ToolTipHandler.registerTooltip(ModItems.GOLDEN_SUN_LEAF.get(), Component.literal("★★☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.JADE_BAMBOO_OF_SERENITY.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.WHITE_JADE_ORCHID.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.IRONWOOD_SPROUT.get(), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.HUNDRED_YEAR_FIRE_GINSENG.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.HUNDRED_YEAR_GINSENG.get(), Component.literal("★★☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.HUNDRED_YEAR_SNOW_GINSENG.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));

        //Fires
        ToolTipHandler.registerTooltip(ModItems.CRIMSON_LOTUS_FLAME.get(), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.RED));


        //Artifacts
        ToolTipHandler.registerTooltip(ModItems.SPATIAL_RUPTURE_TALISMAN_T1.get(), Component.literal("2.5k x 2.5k Range │ 60 min cooldown").withStyle(ChatFormatting.GOLD), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.SPATIAL_RUPTURE_TALISMAN_T2.get(), Component.literal("5k x 5k Range │ 40 min cooldown").withStyle(ChatFormatting.GOLD), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.DARK_RED));
        ToolTipHandler.registerTooltip(ModItems.SPATIAL_RUPTURE_TALISMAN_T3.get(), Component.literal("7.5k x 7.5k Range │ 20 min cooldown").withStyle(ChatFormatting.GOLD), Component.literal("★★☆☆☆").withStyle(ChatFormatting.DARK_RED));
        ToolTipHandler.registerTooltip(ModItems.SOULSTEAD_RETURN_TALISMAN.get(), Component.literal("5 min cooldown").withStyle(ChatFormatting.GOLD), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.WORLD_AXIS_TALISMAN.get(), Component.literal("5 min cooldown").withStyle(ChatFormatting.GOLD), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.VOID_MARKING_TALISMAN.get(), Component.literal("5 min cooldown").withStyle(ChatFormatting.GOLD), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.RED));

        ToolTipHandler.registerTooltip(ModItems.TABLET_OF_DESTRUCTION_HUMAN.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.TABLET_OF_DESTRUCTION_EARTH.get(), Component.literal("★★★★☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.TABLET_OF_DESTRUCTION_HEAVEN.get(), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.REPAIR_SLIP.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.ENDER_POUCH.get(), Component.literal("★★☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.FIRE_GOURD.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.RED));

        ToolTipHandler.registerTooltip(ModItems.JADE_SLIP.get(), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerAnimatedTooltip(ModItems.JADE_SLIP.get(), "Sect Identification", 0.001f);

        ToolTipHandler.registerTooltip(ModItems.IRON_SPATIAL_RING.get(), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.RED));
        ToolTipHandler.registerTooltip(ModItems.GOLD_SPATIAL_RING.get(), Component.literal("★★☆☆☆").withStyle(ChatFormatting.RED));
        ToolTipHandler.registerTooltip(ModItems.DIAMOND_SPATIAL_RING.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.RED));
        ToolTipHandler.registerTooltip(ModItems.NETHERITE_SPATIAL_RING.get(), Component.literal("★★★★☆").withStyle(ChatFormatting.RED));
        ToolTipHandler.registerTooltip(ModItems.JADE_SPATIAL_RING.get(), Component.literal("★★★★★").withStyle(ChatFormatting.RED));


        //Crafting Ingredients
        ToolTipHandler.registerTooltip(ModItems.TALISMAN_PAPER.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));

        // Advanced example with conditional logic
        registerAdvancedTooltips();
    }

    private static void registerAdvancedTooltips() {
        // You can add more complex tooltip logic here
        //ToolTipHandler.registerTooltip(
                //ModItems.SPECIAL_ITEM.get(),
                //"Special Item Tooltip",
                //"Hold Shift for more info"
        //);
    }
}
