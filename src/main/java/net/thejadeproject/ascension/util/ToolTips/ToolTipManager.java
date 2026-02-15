package net.thejadeproject.ascension.util.ToolTips;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;

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
        ToolTipHandler.registerTooltip(ModItems.PILL_RESIDUE.get(), Component.translatable("ascension.tooltip.waste").withStyle(ChatFormatting.DARK_RED), Component.literal("☆☆☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.REGENERATION_PILL.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.NEUTRALITY_PILL.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.FASTING_PILL_T1.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.FASTING_PILL_T2.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.FASTING_PILL_T3.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.REBIRTH_PILL.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.AQUA));
        ToolTipHandler.registerAnimatedTooltip(ModItems.REBIRTH_PILL.get(), Component.translatable("ascension.tooltip.rgb.rebirth_warning"), 0.001f);

        ToolTipHandler.registerTooltip(ModItems.ANTIDOTE_PILL_T1.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.translatable("ascension.tooltip.qdppill").withStyle(ChatFormatting.GREEN), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.ANTIDOTE_PILL_T2.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.ANTIDOTE_PILL_T3.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));

        ToolTipHandler.registerTooltip(ModItems.CLEANSING_PILL_T1.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.CLEANSING_PILL_T2.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.CLEANSING_PILL_T3.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.CLEANSING_PILL_T4.get(), Component.translatable("ascension.tooltip.medicinal").withStyle(ChatFormatting.RED), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.DARK_RED));

        //Cultivation Pills
        ToolTipHandler.registerTooltip(ModItems.INNER_REINFORCEMENT_PILL_T1.get(), Component.translatable("ascension.tooltip.cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.INNER_REINFORCEMENT_PILL_T2.get(), Component.translatable("ascension.tooltip.cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★★☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.INNER_REINFORCEMENT_PILL_T3.get(), Component.translatable("ascension.tooltip.cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));

        ToolTipHandler.registerTooltip(ModItems.ESSENCE_GATHERING_PILL_T1.get(), Component.translatable("ascension.tooltip.cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.ESSENCE_GATHERING_PILL_T2.get(), Component.translatable("ascension.tooltip.cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★★☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.ESSENCE_GATHERING_PILL_T3.get(), Component.translatable("ascension.tooltip.cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));

        ToolTipHandler.registerTooltip(ModItems.SPIRIT_FOCUS_PILL_T1.get(), Component.translatable("ascension.tooltip.cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.SPIRIT_FOCUS_PILL_T2.get(), Component.translatable("ascension.tooltip.cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★★☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.SPIRIT_FOCUS_PILL_T3.get(), Component.translatable("ascension.tooltip.cultivation").withStyle(ChatFormatting.LIGHT_PURPLE), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));

        //Poison Pills
        ToolTipHandler.registerTooltip(ModItems.QI_DEVOURING_PARASITE_PILL.get(), Component.translatable("ascension.tooltip.poisonous").withStyle(ChatFormatting.GREEN), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
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
        ToolTipHandler.registerTooltip(ModItems.SOUL_FLAME.get(), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.FLAME.get(), Component.literal("★★★★☆").withStyle(ChatFormatting.YELLOW));


        //Artifacts
        ToolTipHandler.registerTooltip(ModItems.SPATIAL_RUPTURE_TALISMAN_T1.get(), Component.translatable("ascension.tooltip.srtt1").withStyle(ChatFormatting.GOLD), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.SPATIAL_RUPTURE_TALISMAN_T2.get(), Component.translatable("ascension.tooltip.srtt2").withStyle(ChatFormatting.GOLD), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.DARK_RED));
        ToolTipHandler.registerTooltip(ModItems.SPATIAL_RUPTURE_TALISMAN_T3.get(), Component.translatable("ascension.tooltip.srtt3").withStyle(ChatFormatting.GOLD), Component.literal("★★☆☆☆").withStyle(ChatFormatting.DARK_RED));
        ToolTipHandler.registerTooltip(ModItems.SOULSTEAD_RETURN_TALISMAN.get(), Component.translatable("ascension.tooltip.srt").withStyle(ChatFormatting.GOLD), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.WORLD_AXIS_TALISMAN.get(), Component.translatable("ascension.tooltip.wat").withStyle(ChatFormatting.GOLD), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.VOID_MARKING_TALISMAN.get(), Component.translatable("ascension.tooltip.vmt").withStyle(ChatFormatting.GOLD), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.RED));

        ToolTipHandler.registerTooltip(ModItems.TABLET_OF_DESTRUCTION_HUMAN.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.TABLET_OF_DESTRUCTION_EARTH.get(), Component.literal("★★★★☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.TABLET_OF_DESTRUCTION_HEAVEN.get(), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.REPAIR_SLIP.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.ENDER_POUCH.get(), Component.literal("★★☆☆☆").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerTooltip(ModItems.FIRE_GOURD.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.RED));

        ToolTipHandler.registerTooltip(ModItems.FORMATION_SLIP_ACACIA.get(), Component.literal("★★★★★").withStyle(ChatFormatting.YELLOW));
        ToolTipHandler.registerAnimatedTooltip(ModItems.FORMATION_SLIP_ACACIA.get(), Component.translatable("ascension.tooltip.rgb.jade_slip"), 0.001f);

        ToolTipHandler.registerTooltip(ModItems.SPATIAL_RING.get(), Component.literal("★☆☆☆☆").withStyle(ChatFormatting.RED));


        //Crafting Ingredients
        ToolTipHandler.registerTooltip(ModItems.TALISMAN_PAPER.get(), Component.literal("★★★☆☆").withStyle(ChatFormatting.YELLOW));




        // Essence Manuals
        ToolTipHandler.registerTooltip(ModTechniques.PURE_FIRE_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));
        ToolTipHandler.registerTooltip(ModTechniques.PURE_WATER_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));
        ToolTipHandler.registerTooltip(ModTechniques.PURE_WOOD_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));
        ToolTipHandler.registerTooltip(ModTechniques.PURE_EARTH_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));
        ToolTipHandler.registerTooltip(ModTechniques.PURE_METAL_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));
        //TODO update mod technique
        ToolTipHandler.registerTooltip(ModTechniques.SWIFT_BREEZE_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));

        ToolTipHandler.registerTooltip(ModTechniques.VOID_SWALLOWING_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));
        ToolTipHandler.registerTooltip(ModTechniques.THUNDER_HEART_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));

        ToolTipHandler.registerTooltip(ModTechniques.VOID_WALKER_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));

        ToolTipHandler.registerTooltip(ModTechniques.COSMIC_CREATION_ESSENCE_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));
        ToolTipHandler.registerTooltip(ModTechniques.INFINITE_TIME_ESSENCE_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.essence"));

        // Intent Manuals
        ToolTipHandler.registerTooltip(ModTechniques.PURE_SWORD_INTENT.manual.get(), Component.translatable("ascension.technique.path.intent"));
        ToolTipHandler.registerTooltip(ModTechniques.PURE_AXE_INTENT.manual.get(), Component.translatable("ascension.technique.path.intent"));
        ToolTipHandler.registerTooltip(ModTechniques.PURE_BLADE_INTENT.manual.get(), Component.translatable("ascension.technique.path.intent"));
        ToolTipHandler.registerTooltip(ModTechniques.PURE_SPEAR_INTENT.manual.get(), Component.translatable("ascension.technique.path.intent"));
        ToolTipHandler.registerTooltip(ModTechniques.PURE_FIST_INTENT.manual.get(), Component.translatable("ascension.technique.path.intent"));
        ToolTipHandler.registerTooltip(ModTechniques.FOCUSED_STRIKE_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.intent"));

        ToolTipHandler.registerTooltip(ModTechniques.FIST_KINGS_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.intent"));
        ToolTipHandler.registerTooltip(ModTechniques.BLADE_DANCE_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.intent"));

        ToolTipHandler.registerTooltip(ModTechniques.SWORD_SAINT_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.intent"));

        ToolTipHandler.registerTooltip(ModTechniques.UNIVERSE_DEVOURER_INTENT_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.intent"));
        ToolTipHandler.registerTooltip(ModTechniques.ETERNAL_REINCARNATION_INTENT_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.intent"));


        // Body Manuals
        ToolTipHandler.registerTooltip(ModTechniques.WOOD_ELEMENTAL_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));
        ToolTipHandler.registerTooltip(ModTechniques.FIRE_ELEMENTAL_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));
        ToolTipHandler.registerTooltip(ModTechniques.EARTH_ELEMENTAL_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));
        ToolTipHandler.registerTooltip(ModTechniques.METAL_ELEMENTAL_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));
        ToolTipHandler.registerTooltip(ModTechniques.WATER_ELEMENTAL_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));
        ToolTipHandler.registerTooltip(ModTechniques.IRON_SKIN_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));

        ToolTipHandler.registerTooltip(ModTechniques.DIVINE_PHOENIX_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));
        ToolTipHandler.registerTooltip(ModTechniques.JADE_BONE_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));

        ToolTipHandler.registerTooltip(ModTechniques.CELESTIAL_BODY_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));

        ToolTipHandler.registerTooltip(ModTechniques.PRIMORDIAL_CHAOS_BODY_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));
        ToolTipHandler.registerTooltip(ModTechniques.DRAGON_KING_BODY_TECHNIQUE.manual.get(), Component.translatable("ascension.technique.path.body"));


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
