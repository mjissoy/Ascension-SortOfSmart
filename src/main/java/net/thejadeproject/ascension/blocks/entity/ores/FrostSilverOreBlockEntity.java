package net.thejadeproject.ascension.blocks.entity.ores;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.thejadeproject.ascension.blocks.entity.ModBlockEntities;

import java.util.List;

public class FrostSilverOreBlockEntity extends BlockEntity {
    private static final double FREEZE_RANGE = 10.0;
    public FrostSilverOreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FROST_SILVER_ORE_BE.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos) {
        List<Player> players = level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(FREEZE_RANGE));
        for (Player player : players) {
            player.setTicksFrozen(300);
        }
    }
}
