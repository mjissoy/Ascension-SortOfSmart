package net.thejadeproject.ascension.items.herbs;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.ascension.blocks.custom.CustomHerbs;

import javax.annotation.Nullable;
import java.util.Set;

public class PlantableHerb extends BlockItem {
    public PlantableHerb(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        // Get the set of blocks this herb can be planted on from the CustomHerbs block
        Set<Block> blocksToPlantOn = getRequiredGroundBlocks();

        // Only allow placement if the clicked block is one of the allowed blocks
        if (blocksToPlantOn != null && blocksToPlantOn.stream().anyMatch(block -> state.is(block))) {
            BlockPos abovePos = pos.above();

            // Check if the block above is air and can support the crop
            if (level.isEmptyBlock(abovePos) &&
                    this.getBlock().defaultBlockState().canSurvive(level, abovePos)) {

                // Place the crop block
                level.setBlock(abovePos, this.getBlock().defaultBlockState(), 3);

                // Play planting sound
                level.playSound(player, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);

                // Consume one item if not in creative mode
                if (player != null && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                return InteractionResult.sidedSuccess(level.isClientSide());
            }
        }

        // If conditions aren't met, don't allow placement
        return InteractionResult.FAIL;
    }

    @Override
    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) {
        // Override to prevent default placement behavior
        return null;
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        // Override to prevent default placement behavior
        // We handle placement manually in useOn instead
        return false;
    }

    /**
     * Gets the set of required ground blocks from the associated CustomHerbs block
     */
    private Set<Block> getRequiredGroundBlocks() {
        if (this.getBlock() instanceof CustomHerbs customHerbs) {
            return customHerbs.getBlocksToSurviveOn();
        }
        return null;
    }
}