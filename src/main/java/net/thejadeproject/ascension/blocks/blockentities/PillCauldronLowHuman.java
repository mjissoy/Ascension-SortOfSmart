package net.thejadeproject.ascension.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.thejadeproject.ascension.guis.blockentity.PillCauldronLowHumanEntity;

public class PillCauldronLowHuman extends Block implements EntityBlock {
    public PillCauldronLowHuman() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(3.5f).requiresCorrectToolForDrops());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PillCauldronLowHumanEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockEntity instanceof PillCauldronLowHumanEntity processor)) {
            return InteractionResult.PASS;
        }

        //Heat Increasing Items
        if (!level.isClientSide) {
            int heatIncrease = 0;
            if (heldItem.is(Items.BLAZE_POWDER)) {
                heatIncrease = 100;
                heldItem.shrink(1);
            } else if (heldItem.is(Items.COAL)) {
                heatIncrease = 50;
                heldItem.shrink(50);
            }
            if (heatIncrease > 0) {
                processor.addHeat(heatIncrease);
                return InteractionResult.CONSUME;
            }
            if (player instanceof ServerPlayer serverPlayer) {
                NetworkHooks.openScreen(serverPlayer, processor, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
