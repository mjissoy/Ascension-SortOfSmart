package net.thejadeproject.ascension.events.ingameEvents;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class BlockBreakEvents {

    @SubscribeEvent
    public static void onStoneBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null && event.getState().is(Blocks.STONE)) {
            if (player.getMainHandItem().isEmpty()) { // Bare hands
                int stoneBreaks = player.getPersistentData().getInt("ascension_stone_breaks");
                stoneBreaks++;
                player.getPersistentData().putInt("ascension_stone_breaks", stoneBreaks);
            }
        }
    }
}