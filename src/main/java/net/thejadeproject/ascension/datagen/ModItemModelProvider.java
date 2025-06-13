package net.thejadeproject.ascension.datagen;


import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AscensionCraft.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {



        //Items
        basicItem(ModItems.JADE.get());
        basicItem(ModItems.RAW_JADE.get());



        //Pills
        basicItem(ModItems.REGENERATION_PILL.get());



        //herbs
        basicItem(ModItems.GOLDEN_SUN_LEAF.get());

        //Saplings
        saplingItem(ModBlocks.GOLDEN_PALM_SAPLING);
    }

    private ItemModelBuilder saplingItem(DeferredBlock<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"block/" + item.getId().getPath()));
    }

}
