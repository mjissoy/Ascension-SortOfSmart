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

        //Blocks
        add("block.ascension.jade_ore", "Jade Ore");
        add("block.ascension.jade_block", "Jade Block");

        //GUI & Other Stuff
        add("creativetab.ascension.items", "Ascension Items");
        add("creativetab.ascension.blocks", "Ascension Blocks");
        add("category.ascension.cultivation", "Ascension");
        add("key.ascension.cultivate", "Cultivate");

    }
}
