package net.thejadeproject.ascension.progression.skills.active_skills.wood_dao;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RootwardensCallActiveSkill extends AbstractActiveSkill {

    // Store wood blocks to be harvested - separate maps for client and server
    private static final Map<UUID, WoodHarvestData> CLIENT_WOOD_DATA = new ConcurrentHashMap<>();
    private static final Map<UUID, WoodHarvestData> SERVER_WOOD_DATA = new ConcurrentHashMap<>();

    // Base values for scaling
    private static final int BASE_RADIUS = 6;
    private static final int BASE_COOLDOWN_TICKS = 20 * 30; // 30 seconds base cooldown
    private static final int RADIUS_INCREASE_PER_REALM = 2; // +2 blocks per major realm
    private static final int COOLDOWN_REDUCTION_PER_REALM_TICKS = 40; // -2 seconds (40 ticks) per major realm
    private static final int MIN_COOLDOWN_TICKS = 20 * 5; // Minimum 5 second cooldown

    // Particle settings
    private static final Vector3f GREEN_PARTICLE_COLOR = new Vector3f(0.0f, 1.0f, 0.0f); // Green color
    private static final float PARTICLE_SCALE = 1.5f; // Size of particles
    private static final int PARTICLE_DURATION_TICKS = 30; // 1.5 seconds (20 ticks per second * 1.5 = 30)

    private static class WoodHarvestData {
        final Set<BlockPos> allWoodBlocks = new HashSet<>();
        final List<Set<BlockPos>> connectedComponents = new ArrayList<>();
        final long expirationTime;
        final UUID playerId;

        WoodHarvestData(UUID playerId, int durationSeconds) {
            this.playerId = playerId;
            this.expirationTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        }
    }

    public RootwardensCallActiveSkill() {
        super(Component.translatable("ascension.skill.active.rootwardens_call"));
        this.path = "ascension:essence";
        this.qiCost = 25.0;
    }

    @Override
    public boolean isPrimarySkill() {
        return true;
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public int getCooldown() {
        return 20 * 30; // 30 second base cooldown
    }

    /**
     * Get adjusted cooldown based on player's major realm
     */
    public int getAdjustedCooldown(Player player) {
        int majorRealm = getPlayerMajorRealm(player);
        int adjustedTicks = BASE_COOLDOWN_TICKS - (majorRealm * COOLDOWN_REDUCTION_PER_REALM_TICKS);
        return Math.max(adjustedTicks, MIN_COOLDOWN_TICKS);
    }

    /**
     * Get adjusted radius based on player's major realm
     */
    public int getAdjustedRadius(Player player) {
        int majorRealm = getPlayerMajorRealm(player);
        return BASE_RADIUS + (majorRealm * RADIUS_INCREASE_PER_REALM);
    }

    /**
     * Get player's major realm for this skill's path
     */
    private int getPlayerMajorRealm(Player player) {
        try {
            var playerData = player.getData(ModAttachments.PLAYER_DATA);
            var cultivationData = playerData.getCultivationData();
            var pathData = cultivationData.getPathData(this.path);
            return pathData != null ? pathData.majorRealm : 0;
        } catch (Exception e) {
            AscensionCraft.LOGGER.warn("Failed to get major realm for player {}: {}", player.getName().getString(), e.getMessage());
            return 0;
        }
    }

    @Override
    public int maxCastingTicks() {
        return 100; // 5 seconds (20 ticks per second * 5 = 100)
    }


    @Override
    public void cast(int castingTicksElapsed, Level level, Player player) {
        // This is called on BOTH client and server during the cast

        if (level.isClientSide()) {
            // CLIENT: Handle rendering data
            if (castingTicksElapsed == 0) {
                // First tick on client - find and display wood blocks
                findAndDisplayWoodBlocks(level, player);
                level.playSound(null, player.blockPosition(), SoundEvents.GROWING_PLANT_CROP,
                        SoundSource.PLAYERS, 0.8F, 0.9F);
            } else if (castingTicksElapsed >= maxCastingTicks() - 1) {
                // Last tick on client - clear data
                CLIENT_WOOD_DATA.remove(player.getUUID());
                level.playSound(null, player.blockPosition(), SoundEvents.WOOD_BREAK,
                        SoundSource.PLAYERS, 1.0F, 0.8F);
            }
        } else {
            // SERVER: Handle actual harvesting
            if (castingTicksElapsed >= maxCastingTicks() - 1) {
                // Last tick on server - harvest wood blocks
                harvestWoodBlocks(level, player);
                SERVER_WOOD_DATA.remove(player.getUUID());
                level.playSound(null, player.blockPosition(), SoundEvents.WOOD_BREAK,
                        SoundSource.PLAYERS, 1.0F, 0.8F);
                level.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP,
                        SoundSource.PLAYERS, 0.5F, 1.2F);
            }
        }
    }

    @Override
    public void onPreCast() {
        // Called before cast starts - we can use this to initialize
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        UUID playerId = player.getUUID();
        CLIENT_WOOD_DATA.remove(playerId);
        SERVER_WOOD_DATA.remove(playerId);
    }

    private void findAndDisplayWoodBlocks(Level level, Player player) {
        UUID playerId = player.getUUID();
        BlockPos playerPos = player.blockPosition();

        // Get adjusted radius based on player's major realm
        int radius = getAdjustedRadius(player);

        WoodHarvestData data = new WoodHarvestData(playerId, 30);
        Set<BlockPos> allWoodPositions = new HashSet<>();

        // Find all wood blocks in radius
        int woodCount = 0;
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = playerPos.offset(x, y, z);

                    // Check if chunk is loaded
                    if (level.isLoaded(checkPos)) {
                        BlockState state = level.getBlockState(checkPos);

                        if (isWoodBlock(state)) {
                            allWoodPositions.add(checkPos.immutable());
                            woodCount++;
                        }
                    }
                }
            }
        }

        data.allWoodBlocks.addAll(allWoodPositions);

        // Find connected components for rendering
        Set<BlockPos> visited = new HashSet<>();
        for (BlockPos woodPos : allWoodPositions) {
            if (!visited.contains(woodPos)) {
                Set<BlockPos> component = new HashSet<>();
                floodFillWood(woodPos, component, visited, allWoodPositions);
                if (!component.isEmpty()) {
                    data.connectedComponents.add(component);
                }
            }
        }

        // Store in client map
        CLIENT_WOOD_DATA.put(playerId, data);
    }

    private void floodFillWood(BlockPos start, Set<BlockPos> component,
                               Set<BlockPos> visited, Set<BlockPos> allWoodPositions) {
        Stack<BlockPos> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            BlockPos current = stack.pop();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);
            component.add(current);

            // Check all 6 directions
            for (Direction direction : Direction.values()) {
                BlockPos neighbor = current.relative(direction);
                if (allWoodPositions.contains(neighbor) && !visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
    }

    private boolean isWoodBlock(BlockState state) {
        if (state == null || state.isAir()) {
            return false;
        }

        // First, check if the block is in the logs or planks tag
        try {
            if (state.is(BlockTags.LOGS)) {
                return true;
            }
        } catch (Exception e) {
            // Tag system might not be ready, fall through to name check
        }

        // Fallback: check by block name
        Block block = state.getBlock();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        if (blockId != null) {
            String path = blockId.getPath().toLowerCase();

            // Check for wood-related terms
            boolean isWoodLike = path.contains("log") ||
                    path.contains("wood") ||
                    path.contains("plank") ||
                    path.contains("stem") ||
                    path.contains("hyphae");

            // Exclude non-wood blocks
            boolean isExcluded = path.contains("leaf") ||
                    path.contains("sapling") ||
                    path.contains("fungus") ||
                    path.contains("nylium") ||
                    path.contains("wart") ||
                    path.contains("shroomlight") ||
                    path.contains("mushroom") ||
                    path.contains("vine");

            return isWoodLike && !isExcluded;
        }

        return false;
    }

    private void harvestWoodBlocks(Level level, Player player) {
        UUID playerId = player.getUUID();
        BlockPos playerPos = player.blockPosition();

        // Get adjusted radius based on player's major realm
        int radius = getAdjustedRadius(player);

        Set<BlockPos> allWoodPositions = new HashSet<>();

        // Find all wood blocks in radius
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = playerPos.offset(x, y, z);

                    if (level.isLoaded(checkPos)) {
                        BlockState state = level.getBlockState(checkPos);

                        if (isWoodBlock(state)) {
                            allWoodPositions.add(checkPos.immutable());
                        }
                    }
                }
            }
        }

        ServerLevel serverLevel = (ServerLevel) level;
        int blocksBroken = 0;
        int itemsDropped = 0;

        // Create green dust particle options for breaking wood
        ParticleOptions greenParticle = new DustParticleOptions(GREEN_PARTICLE_COLOR, PARTICLE_SCALE);

        for (BlockPos woodPos : allWoodPositions) {
            if (level.isLoaded(woodPos)) {
                BlockState state = level.getBlockState(woodPos);

                if (isWoodBlock(state)) {
                    // Create LootParams
                    LootParams.Builder lootParamsBuilder = new LootParams.Builder(serverLevel)
                            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(woodPos))
                            .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                            .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
                            .withParameter(LootContextParams.BLOCK_STATE, state);

                    // Get drops
                    List<ItemStack> drops = state.getDrops(lootParamsBuilder);

                    // If no drops, try alternative method
                    if (drops.isEmpty()) {
                        drops = state.getBlock().getDrops(state, serverLevel, woodPos, null, player, ItemStack.EMPTY);
                    }

                    // Remove block if game rules allow
                    if (!player.isCreative() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
                        // Spawn green particle at the center of the block
                        spawnGreenParticles(serverLevel, woodPos, greenParticle);

                        // Set to air
                        level.setBlock(woodPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                        blocksBroken++;

                        // Spawn drops at player's position
                        for (ItemStack drop : drops) {
                            if (!drop.isEmpty()) {
                                ItemEntity itemEntity = new ItemEntity(level,
                                        player.getX(),
                                        player.getY() + 1.0,
                                        player.getZ(),
                                        drop.copy());
                                itemEntity.setDefaultPickUpDelay();
                                level.addFreshEntity(itemEntity);
                                itemsDropped++;
                            }
                        }
                    } else if (player.isCreative()) {
                        // In creative, just remove the block
                        spawnGreenParticles(serverLevel, woodPos, greenParticle);
                        level.setBlock(woodPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                        blocksBroken++;
                    }

                    // Play break sound
                    level.playSound(null, woodPos, state.getSoundType().getBreakSound(),
                            SoundSource.BLOCKS, 1.0F, 0.8F);
                }
            }
        }
    }

    /**
     * Spawn green particles at the center of a block position
     */
    private void spawnGreenParticles(ServerLevel level, BlockPos pos, ParticleOptions particle) {
        // Center of the block
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        // Spawn multiple particles in a small area for better visibility
        for (int i = 0; i < 8; i++) {
            // Random offset within the block
            double offsetX = (level.random.nextDouble() - 0.5) * 0.8;
            double offsetY = (level.random.nextDouble() - 0.5) * 0.8;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.8;

            // Spawn particle with slight random velocity
            level.sendParticles(particle,
                    centerX + offsetX,
                    centerY + offsetY,
                    centerZ + offsetZ,
                    1, // count
                    0.0, 0.0, 0.0, // delta (velocity)
                    0.1); // speed
        }

        // Also spawn some larger particles at the exact center
        for (int i = 0; i < 3; i++) {
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    centerX,
                    centerY,
                    centerZ,
                    1,
                    0.0, 0.0, 0.0,
                    0.2);
        }
    }

    public static WoodHarvestData getWoodDataForPlayer(UUID playerId) {
        WoodHarvestData data = CLIENT_WOOD_DATA.get(playerId);
        if (data != null && System.currentTimeMillis() > data.expirationTime) {
            CLIENT_WOOD_DATA.remove(playerId);
            return null;
        }
        return data;
    }

    public static void clearPlayerData(UUID playerId) {
        CLIENT_WOOD_DATA.remove(playerId);
        SERVER_WOOD_DATA.remove(playerId);
    }
}