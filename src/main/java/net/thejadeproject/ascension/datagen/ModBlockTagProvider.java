package net.thejadeproject.ascension.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, AscensionCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.JADE_BLOCK.get())
                .add(ModBlocks.JADE_ORE.get())
                .add(ModBlocks.PILL_CAULDRON_HUMAN_LOW.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.JADE_ORE.get())
                .add(ModBlocks.JADE_BLOCK.get())
                .add(ModBlocks.PILL_CAULDRON_HUMAN_LOW.get());

        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.GOLDEN_PALM_LOG.get())
                .add(ModBlocks.GOLDEN_PALM_WOOD.get())
                .add(ModBlocks.STRIPPED_GOLDEN_PALM_LOG.get())
                .add(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD.get());
    }
}
