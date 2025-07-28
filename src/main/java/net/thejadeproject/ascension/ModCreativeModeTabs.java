package net.thejadeproject.ascension;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.blocks.ModBlocks;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.progression.techniques.ModTechniques;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AscensionCraft.MOD_ID);

    public static final Supplier<CreativeModeTab> ASCENSION_ITEMS_TAB = CREATIVE_MODE_TAB.register("ascension_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.JADE.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_blocks_tab"))
                    .title(Component.translatable("creativetab.ascension.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.SPIRITUAL_STONE);
                        output.accept(ModItems.RAW_JADE);
                        output.accept(ModItems.JADE_NUGGET);
                        output.accept(ModItems.JADE);
                        output.accept(ModItems.UNDEAD_CORE);
                        output.accept(ModItems.LIVING_CORE);
                        output.accept(ModItems.REGENERATION_PILL);
                    }).build());

    public static final Supplier<CreativeModeTab> ASCENSION_ARTIFACTS_TAB = CREATIVE_MODE_TAB.register("ascension_artifacts_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SPIRITUAL_STONE_SPATIAL_RING.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AscensionCraft.MOD_ID, "ascension_items_tab"))
                    .title(Component.translatable("creativetab.ascension.artifacts"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.JADE_SLIP);
                        output.accept(ModItems.IRON_SPATIAL_RING);
                        output.accept(ModItems.GOLD_SPATIAL_RING);
                        output.accept(ModItems.DIAMOND_SPATIAL_RING);
                        output.accept(ModItems.NETHERITE_SPATIAL_RING);
                        output.accept(ModItems.JADE_SPATIAL_RING);
                        output.accept(ModItems.SPIRITUAL_STONE_SPATIAL_RING);
                    }).build());

    public static final Supplier<CreativeModeTab> ASCENSION_BLOCKS_TAB = CREATIVE_MODE_TAB.register("ascension_blocks_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.JADE_ORE.get()))
                    .title(Component.translatable("creativetab.ascension.blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.GOLDEN_PALM_LOG);
                        output.accept(ModBlocks.GOLDEN_PALM_WOOD);

                        output.accept(ModBlocks.STRIPPED_GOLDEN_PALM_LOG);
                        output.accept(ModBlocks.STRIPPED_GOLDEN_PALM_WOOD);

                        output.accept(ModBlocks.GOLDEN_PALM_PLANKS);
                        output.accept(ModBlocks.GOLDEN_PALM_SAPLING);

                        output.accept(ModBlocks.GOLDEN_PALM_LEAVES);
                        output.accept(ModBlocks.GOLDEN_PALM_STAIRS);
                        output.accept(ModBlocks.GOLDEN_PALM_SLAB);
                        output.accept(ModBlocks.GOLDEN_PALM_PRESSURE_PLATE);
                        output.accept(ModBlocks.GOLDEN_PALM_BUTTON);
                        output.accept(ModBlocks.GOLDEN_PALM_FENCE);
                        output.accept(ModBlocks.GOLDEN_PALM_FENCE_GATE);
                        output.accept(ModBlocks.GOLDEN_PALM_DOOR);
                        output.accept(ModBlocks.GOLDEN_PALM_TRAPDOOR);

                        output.accept(ModBlocks.RAW_MARBLE);
                        output.accept(ModBlocks.POLISHED_MARBLE);
                        output.accept(ModBlocks.MARBLE_BRICKS);
                        output.accept(ModBlocks.CRACKED_MARBLE_BRICKS);
                        output.accept(ModBlocks.MOSSY_MARBLE_BRICKS);
                        output.accept(ModBlocks.MARBLE_CHISELED);
                        output.accept(ModBlocks.MARBLE_TILES);
                        output.accept(ModBlocks.MARBLE_BRICK_STAIRS);
                        output.accept(ModBlocks.MARBLE_TILE_STAIRS);
                        output.accept(ModBlocks.MARBLE_BRICK_SLABS);
                        output.accept(ModBlocks.MARBLE_TILE_SLABS);
                        output.accept(ModBlocks.MARBLE_BRICK_WALLS);
                        output.accept(ModBlocks.MARBLE_TILE_WALLS);
                        output.accept(ModBlocks.CHARRED_MARBLE);
                        output.accept(ModBlocks.POLISHED_BURNED_MARBLE);
                        output.accept(ModBlocks.CHARRED_MARBLE_BRICKS);
                        output.accept(ModBlocks.CHARRED_MARBLE_CHISELED);
                        output.accept(ModBlocks.CHARRED_MARBLE_TILES);
                        output.accept(ModBlocks.CHARRED_MARBLE_BRICK_STAIRS);
                        output.accept(ModBlocks.CHARRED_MARBLE_TILE_STAIRS);
                        output.accept(ModBlocks.CHARRED_MARBLE_BRICK_SLABS);
                        output.accept(ModBlocks.CHARRED_MARBLE_TILE_SLABS);
                        output.accept(ModBlocks.BURNED_MARBLE_BRICK_WALLS);
                        output.accept(ModBlocks.BURNED_MARBLE_TILE_WALLS);




                        output.accept(ModBlocks.JADE_ORE);
                        output.accept(ModBlocks.JADE_BLOCK);
                        output.accept(ModBlocks.SPIRITUAL_STONE_CLUSTER);
                        output.accept(ModBlocks.PILL_CAULDRON_HUMAN_LOW);


                    }).build());

    public static final Supplier<CreativeModeTab> ASCENSION_HERBS_TAB = CREATIVE_MODE_TAB.register("ascension_herbs_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.GOLDEN_SUN_LEAF.get()))
                    .title(Component.translatable("creativetab.ascension.herbs"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.CRIMSON_LOTUS_FLAME);
                        output.accept(ModItems.GOLDEN_SUN_LEAF);
                        output.accept(ModItems.IRONWOOD_SPROUT);
                        output.accept(ModItems.WHITE_JADE_ORCHID);


                    }).build());

    public static final Supplier<CreativeModeTab> ASCENSION_TECHNIQUE_MANUALS = CREATIVE_MODE_TAB.register("ascension_technique_manuals",
            ()->CreativeModeTab.builder().icon(()-> new ItemStack(ModTechniques.PURE_FIRE_TECHNIQUE.manual.get()))
                    .title(Component.translatable("creativetab.ascension.manuals"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModTechniques.PURE_FIRE_TECHNIQUE.manual);
                        output.accept(ModTechniques.PURE_WATER_TECHNIQUE.manual);
                        output.accept(ModTechniques.PURE_SWORD_INTENT.manual);
                        output.accept(ModTechniques.PURE_FIST_INTENT.manual);
                        output.accept(ModTechniques.DIVINE_PHOENIX_TECHNIQUE.manual);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
