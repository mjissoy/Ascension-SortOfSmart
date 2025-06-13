package net.thejadeproject.ascension.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
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

        tag(ModTags.Items.FIRE)
                .add(ModItems.GOLDEN_SUN_LEAF.get());
        tag(ModTags.Items.MEDICINAL)
                .add(ModItems.GOLDEN_SUN_LEAF.get());

        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.GOLDEN_PALM_LOG.get().asItem())
                .add(ModBlocks.GOLDEN_PALM_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.GOLDEN_PALM_PLANKS.asItem());

    }
}
