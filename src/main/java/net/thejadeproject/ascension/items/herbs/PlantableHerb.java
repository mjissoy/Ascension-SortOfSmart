package net.thejadeproject.ascension.items.herbs;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.thejadeproject.ascension.blocks.custom.CustomHerbs;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * Plantable herb item. Handles custom placement on specific ground blocks
 * and shows Quality / Age tooltips when stamped by the crop system.
 */
public class PlantableHerb extends BlockItem {

    public PlantableHerb(Block block, Properties properties) {
        super(block, properties);
    }

    // ── Tooltip ───────────────────────────────────────────────────

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        HerbQuality.appendHerbTooltip(stack, tooltip);
    }

    // ── Planting ──────────────────────────────────────────────────

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level    = context.getLevel();
        BlockPos pos   = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player  = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        Set<Block> allowed = getRequiredGroundBlocks();
        if (allowed == null || allowed.stream().noneMatch(state::is)) {
            return InteractionResult.FAIL;
        }

        BlockPos above = pos.above();
        if (level.isEmptyBlock(above)
                && this.getBlock().defaultBlockState().canSurvive(level, above)) {
            level.setBlock(above, this.getBlock().defaultBlockState(), 3);
            level.playSound(player, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1f, 1f);
            if (player != null && !player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.FAIL;
    }

    @Override
    @Nullable
    public BlockPlaceContext updatePlacementContext(BlockPlaceContext context) { return null; }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) { return false; }

    private Set<Block> getRequiredGroundBlocks() {
        if (this.getBlock() instanceof CustomHerbs ch) return ch.getBlocksToSurviveOn();
        return null;
    }
}