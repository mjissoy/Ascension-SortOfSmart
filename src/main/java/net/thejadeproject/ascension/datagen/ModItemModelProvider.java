package net.thejadeproject.ascension.datagen;


import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;

import java.util.Objects;

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


        /** Walls */
        wallItem(ModBlocks.MARBLE_BRICK_WALLS, ModBlocks.MARBLE_BRICKS);
        wallItem(ModBlocks.MARBLE_TILE_WALLS, ModBlocks.MARBLE_TILES);
        wallItem(ModBlocks.LIGHT_GRAY_MARBLE_BRICK_WALLS, ModBlocks.LIGHT_GRAY_MARBLE_BRICKS);
        wallItem(ModBlocks.LIGHT_GRAY_MARBLE_TILE_WALLS, ModBlocks.LIGHT_GRAY_MARBLE_TILES);
        wallItem(ModBlocks.GRAY_MARBLE_BRICK_WALLS, ModBlocks.GRAY_MARBLE_BRICKS);
        wallItem(ModBlocks.GRAY_MARBLE_TILE_WALLS, ModBlocks.GRAY_MARBLE_TILES);
        wallItem(ModBlocks.CHARRED_MARBLE_BRICK_WALLS, ModBlocks.CHARRED_MARBLE_BRICKS);
        wallItem(ModBlocks.CHARRED_MARBLE_TILE_WALLS, ModBlocks.CHARRED_MARBLE_TILES);
        wallItem(ModBlocks.BROWN_MARBLE_BRICK_WALLS, ModBlocks.BROWN_MARBLE_BRICKS);
        wallItem(ModBlocks.BROWN_MARBLE_TILE_WALLS, ModBlocks.BROWN_MARBLE_TILES);
        wallItem(ModBlocks.RED_MARBLE_BRICK_WALLS, ModBlocks.RED_MARBLE_BRICKS);
        wallItem(ModBlocks.RED_MARBLE_TILE_WALLS, ModBlocks.RED_MARBLE_TILES);
        wallItem(ModBlocks.ORANGE_MARBLE_BRICK_WALLS, ModBlocks.ORANGE_MARBLE_BRICKS);
        wallItem(ModBlocks.ORANGE_MARBLE_TILE_WALLS, ModBlocks.ORANGE_MARBLE_TILES);
        wallItem(ModBlocks.YELLOW_MARBLE_BRICK_WALLS, ModBlocks.YELLOW_MARBLE_BRICKS);
        wallItem(ModBlocks.YELLOW_MARBLE_TILE_WALLS, ModBlocks.YELLOW_MARBLE_TILES);
        wallItem(ModBlocks.LIME_MARBLE_BRICK_WALLS, ModBlocks.LIME_MARBLE_BRICKS);
        wallItem(ModBlocks.LIME_MARBLE_TILE_WALLS, ModBlocks.LIME_MARBLE_TILES);
        wallItem(ModBlocks.GREEN_MARBLE_BRICK_WALLS, ModBlocks.GREEN_MARBLE_BRICKS);
        wallItem(ModBlocks.GREEN_MARBLE_TILE_WALLS, ModBlocks.GREEN_MARBLE_TILES);
        wallItem(ModBlocks.CYAN_MARBLE_BRICK_WALLS, ModBlocks.CYAN_MARBLE_BRICKS);
        wallItem(ModBlocks.CYAN_MARBLE_TILE_WALLS, ModBlocks.CYAN_MARBLE_TILES);
        wallItem(ModBlocks.LIGHT_BLUE_MARBLE_BRICK_WALLS, ModBlocks.LIGHT_BLUE_MARBLE_BRICKS);
        wallItem(ModBlocks.LIGHT_BLUE_MARBLE_TILE_WALLS, ModBlocks.LIGHT_BLUE_MARBLE_TILES);
        wallItem(ModBlocks.BLUE_MARBLE_BRICK_WALLS, ModBlocks.BLUE_MARBLE_BRICKS);
        wallItem(ModBlocks.BLUE_MARBLE_TILE_WALLS, ModBlocks.BLUE_MARBLE_TILES);
        wallItem(ModBlocks.PURPLE_MARBLE_BRICK_WALLS, ModBlocks.PURPLE_MARBLE_BRICKS);
        wallItem(ModBlocks.PURPLE_MARBLE_TILE_WALLS, ModBlocks.PURPLE_MARBLE_TILES);
        wallItem(ModBlocks.MAGENTA_MARBLE_BRICK_WALLS, ModBlocks.MAGENTA_MARBLE_BRICKS);
        wallItem(ModBlocks.MAGENTA_MARBLE_TILE_WALLS, ModBlocks.MAGENTA_MARBLE_TILES);
        wallItem(ModBlocks.PINK_MARBLE_BRICK_WALLS, ModBlocks.PINK_MARBLE_BRICKS);
        wallItem(ModBlocks.PINK_MARBLE_TILE_WALLS, ModBlocks.PINK_MARBLE_TILES);





        //Artifacts
        basicItem(ModItems.IRON_SPATIAL_RING.get());
        basicItem(ModItems.GOLD_SPATIAL_RING.get());
        basicItem(ModItems.DIAMOND_SPATIAL_RING.get());
        basicItem(ModItems.NETHERITE_SPATIAL_RING.get());
        basicItem(ModItems.JADE_SPATIAL_RING.get());
        basicItem(ModItems.SPIRITUAL_STONE_SPATIAL_RING.get());
        basicItem(ModItems.REPAIR_SLIP.get());
        basicItem(ModItems.ENDER_POUCH.get());



        //Items
        basicItem(ModItems.JADE.get());
        basicItem(ModItems.SPIRITUAL_STONE.get());
        basicItem(ModItems.JADE_SLIP.get());
        basicItem(ModItems.JADE_NUGGET.get());
        //basicItem(ModItems.RAW_JADE.get());

        //Drops
        basicItem(ModItems.LIVING_CORE.get());
        basicItem(ModItems.UNDEAD_CORE.get());



        //Spiritual Fires
        handheldItem(ModItems.CRIMSON_LOTUS_FLAME.get());



        //Not Used For Anything in mod except being icons etc...
        basicItem(ModItems.ASCENSION_ICON.get());

        //Manuals
        manual(ModTechniques.PURE_FIRE_TECHNIQUE.manual.get());
        manual(ModTechniques.PURE_WATER_TECHNIQUE.manual.get());
        manual(ModTechniques.PURE_SWORD_INTENT.manual.get());
        manual(ModTechniques.PURE_FIST_INTENT.manual.get());
        manual(ModTechniques.DIVINE_PHOENIX_TECHNIQUE.manual.get());
        manual(ModTechniques.VOID_SWALLOWING_TECHNIQUE.manual.get());

        //Tablet Of Destructions
        tablet(ModItems.TABLET_OF_DESTRUCTION_HUMAN.get());
        tablet(ModItems.TABLET_OF_DESTRUCTION_EARTH.get());
        tablet(ModItems.TABLET_OF_DESTRUCTION_HEAVEN.get());

        //Pills
        basicItem(ModItems.REGENERATION_PILL.get());
        basicItem(ModItems.CLEANSING_PILL.get());
        basicItem(ModItems.FASTING_PILL_T1.get());
        basicItem(ModItems.FASTING_PILL_T2.get());
        basicItem(ModItems.FASTING_PILL_T3.get());



        //herbs
        basicItem(ModItems.GOLDEN_SUN_LEAF.get());

        basicItem(ModItems.IRONWOOD_SPROUT.get());
        withExistingParent("ironwood_sprout_block", "item/generated")
                .texture("layer0", "ascension:block/ironwood_sprout_block");
        basicItem(ModItems.WHITE_JADE_ORCHID.get());
        withExistingParent("white_jade_orchid_block", "item/generated")
                .texture("layer0", "ascension:block/white_jade_orchid_block");

        //Saplings
        saplingItem(ModBlocks.GOLDEN_PALM_SAPLING);


        //MobEggs
        withExistingParent(ModItems.RAT_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));



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
    public ItemModelBuilder manual(Item item){
        return basicItemWithSharedTexture(item,ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,
                "generic_manual_texture"
        ));
    }
    public ItemModelBuilder tablet(Item item){
        return basicItemWithSharedTexture(item,ResourceLocation.fromNamespaceAndPath(
                AscensionCraft.MOD_ID,
                "tablet_of_destruction"
        ));
    }
    public ItemModelBuilder basicItemWithSharedTexture(Item item,ResourceLocation texture){
        return basicItemWithSharedTexture(
                Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)),
                texture
        );
    }
    public ItemModelBuilder basicItemWithSharedTexture(ResourceLocation item,ResourceLocation texture) {
        return (this.getBuilder(item.toString())).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), "item/" + texture.getPath()));
    }

    // New method for tinted ingot models
    /*public ItemModelBuilder tintedIngotItem(Item item, int argbColor) {
        String itemName = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)).getPath();
        String hexColor = String.format("%08X", argbColor); // Convert ARGB to hex string

        return getBuilder(itemName)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .customLoader((builder, helper) -> builder
                        .custom("neoforge:item_layers")
                        .end()
                        .element()
                        .texture("layer0", ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "item/ingot_template"))
                        .color(0, hexColor)
                        .end());
    }*/

}
