package net.thejadeproject.ascension.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.blocks.custom.CustomHerbs;
import net.thejadeproject.ascension.blocks.custom.GoldenPalmSapling;
import net.thejadeproject.ascension.blocks.custom.ModFlammableRotatePillarBlock;
import net.thejadeproject.ascension.blocks.custom.PillCauldronLowHumanBlock;
import net.thejadeproject.ascension.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.thejadeproject.ascension.worldgen.tree.ModTreeGrowers;


import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCK =
            DeferredRegister.createBlocks(AscensionCraft.MOD_ID);

    public static final DeferredBlock<Block> JADE_ORE = registerBlock("jade_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 4),
                    BlockBehaviour.Properties.of()
                .strength(4.5f, 3.5f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));

    public static final DeferredBlock<Block> JADE_BLOCK = registerBlock("jade_block",
            () -> new Block(BlockBehaviour.Properties.of()
                .strength(5.5f, 4.5f).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK)));

    public static final DeferredBlock<Block> PILL_CAULDRON_HUMAN_LOW = registerBlock("pill_cauldron_low_human",
            () -> new PillCauldronLowHumanBlock(BlockBehaviour.Properties.of()));


    //Wood
    public static final DeferredBlock<Block> GOLDEN_PALM_LOG = registerBlock("golden_palm_log",
            () -> new ModFlammableRotatePillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> STRIPPED_GOLDEN_PALM_LOG = registerBlock("stripped_golden_palm_log",
            () -> new ModFlammableRotatePillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredBlock<Block> GOLDEN_PALM_WOOD = registerBlock("golden_palm_wood",
            () -> new ModFlammableRotatePillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> STRIPPED_GOLDEN_PALM_WOOD = registerBlock("stripped_golden_palm_wood",
            () -> new ModFlammableRotatePillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

    public static final DeferredBlock<Block> GOLDEN_PALM_PLANKS = registerBlock("golden_palm_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }
                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 20;
                }
                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 5;
                }
            });
    public static final DeferredBlock<Block> GOLDEN_PALM_LEAVES = registerBlock("golden_palm_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return true;
                }
                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 60;
                }
                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
                    return 30;
                }
            });

    public static final DeferredBlock<Block> GOLDEN_PALM_SAPLING = registerBlock("golden_palm_sapling",
            () -> new GoldenPalmSapling(ModTreeGrowers.GOLDEN_PALM, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING), () -> Blocks.SAND));

    //Herbs
    public static final DeferredBlock<Block> GOLDEN_SUN_LEAF_BLOCK = registerBlock("golden_sun_leaf_block",
            () -> new CustomHerbs());

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCK.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCK.register(eventBus);
    }
}
