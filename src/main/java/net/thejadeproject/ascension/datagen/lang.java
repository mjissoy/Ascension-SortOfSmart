package net.thejadeproject.ascension.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.thejadeproject.ascension.AscensionCraft;

public class lang extends LanguageProvider {
    public lang(PackOutput output, String locale) {
        super(output, AscensionCraft.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {

        //Items
        add("item.ascension.jade", "Jade");
        add("item.ascension.raw_jade", "Raw Jade");
        add("item.ascension.regeneration_pill", "Regeneration Pill");

        //Blocks
        add("block.ascension.jade_ore", "Jade Ore");
        add("block.ascension.jade_block", "Jade Block");

        //GUI & Other Stuff
        add("creativetab.ascension.items", "Ascension Items");
        add("creativetab.ascension.blocks", "Ascension Blocks");
        add("effect.ascension.qi_enhanced_regeneration", "Qi Enhanced Regen");
        add("category.ascension.cultivation", "Ascension");
        add("key.ascension.cultivate", "Cultivate");
        add("key.ascension.introspection", "Introspection");




        add("ascension.configuration.Multipliers", "Stats Multipliers");
        add("ascension.configuration.CultivationMultipliers", "Cultivation Multipliers");
        add("ascension.configuration.Cultivation_Speed", "Cultivation Speed");
        add("ascension.configuration.Minor_Cultivation_Stats_Multiplier", "Minor Cultivation Stats Multiplier");
        add("ascension.configuration.Major_Cultivation_Stats_Multiplier", "Major Cultivation Stats Multiplier");
        add("ascension.configuration.Flight_Realm", "Flight Realm");

        add("ascension.configuration.AttributeMultipliers", "Attributes Multipliers");
        add("ascension.configuration.Speed_Multiplier_Max", "Max Speed");
        add("block.ascension.pill_cauldron", "Pill Cauldron");

    }
}
