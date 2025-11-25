package net.thejadeproject.ascension.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.util.ModTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper){
        super(output, lookupProvider, blockTags, AscensionCraft.MOD_ID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(ModTags.Items.daoItemTags.get("ascension:fire"))
                .add(ModItems.GOLDEN_SUN_LEAF.get());
        tag(ModTags.Items.daoItemTags.get("ascension:earth"))
                .add(ModItems.IRONWOOD_SPROUT.get());
        tag(ModTags.Items.daoItemTags.get("ascension:metal"))
                .add(ModItems.WHITE_JADE_ORCHID.get());
        tag(ModTags.Items.MEDICINAL)
                .add(ModItems.GOLDEN_SUN_LEAF.get())
                .add(ModItems.WHITE_JADE_ORCHID.get())
                .add(ModItems.JADE_BAMBOO_OF_SERENITY.get())
                .add(ModItems.HUNDRED_YEAR_FIRE_GINSENG.get())
                .add(ModItems.HUNDRED_YEAR_SNOW_GINSENG.get())
                .add(ModItems.HUNDRED_YEAR_GINSENG.get())
                .add(ModItems.IRONWOOD_SPROUT.get());
        tag(ModTags.Items.HUMAN)
                .add(ModItems.GOLDEN_SUN_LEAF.get())
                .add(ModItems.JADE_BAMBOO_OF_SERENITY.get())
                .add(ModItems.WHITE_JADE_ORCHID.get())
                .add(ModItems.IRONWOOD_SPROUT.get());
        tag(ModTags.Items.ALCHEMY_FAILURE)
                .add(Items.GUNPOWDER);
        tag(ModTags.Items.ALCHEMY_SUCCESS)
                .add(ModItems.REGENERATION_PILL.get())
                .add(ModItems.CLEANSING_PILL.get())
                .add(ModItems.REBIRTH_PILL.get())
                .add(ModItems.INNER_REINFORCEMENT_PILL_T1.get())
                .add(ModItems.FASTING_PILL_T1.get())
                .add(ModItems.FASTING_PILL_T2.get())
                .add(ModItems.FASTING_PILL_T3.get());
        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.GOLDEN_PALM_LOG.get().asItem())
                .add(ModBlocks.GOLDEN_PALM_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.GOLDEN_PALM_PLANKS.asItem());

        tag(ModTags.Items.daoItemTags.get("ascension:sword_intent"))
                .addTag(ItemTags.SWORDS);

        tag(ModTags.Items.daoItemTags.get("ascension:blade_intent"))
                .addTag(ModTags.Items.BLADE);

        tag(ModTags.Items.daoItemTags.get("ascension:bow_intent"))
                .add(Items.BOW)
                .add(Items.CROSSBOW);

        tag(ModTags.Items.daoItemTags.get("ascension:spear_intent"))
                .addTag(ModTags.Items.SPEAR);


        tag(ModTags.Items.BLADE)
                .add(ModItems.SEARING_BLADE.get())
                .add(ModItems.JADE_BLADE.get());

        tag(ModTags.Items.SPEAR)
                .add(ModItems.JADE_SPEAR.get());


        tag(ModTags.Items.SPEAR_ENCHANTABLE)
                .addTag(ModTags.Items.SPEAR);
        tag(Tags.Items.ENCHANTABLES)
                .addTag(ModTags.Items.BLADE)
                .addTag(ModTags.Items.SPEAR_ENCHANTABLE);
        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE)
                .addTag(ModTags.Items.BLADE)
                .addTag(ModTags.Items.SPEAR_ENCHANTABLE);
        tag(ItemTags.BREAKS_DECORATED_POTS)
                .addTag(ModTags.Items.BLADE)
                .addTag(ModTags.Items.SPEAR);
        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTag(ModTags.Items.BLADE)
                .addTag(ModTags.Items.SPEAR);
        tag(Tags.Items.TOOLS)
                .addTag(ModTags.Items.BLADE)
                .addTag(ModTags.Items.SPEAR);


    }
}
