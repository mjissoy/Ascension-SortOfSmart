package net.thejadeproject.ascension.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
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


    //Ores

    public static final DeferredBlock<Block> JADE_ORE = registerBlock("jade_ore",
            () -> new DropExperienceBlock(UniformInt.of(2, 4),
                    BlockBehaviour.Properties.of()
                .strength(4.5f, 3.5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));
    public static final DeferredBlock<Block> SPIRITUAL_STONE_CLUSTER = registerBlock("spiritual_stone_cluster",
            () -> new DropExperienceBlock(UniformInt.of(2, 4),
                    BlockBehaviour.Properties.of()
                .strength(6.5f, 5.5f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST).noOcclusion().lightLevel(state -> 10)));

    //Other Blocks
    public static final DeferredBlock<Block> JADE_BLOCK = registerBlock("jade_block",
            () -> new Block(BlockBehaviour.Properties.of()
                .strength(5.5f, 4.5f).requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK)));

    public static final DeferredBlock<Block> PILL_CAULDRON_HUMAN_LOW = registerBlock("pill_cauldron_low_human",
            () -> new PillCauldronLowHumanBlock(BlockBehaviour.Properties.of().noOcclusion()));


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

    public static final DeferredBlock<StairBlock> GOLDEN_PALM_STAIRS = registerBlock("golden_palm_stairs",
            () -> new StairBlock(ModBlocks.GOLDEN_PALM_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredBlock<SlabBlock> GOLDEN_PALM_SLAB = registerBlock("golden_palm_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    public static final DeferredBlock<PressurePlateBlock> GOLDEN_PALM_PRESSURE_PLATE = registerBlock("golden_palm_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredBlock<ButtonBlock> GOLDEN_PALM_BUTTON = registerBlock("golden_palm_button",
            () -> new ButtonBlock(BlockSetType.OAK, 20, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON).noCollission()));

    public static final DeferredBlock<FenceBlock> GOLDEN_PALM_FENCE = registerBlock("golden_palm_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredBlock<FenceGateBlock> GOLDEN_PALM_FENCE_GATE = registerBlock("golden_palm_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));

    public static final DeferredBlock<DoorBlock> GOLDEN_PALM_DOOR = registerBlock("golden_palm_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).noOcclusion()));
    public static final DeferredBlock<TrapDoorBlock> GOLDEN_PALM_TRAPDOOR = registerBlock("golden_palm_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).noOcclusion()));






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
            () -> new CustomHerbs(() -> Blocks.GRASS_BLOCK));
    public static final DeferredBlock<Block> IRONWOOD_SPROUT_BLOCK = registerBlock("ironwood_sprout_block",
            () -> new CustomHerbs(() -> Blocks.STONE));
    public static final DeferredBlock<Block> WHITE_JADE_ORCHID_BLOCK = registerBlock("white_jade_orchid_block",
            () -> new CustomHerbs(() -> ModBlocks.JADE_ORE.get()));

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
