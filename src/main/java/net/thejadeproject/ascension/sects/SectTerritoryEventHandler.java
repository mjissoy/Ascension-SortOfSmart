package net.thejadeproject.ascension.sects;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDestroyBlockEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.thejadeproject.ascension.AscensionCraft;

public class SectTerritoryEventHandler {

    // Helper method to check if an entity can break/place in a chunk
    private boolean canEntityInteractInChunk(Level level, Entity entity, BlockPos pos) {
        if (entity == null) return false;

        SectManager manager = AscensionCraft.getSectManager(level.getServer());
        if (manager == null) return true; // No manager, allow by default

        ChunkPos chunkPos = new ChunkPos(pos);
        long chunkLong = chunkPos.toLong();

        // Check if chunk is claimed by any sect
        if (!manager.isChunkClaimed(chunkLong)) {
            return true; // Not claimed, allow
        }

        Sect claimingSect = manager.getSectByChunk(chunkLong);
        if (claimingSect == null) return true;

        // If entity is a player, check permissions
        if (entity instanceof ServerPlayer player) {
            Sect playerSect = manager.getPlayerSect(player.getUUID());

            if (playerSect == null) {
                // Player not in a sect, cannot interact in claimed territory
                return false;
            }

            if (claimingSect == playerSect) {
                // Same sect, allow for normal interactions (but NOT explosions)
                return true;
            }

            // Different sect, check if enemy and has more power
            if (playerSect.isEnemy(claimingSect.getName()) &&
                    playerSect.getPower() > claimingSect.getPower()) {
                return true; // Enemy with more power, allow overclaiming
            }

            return false;
        }

        // Non-player entity (Wither, creeper, etc.) - NOT allowed in claimed territory
        return false;
    }

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

        Entity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer player)) return;

        SectManager manager = AscensionCraft.getSectManager(((Level) event.getLevel()).getServer());
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

        // Get the explosion position using center()
        BlockPos explosionPos = new BlockPos(
                (int) event.getExplosion().center().x,
                (int) event.getExplosion().center().y,
                (int) event.getExplosion().center().z
        );
        ChunkPos explosionChunk = new ChunkPos(explosionPos);

        // Check if the explosion is in a claimed chunk
        if (manager.isChunkClaimed(explosionChunk.toLong())) {
            Entity source = event.getExplosion().getDirectSourceEntity();

            // Players cannot cause explosions in ANY claimed territory - including their own
            if (source instanceof ServerPlayer player) {
                Sect playerSect = manager.getPlayerSect(player.getUUID());
                Sect claimingSect = manager.getSectByChunk(explosionChunk.toLong());

                if (claimingSect != null) {
                    // NO explosions allowed for players in claimed territory, even their own
                    event.setCanceled(true);
                    player.sendSystemMessage(Component.literal(
                            "§cExplosions are disabled in all claimed territory! This area is claimed by §e" +
                                    claimingSect.getName() + "§c."
                    ));
                    return;
                }
            }

            // Non-player explosions are also not allowed in claimed territory
            event.setCanceled(true);

            // Optional: Log non-player explosions being blocked
            if (source != null) {
                System.out.println("Non-player explosion prevented in claimed territory at " + explosionPos);
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

        // Get the explosion and source entity
        var explosion = event.getExplosion();
        Entity source = explosion.getDirectSourceEntity();

        // Check each affected block individually
        var affectedBlocks = event.getAffectedBlocks();
        var blocksToRemove = new java.util.ArrayList<BlockPos>();

        for (BlockPos pos : affectedBlocks) {
            // Check if this specific block is in a claimed chunk
            ChunkPos blockChunk = new ChunkPos(pos);
            if (manager.isChunkClaimed(blockChunk.toLong())) {
                Sect claimingSect = manager.getSectByChunk(blockChunk.toLong());

                // Check if the source has permission to affect this block
                boolean hasPermission = false;

                if (source instanceof ServerPlayer player) {
                    Sect playerSect = manager.getPlayerSect(player.getUUID());

                    if (playerSect != null && claimingSect != null) {
                        // NO explosions allowed in ANY claimed territory
                        hasPermission = false;
                    }
                }

                // If no permission, mark this block for removal
                if (!hasPermission) {
                    blocksToRemove.add(pos);
                }
            }
        }

        // Remove blocks that shouldn't be affected
        affectedBlocks.removeAll(blocksToRemove);

        // Also check affected entities
        var affectedEntities = event.getAffectedEntities();
        var entitiesToRemove = new java.util.ArrayList<Entity>();

        for (Entity entity : affectedEntities) {
            if (entity instanceof Player) continue; // Don't remove player damage

            BlockPos entityPos = entity.blockPosition();
            ChunkPos entityChunk = new ChunkPos(entityPos);

            if (manager.isChunkClaimed(entityChunk.toLong())) {
                // Non-player entities in claimed territory shouldn't be damaged by explosions
                boolean hasPermission = false;

                if (source instanceof ServerPlayer player) {
                    Sect playerSect = manager.getPlayerSect(player.getUUID());
                    Sect claimingSect = manager.getSectByChunk(entityChunk.toLong());

                    if (playerSect != null && claimingSect != null) {
                        // NO explosions allowed in ANY claimed territory
                        hasPermission = false;
                    }
                }

                if (!hasPermission) {
                    entitiesToRemove.add(entity);
                }
            }
        }

        // Remove entities that shouldn't be affected
        affectedEntities.removeAll(entitiesToRemove);

        // Optional: Send message if explosion was blocked
        if (!blocksToRemove.isEmpty() && source instanceof ServerPlayer player) {
            player.sendSystemMessage(Component.literal(
                    "§cSome blocks were protected from the explosion in claimed territory!"
            ));
        }
    }

    // FIX FOR WITHER: Handle LivingDestroyBlockEvent for Wither and other special entities
    @SubscribeEvent
    public void onLivingDestroyBlock(LivingDestroyBlockEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        LivingEntity entity = event.getEntity();
        BlockPos pos = event.getPos();
        Level level = (Level) entity.level();

        // Only handle specific entities that can break blocks
        if (entity instanceof WitherBoss ||
                entity instanceof net.minecraft.world.entity.monster.EnderMan ||
                entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragon) {

            SectManager manager = AscensionCraft.getSectManager(level.getServer());
            if (manager == null) return;

            ChunkPos chunkPos = new ChunkPos(pos);
            long chunkLong = chunkPos.toLong();

            // Check if chunk is claimed
            if (manager.isChunkClaimed(chunkLong)) {
                Sect claimingSect = manager.getSectByChunk(chunkLong);
                if (claimingSect != null) {
                    // Wither and other special entities cannot break blocks in claimed territory
                    event.setCanceled(true);

                    // Optional: Log or notify
                    System.out.println("Wither block breaking prevented in " + claimingSect.getName() + "'s territory at " + pos);
                }
            }
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

    // NEW: Handle creeper explosions specifically
    @SubscribeEvent
    public void onCreeperExplosion(ExplosionEvent.Detonate event) {
        // This is already handled by onExplosionDetonate, but we can add specific creeper logic
        Level level = (Level) event.getLevel();

        if (level.isClientSide()) return;

        var explosion = event.getExplosion();
        Entity source = explosion.getDirectSourceEntity();

        // Check if it's a creeper explosion
        if (source instanceof Creeper) {
            // Additional creeper-specific logic if needed
            // The main logic is already in onExplosionDetonate
        }
    }
}