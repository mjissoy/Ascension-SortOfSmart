package net.thejadeproject.ascension.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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



        tag(ModTags.Items.INGOTS_BLACK_IRON)
                .add(ModItems.BLACK_IRON_INGOT.get());
        tag(ModTags.Items.BLACK_IRON_RAW)
                .add(ModItems.RAW_BLACK_IRON.get());
        tag(ModTags.Items.NUGGETS_BLACK_IRON)
                .add(ModItems.BLACK_IRON_NUGGET.get());

        tag(ModTags.Items.INGOTS_FROST_SILVER)
                .add(ModItems.FROST_SILVER_INGOT.get());
        tag(ModTags.Items.FROST_SILVER_RAW)
                .add(ModItems.RAW_FROST_SILVER.get());
        tag(ModTags.Items.NUGGETS_FROST_SILVER)
                .add(ModItems.FROST_SILVER_NUGGET.get());


        this.tag(ModTags.Items.CURIOS_RING)
                .add(ModItems.IRON_SPATIAL_RING.get())
                .add(ModItems.GOLD_SPATIAL_RING.get())
                .add(ModItems.DIAMOND_SPATIAL_RING.get())
                .add(ModItems.NETHERITE_SPATIAL_RING.get())
                .add(ModItems.JADE_SPATIAL_RING.get());

    }
}
