package net.thejadeproject.ascension.sects;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.thejadeproject.ascension.AscensionCraft;

public class SectTerritoryEventHandler {

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        // Only handle on server side
        if (event.getLevel().isClientSide()) return;

        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager == null) return;

        ChunkPos chunkPos = new ChunkPos(event.getPos());
        long chunkLong = chunkPos.toLong();

        // Check if chunk is claimed by any sect
        if (manager.isChunkClaimed(chunkLong)) {
            Sect playerSect = manager.getPlayerSect(player.getUUID());
            Sect claimingSect = manager.getSectByChunk(chunkLong);

            if (claimingSect != null && claimingSect != playerSect) {
                if (playerSect == null || !playerSect.isEnemy(claimingSect.getName()) ||
                        playerSect.getPower() <= claimingSect.getPower()) {
                    event.setCanceled(true);
                    player.sendSystemMessage(Component.literal(
                            "§cThis territory is claimed by §e" + claimingSect.getName() + "§c! " +
                                    "Mark them as enemy and have more power to interact here."
                    ));
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        // Only handle on server side
        if (event.getLevel().isClientSide()) return;

        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager == null) return;

        ChunkPos chunkPos = new ChunkPos(event.getPos());
        long chunkLong = chunkPos.toLong();

        // Check if chunk is claimed by any sect
        if (manager.isChunkClaimed(chunkLong)) {
            Sect playerSect = manager.getPlayerSect(player.getUUID());
            Sect claimingSect = manager.getSectByChunk(chunkLong);

            if (claimingSect != null && claimingSect != playerSect) {
                if (playerSect == null || !playerSect.isEnemy(claimingSect.getName()) ||
                        playerSect.getPower() <= claimingSect.getPower()) {
                    event.setCanceled(true);
                    player.sendSystemMessage(Component.literal(
                            "§cThis territory is claimed by §e" + claimingSect.getName() + "§c! " +
                                    "Mark them as enemy and have more power to interact here."
                    ));
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void onChestOpen(PlayerInteractEvent.RightClickBlock event) {
        // Only handle on server side
        if (event.getLevel().isClientSide()) return;

        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Check if the block is a container (chest, furnace, etc.)
        if (event.getLevel().getBlockState(event.getPos()).hasBlockEntity()) {
            SectManager manager = AscensionCraft.getSectManager(player.getServer());
            if (manager == null) return;

            ChunkPos chunkPos = new ChunkPos(event.getPos());
            long chunkLong = chunkPos.toLong();

            // Check if chunk is claimed by any sect
            if (manager.isChunkClaimed(chunkLong)) {
                Sect playerSect = manager.getPlayerSect(player.getUUID());
                Sect claimingSect = manager.getSectByChunk(chunkLong);

                if (claimingSect != null && claimingSect != playerSect) {
                    if (playerSect == null || !playerSect.isEnemy(claimingSect.getName()) ||
                            playerSect.getPower() <= claimingSect.getPower()) {
                        event.setCanceled(true);
                        player.sendSystemMessage(Component.literal(
                                "§cThis territory is claimed by §e" + claimingSect.getName() + "§c! " +
                                        "Mark them as enemy and have more power to access containers here."
                        ));
                        return;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onExplosionStart(ExplosionEvent.Start event) {
        Level level = (Level) event.getLevel();

        // Only handle on server side
        if (level.isClientSide()) return;

        SectManager manager = AscensionCraft.getSectManager(level.getServer());
        if (manager == null) return;

        // Get the explosion position using center() instead of getPosition()
        BlockPos explosionPos = new BlockPos(
                (int) event.getExplosion().center().x,
                (int) event.getExplosion().center().y,
                (int) event.getExplosion().center().z
        );
        ChunkPos explosionChunk = new ChunkPos(explosionPos);

        // Check if the explosion is in a claimed chunk
        if (manager.isChunkClaimed(explosionChunk.toLong())) {
            // Cancel the explosion completely
            event.setCanceled(true);

            // Optional: Send message to nearby players if it was player-caused
            Entity source = event.getExplosion().getDirectSourceEntity();
            if (source instanceof ServerPlayer player) {
                Sect claimingSect = manager.getSectByChunk(explosionChunk.toLong());
                if (claimingSect != null) {
                    player.sendSystemMessage(Component.literal(
                            "§cExplosions are disabled in territory claimed by §e" + claimingSect.getName() + "§c!"
                    ));
                }
            }
        }
    }

    @SubscribeEvent
    public void onExplosionDetonate(ExplosionEvent.Detonate event) {
        Level level = (Level) event.getLevel();

        // Only handle on server side
        if (level.isClientSide()) return;

        SectManager manager = AscensionCraft.getSectManager(level.getServer());
        if (manager == null) return;

        // Get the explosion position using center()
        BlockPos explosionPos = new BlockPos(
                (int) event.getExplosion().center().x,
                (int) event.getExplosion().center().y,
                (int) event.getExplosion().center().z
        );
        ChunkPos explosionChunk = new ChunkPos(explosionPos);

        // Check if the explosion is in a claimed chunk
        if (manager.isChunkClaimed(explosionChunk.toLong())) {
            // Remove all affected blocks from the explosion
            event.getAffectedBlocks().clear();

            // Also clear affected entities to prevent damage
            event.getAffectedEntities().clear();
        }
    }

    @SubscribeEvent
    public void onMobGrief(BlockEvent.EntityPlaceEvent event) {
        // Only handle on server side
        if (event.getLevel().isClientSide()) return;

        // Check if the entity is a mob (not a player)
        if (!(event.getEntity() instanceof Player)) {
            SectManager manager = AscensionCraft.getSectManager(((Level) event.getLevel()).getServer());
            if (manager == null) return;

            ChunkPos chunkPos = new ChunkPos(event.getPos());
            long chunkLong = chunkPos.toLong();

            // Check if chunk is claimed by any sect
            if (manager.isChunkClaimed(chunkLong)) {
                // Cancel mob griefing in claimed territory
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onMobBreak(BlockEvent.BreakEvent event) {
        // Only handle on server side
        if (event.getLevel().isClientSide()) return;

        // Check if the entity is a mob (not a player)
        if (!(event.getPlayer() instanceof Player)) {
            SectManager manager = AscensionCraft.getSectManager(((Level) event.getLevel()).getServer());
            if (manager == null) return;

            ChunkPos chunkPos = new ChunkPos(event.getPos());
            long chunkLong = chunkPos.toLong();

            // Check if chunk is claimed by any sect
            if (manager.isChunkClaimed(chunkLong)) {
                // Cancel mob griefing in claimed territory
                event.setCanceled(true);
            }
        }
    }
}