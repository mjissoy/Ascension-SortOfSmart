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





        //Block Items
        buttonItem(ModBlocks.GOLDEN_PALM_BUTTON, ModBlocks.GOLDEN_PALM_PLANKS);
        fenceItem(ModBlocks.GOLDEN_PALM_FENCE, ModBlocks.GOLDEN_PALM_PLANKS);
        basicItem(ModBlocks.GOLDEN_PALM_DOOR.asItem());



        //Items
        basicItem(ModItems.JADE.get());
        basicItem(ModItems.RAW_JADE.get());

        //Drops
        basicItem(ModItems.LIVING_CORE.get());
        basicItem(ModItems.UNDEAD_CORE.get());



        //Not Used For Anything in mod except being icons etc...
        basicItem(ModItems.ASCENSION_ICON.get());



        //Pills
        basicItem(ModItems.REGENERATION_PILL.get());



        //herbs
        basicItem(ModItems.GOLDEN_SUN_LEAF.get());

        basicItem(ModItems.IRONWOOD_SPROUT.get());
        withExistingParent("ironwood_sprout_block", "item/generated")
                .texture("layer0", "ascension:block/ironwood_sprout_block");

        //Saplings
        saplingItem(ModBlocks.GOLDEN_PALM_SAPLING);



    }

    private ItemModelBuilder saplingItem(DeferredBlock<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,"block/" + item.getId().getPath()));
    }

    public void buttonItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void fenceItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void wallItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

}
