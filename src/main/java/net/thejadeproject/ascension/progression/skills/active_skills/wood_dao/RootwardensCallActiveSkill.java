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
import net.thejadeproject.ascension.progression.skills.data.CastType;
import net.thejadeproject.ascension.progression.skills.data.ISkillData;
import net.thejadeproject.ascension.util.ModAttachments;
import org.joml.Matrix4f;
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

    // Colors for rendering
    private static final int GREEN_OUTLINE_COLOR = 0x00FF00; // Bright green
    private static final int CONNECTED_OUTLINE_COLOR = 0x32CD32; // Lime green for connected
    private static final float OUTLINE_WIDTH = 2.0f; // Line width

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
    public ISkillData getSkillData() {
        return null;
    }

    @Override
    public ISkillData decode(RegistryFriendlyByteBuf buf) {
        return null;
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


/* Adding back renderer in future
    // Client-side rendering
    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class WoodOutlineRenderer {

        @SubscribeEvent
        public static void onRenderLevel(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
                return;
            }

            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null || mc.player == null) {
                return;
            }

            UUID playerId = mc.player.getUUID();
            WoodHarvestData data = getWoodDataForPlayer(playerId);

            if (data == null || data.allWoodBlocks.isEmpty()) {
                return;
            }

            Camera camera = mc.gameRenderer.getMainCamera();
            double camX = camera.getPosition().x;
            double camY = camera.getPosition().y;
            double camZ = camera.getPosition().z;

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

            // Save OpenGL state
            boolean depthTestEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
            boolean blendEnabled = GL11.glIsEnabled(GL11.GL_BLEND);
            boolean cullEnabled = GL11.glIsEnabled(GL11.GL_CULL_FACE);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            // Set line width
            GL11.glLineWidth(OUTLINE_WIDTH);

            // Render all wood blocks with green outlines
            for (BlockPos pos : data.allWoodBlocks) {
                renderSingleBlockOutline(poseStack, buffers, pos, GREEN_OUTLINE_COLOR, camX, camY, camZ);
            }

            // Render connected components with lime green bounding boxes
            for (Set<BlockPos> component : data.connectedComponents) {
                if (component.size() > 1) {
                    renderConnectedOutline(poseStack, buffers, component, CONNECTED_OUTLINE_COLOR, camX, camY, camZ);
                }
            }

            buffers.endBatch();

            // Restore OpenGL state
            GL11.glLineWidth(1.0f);
            if (depthTestEnabled) {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
            if (!blendEnabled) {
                GL11.glDisable(GL11.GL_BLEND);
            }
            if (cullEnabled) {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
        }

        private static void renderSingleBlockOutline(PoseStack poseStack, MultiBufferSource.BufferSource buffers,
                                                     BlockPos pos, int color, double camX, double camY, double camZ) {
            poseStack.pushPose();
            poseStack.translate(pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ);

            VertexConsumer consumer = buffers.getBuffer(RenderType.debugLineStrip(2.0f));
            Matrix4f matrix = poseStack.last().pose();

            // Extract RGB from hex color, with alpha
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            int a = 180;

            // Slightly expand the outline to be visible
            float min = -0.01f;
            float max = 1.01f;

            // Draw wireframe cube (12 lines)
            // Bottom square
            consumer.addVertex(matrix, min, min, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, min, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, min, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, min, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, min, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, min, min, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, min, min, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, min, min, min).setColor(r, g, b, a);

            // Top square
            consumer.addVertex(matrix, min, max, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, max, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, max, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, max, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, max, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, min, max, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, min, max, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, min, max, min).setColor(r, g, b, a);

            // Vertical edges
            consumer.addVertex(matrix, min, min, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, min, max, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, min, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, max, min).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, min, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, max, max, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, min, min, max).setColor(r, g, b, a);
            consumer.addVertex(matrix, min, max, max).setColor(r, g, b, a);

            poseStack.popPose();
        }

        private static void renderConnectedOutline(PoseStack poseStack, MultiBufferSource.BufferSource buffers,
                                                   Set<BlockPos> component, int color, double camX, double camY, double camZ) {
            if (component.isEmpty()) return;

            // Calculate bounding box for the connected component
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;

            for (BlockPos pos : component) {
                minX = Math.min(minX, pos.getX());
                minY = Math.min(minY, pos.getY());
                minZ = Math.min(minZ, pos.getZ());
                maxX = Math.max(maxX, pos.getX());
                maxY = Math.max(maxY, pos.getY());
                maxZ = Math.max(maxZ, pos.getZ());
            }

            poseStack.pushPose();
            poseStack.translate(minX - camX, minY - camY, minZ - camZ);

            VertexConsumer consumer = buffers.getBuffer(RenderType.debugLineStrip(2.5f)); // Thicker line
            Matrix4f matrix = poseStack.last().pose();

            // Extract RGB from hex color, with alpha
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            int a = 200;

            float width = maxX - minX + 1;
            float height = maxY - minY + 1;
            float depth = maxZ - minZ + 1;

            // Slight expansion
            float expand = 0.02f;

            // Draw wireframe bounding box
            // Bottom square
            consumer.addVertex(matrix, -expand, -expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, -expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, -expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, -expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, -expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, -expand, -expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, -expand, -expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, -expand, -expand, -expand).setColor(r, g, b, a);

            // Top square
            consumer.addVertex(matrix, -expand, height + expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, height + expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, height + expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, height + expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, height + expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, -expand, height + expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, -expand, height + expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, -expand, height + expand, -expand).setColor(r, g, b, a);

            // Vertical edges
            consumer.addVertex(matrix, -expand, -expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, -expand, height + expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, -expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, height + expand, -expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, -expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, width + expand, height + expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, -expand, -expand, depth + expand).setColor(r, g, b, a);
            consumer.addVertex(matrix, -expand, height + expand, depth + expand).setColor(r, g, b, a);

            poseStack.popPose();
        }
    }*/
}