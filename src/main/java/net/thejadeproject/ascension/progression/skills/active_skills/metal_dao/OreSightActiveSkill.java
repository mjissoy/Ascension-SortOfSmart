package net.thejadeproject.ascension.progression.skills.active_skills.metal_dao;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.network.packets.ClearOreSightPacket;
import net.thejadeproject.ascension.network.packets.SyncOreSightPacket;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OreSightActiveSkill extends AbstractActiveSkill {

    // Server-side storage for glowing blocks per player
    private static final Map<UUID, Map<BlockPos, OreGlowData>> playerGlowingBlocks = new ConcurrentHashMap<>();

    // Client-side storage for glowing blocks (synced from server)
    private static final Map<UUID, Map<BlockPos, OreGlowData>> clientGlowingBlocks = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> clientExpirationTimes = new ConcurrentHashMap<>();

    // Configuration for ore colors (loaded from config)
    private static Map<Block, OreColorConfig> oreColorConfig = new HashMap<>();

    // Minimum realm required for ore name display
    private static final int MIN_REALM_FOR_NAME_DISPLAY = 5; // Example: 5th major realm

    static {
        Map<Block, Integer> tempMap = new HashMap<>();
        tempMap.put(Blocks.COAL_ORE, 0x2F2F2F);           // Dark Gray
        tempMap.put(Blocks.DEEPSLATE_COAL_ORE, 0x2F2F2F);
        tempMap.put(Blocks.IRON_ORE, 0xD8D8D8);           // Light Gray
        tempMap.put(Blocks.DEEPSLATE_IRON_ORE, 0xD8D8D8);
        tempMap.put(Blocks.COPPER_ORE, 0xB87333);         // Copper
        tempMap.put(Blocks.DEEPSLATE_COPPER_ORE, 0xB87333);
        tempMap.put(Blocks.GOLD_ORE, 0xFFD700);           // Gold
        tempMap.put(Blocks.DEEPSLATE_GOLD_ORE, 0xFFD700);
        tempMap.put(Blocks.NETHER_GOLD_ORE, 0xFFD700);
        tempMap.put(Blocks.DIAMOND_ORE, 0x00FFFF);        // Cyan
        tempMap.put(Blocks.DEEPSLATE_DIAMOND_ORE, 0x00FFFF);
        tempMap.put(Blocks.EMERALD_ORE, 0x00FF00);        // Green
        tempMap.put(Blocks.DEEPSLATE_EMERALD_ORE, 0x00FF00);
        tempMap.put(Blocks.LAPIS_ORE, 0x0000FF);          // Blue
        tempMap.put(Blocks.DEEPSLATE_LAPIS_ORE, 0x0000FF);
        tempMap.put(Blocks.REDSTONE_ORE, 0xFF0000);       // Red
        tempMap.put(Blocks.DEEPSLATE_REDSTONE_ORE, 0xFF0000);
        tempMap.put(Blocks.NETHER_QUARTZ_ORE, 0xF0F0F0);  // Light Gray/White
        tempMap.put(Blocks.ANCIENT_DEBRIS, 0x8B4513);     // Brown

        DEFAULT_ORE_COLORS = Collections.unmodifiableMap(tempMap);
    }

    private static final Map<Block, Integer> DEFAULT_ORE_COLORS;

    public static class OreGlowData {
        final int color;
        final long expirationTime;
        final Block block;

        public OreGlowData(int color, long expirationTime, Block block) {
            this.color = color;
            this.expirationTime = expirationTime;
            this.block = block;
        }
    }

    private static class OreColorConfig {
        final int color;
        final boolean enabled;

        OreColorConfig(int color, boolean enabled) {
            this.color = color;
            this.enabled = enabled;
        }
    }

    public OreSightActiveSkill() {
        super(Component.translatable("ascension.skill.active.ore_sight"));
        this.path = "ascension:essence";
        this.qiCost = 15.0;

        // Initialize config from mod configuration
        loadOreColorConfig();
    }

    @Override
    public boolean isPrimarySkill() {
        return true;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public int getCooldown() {
        return 20 * 60; // 1 minute cooldown
    }

    @Override
    public int maxCastingTicks() {
        return 0;
    }


    @Override
    public void cast(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        if (level.isClientSide()) return;

        UUID playerId = player.getUUID();

        // Clear previous glowing blocks for this player
        playerGlowingBlocks.remove(playerId);

        // Get skill duration based on cultivation level
        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        int durationSeconds = 10 + (majorRealm * 5); // 10s + 5s per realm
        long expirationTime = System.currentTimeMillis() + (durationSeconds * 1000L);

        // Get detection radius based on cultivation level
        int detectionRadius = 10 + majorRealm; // 10 + realm blocks radius

        BlockPos playerPos = player.blockPosition();
        Map<BlockPos, OreGlowData> glowingBlocks = new HashMap<>();
        int oresFound = 0;

        // Scan for ores in the area
        for (int x = -detectionRadius; x <= detectionRadius; x++) {
            for (int y = -detectionRadius; y <= detectionRadius; y++) {
                for (int z = -detectionRadius; z <= detectionRadius; z++) {
                    BlockPos checkPos = playerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(checkPos);
                    Block block = blockState.getBlock();

                    if (isOreBlock(block)) {
                        OreColorConfig config = oreColorConfig.get(block);
                        if (config != null && config.enabled) {
                            glowingBlocks.put(checkPos.immutable(), new OreGlowData(config.color, expirationTime, block));
                            oresFound++;

                            // Spawn particle effect on server side
                            if (level instanceof ServerLevel serverLevel) {
                                spawnOreParticle(serverLevel, checkPos, config.color);
                            }
                        }
                    }
                }
            }
        }

        playerGlowingBlocks.put(playerId, glowingBlocks);

        // Send packet to client to sync glowing blocks
        if (player instanceof ServerPlayer serverPlayer) {
            // Convert to packet data
            Map<BlockPos, SyncOreSightPacket.BlockData> packetData = new HashMap<>();
            for (Map.Entry<BlockPos, OreGlowData> entry : glowingBlocks.entrySet()) {
                OreGlowData data = entry.getValue();
                packetData.put(
                        entry.getKey(),
                        new SyncOreSightPacket.BlockData(
                                data.color,
                                data.expirationTime,
                                BuiltInRegistries.BLOCK.getKey(data.block)
                        )
                );
            }

            SyncOreSightPacket packet = new SyncOreSightPacket(
                    playerId,
                    packetData,
                    durationSeconds
            );
            serverPlayer.connection.send(packet);
        }

        // Play sound based on ores found
        if (oresFound > 0) {
            level.playSound(null, playerPos, SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS, 0.8F, 1.2F);
        } else {
            level.playSound(null, playerPos, SoundEvents.NOTE_BLOCK_BASS.value(),
                    SoundSource.PLAYERS, 0.5F, 0.8F);
        }

        // Clean up old glowing blocks for all players
        cleanupOldGlowingBlocks();
    }

    @Override
    public void onPreCast() {
        // Nothing needed here
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        UUID playerId = player.getUUID();
        playerGlowingBlocks.remove(playerId);

        // Send packet to client to clear sense data
        if (player instanceof ServerPlayer serverPlayer) {
            ClearOreSightPacket packet = new ClearOreSightPacket(playerId);
            serverPlayer.connection.send(packet);
        }
    }

    private boolean isOreBlock(Block block) {
        // Check if block is in our config
        if (oreColorConfig.containsKey(block)) {
            return true;
        }

        // Fallback: check if block name contains "ore" (excluding common false positives)
        ResourceLocation blockName = BuiltInRegistries.BLOCK.getKey(block);
        if (blockName == null) {
            return false;
        }

        String name = blockName.getPath().toLowerCase();
        return name.contains("ore") &&
                !name.contains("shore") &&
                !name.contains("more") &&
                !name.contains("core") &&
                !name.contains("store") &&
                !name.contains("before") &&
                !name.contains("explore") &&
                !name.contains("ignore") &&
                !name.contains("restore") &&
                !name.contains("fore");
    }

    private void spawnOreParticle(ServerLevel level, BlockPos pos, int color) {
        // Convert hex color to RGB
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        DustParticleOptions particle = new DustParticleOptions(new Vector3f(r, g, b), 1.0f);

        // Spawn particles above the ore
        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 0.3;
            double y = pos.getY() + 1.0 + level.random.nextDouble() * 0.5;
            double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 0.3;

            level.sendParticles(particle, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    private void loadOreColorConfig() {
        oreColorConfig.clear();

        // Load from mod configuration
        // This would typically come from your mod's config file
        // For now, we'll use defaults and allow runtime configuration

        // Add default vanilla ores
        for (Map.Entry<Block, Integer> entry : DEFAULT_ORE_COLORS.entrySet()) {
            oreColorConfig.put(entry.getKey(), new OreColorConfig(entry.getValue(), true));
        }

        // You can add a method to load from config file here
        // Example: loadConfigFromFile();
    }

    public static void addOreColorConfig(Block block, int color, boolean enabled) {
        oreColorConfig.put(block, new OreColorConfig(color, enabled));
    }

    public static void removeOreColorConfig(Block block) {
        oreColorConfig.remove(block);
    }

    public static Map<Block, Integer> getConfiguredOres() {
        return oreColorConfig.entrySet().stream()
                .filter(entry -> entry.getValue().enabled)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().color
                ));
    }

    private static void cleanupOldGlowingBlocks() {
        long currentTime = System.currentTimeMillis();

        playerGlowingBlocks.forEach((playerId, blocks) -> {
            blocks.entrySet().removeIf(entry ->
                    currentTime > entry.getValue().expirationTime
            );
        });

        // Remove empty player entries
        playerGlowingBlocks.entrySet().removeIf(entry ->
                entry.getValue().isEmpty()
        );
    }

    private static void cleanupOldClientGlowingBlocks() {
        long currentTime = System.currentTimeMillis();

        clientGlowingBlocks.entrySet().removeIf(entry -> {
            Long expirationTime = clientExpirationTimes.get(entry.getKey());
            return expirationTime == null || currentTime > expirationTime;
        });

        clientExpirationTimes.entrySet().removeIf(entry -> currentTime > entry.getValue());
    }

    public static Map<BlockPos, OreGlowData> getGlowingBlocksForPlayer(UUID playerId) {
        // On client, use client-side data; on server, use server-side data
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.level.isClientSide()) {
            return getClientGlowingBlocksForPlayer(playerId);
        }
        cleanupOldGlowingBlocks();
        return playerGlowingBlocks.getOrDefault(playerId, Collections.emptyMap());
    }

    public static Map<BlockPos, OreGlowData> getClientGlowingBlocksForPlayer(UUID playerId) {
        cleanupOldClientGlowingBlocks();
        return clientGlowingBlocks.getOrDefault(playerId, Collections.emptyMap());
    }

    public static boolean hasActiveOreSight(Player player) {
        // Check appropriate map based on side
        if (player.level().isClientSide()) {
            Map<BlockPos, OreGlowData> blocks = getClientGlowingBlocksForPlayer(player.getUUID());
            return blocks != null && !blocks.isEmpty();
        } else {
            Map<BlockPos, OreGlowData> blocks = playerGlowingBlocks.get(player.getUUID());
            return blocks != null && !blocks.isEmpty();
        }
    }

    public static void clearPlayerOreSight(UUID playerId) {
        playerGlowingBlocks.remove(playerId);
    }

    public static void updateClientGlowingBlocks(UUID playerId, Map<BlockPos, OreGlowData> blocks, long durationSeconds) {
        clientGlowingBlocks.put(playerId, blocks);
        clientExpirationTimes.put(playerId, System.currentTimeMillis() + (durationSeconds * 1000L));
    }

    public static void clearClientGlowingBlocks(UUID playerId) {
        clientGlowingBlocks.remove(playerId);
        clientExpirationTimes.remove(playerId);
    }

    // Get the ore block the player is currently looking at based on their view direction
    public static Block getLookedAtOreFromGlow(Player player) {
        Minecraft mc = Minecraft.getInstance();
        Map<BlockPos, OreGlowData> glowingBlocks = getGlowingBlocksForPlayer(player.getUUID());

        if (glowingBlocks.isEmpty()) {
            return null;
        }

        // Get player's eye position and look vector
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();

        // Raycast through the view direction
        double maxDistance = 64.0; // Max render distance for the glow

        // Check each glowing block to see if it's in the view direction
        BlockPos closestBlock = null;
        double closestDistance = Double.MAX_VALUE;

        for (BlockPos pos : glowingBlocks.keySet()) {
            // Create AABB for the glowing block (slightly expanded for easier targeting)
            AABB blockAABB = new AABB(
                    pos.getX() - 0.1, pos.getY() - 0.1, pos.getZ() - 0.1,
                    pos.getX() + 1.1, pos.getY() + 1.1, pos.getZ() + 1.1
            );

            // Check if the look vector intersects with the block's AABB
            Vec3 hitVec = blockAABB.clip(eyePos, eyePos.add(lookVec.scale(maxDistance))).orElse(null);

            if (hitVec != null) {
                double distance = eyePos.distanceTo(hitVec);
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestBlock = pos;
                }
            }
        }

        if (closestBlock != null) {
            OreGlowData data = glowingBlocks.get(closestBlock);
            if (data != null) {
                return data.block;
            }
        }
        return null;
    }

    // Get player's cultivation realm for this skill path
    public static int getPlayerRealm(Player player) {
        return player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:essence").majorRealm;
    }

    // Server tick handler for updating detected ores
    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class ServerTickHandler {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Pre event) {
            if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
                UUID playerId = serverPlayer.getUUID();
                Map<BlockPos, OreGlowData> glowing = playerGlowingBlocks.get(playerId);

                // Sync ore sight data periodically (every 2 seconds) for reliability
                if (glowing != null && !glowing.isEmpty() && serverPlayer.tickCount % 40 == 0) {
                    // Get skill duration
                    int majorRealm = serverPlayer.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:essence").majorRealm;
                    int durationSeconds = 10 + (majorRealm * 5);

                    // Convert to packet data
                    Map<BlockPos, SyncOreSightPacket.BlockData> packetData = new HashMap<>();
                    for (Map.Entry<BlockPos, OreGlowData> entry : glowing.entrySet()) {
                        OreGlowData data = entry.getValue();
                        packetData.put(
                                entry.getKey(),
                                new SyncOreSightPacket.BlockData(
                                        data.color,
                                        data.expirationTime,
                                        BuiltInRegistries.BLOCK.getKey(data.block)
                                )
                        );
                    }

                    SyncOreSightPacket packet = new SyncOreSightPacket(
                            playerId,
                            packetData,
                            durationSeconds
                    );
                    serverPlayer.connection.send(packet);
                }
            }
        }
    }

    // Client-side rendering
    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class OreGlowRenderer {

        // Alpha values for distance-based transparency
        private static final int MAX_ALPHA = 180;  // Maximum alpha (far away)
        private static final int MIN_ALPHA = 30;   // Minimum alpha (very close)
        private static final float FADE_START_DISTANCE = 3.0f; // Distance where fading starts
        private static final float FADE_END_DISTANCE = 10.0f;  // Distance where fading ends (max alpha)

        @SubscribeEvent
        public static void onRenderLevel(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
                return;
            }

            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null || mc.player == null) {
                return;
            }

            Map<BlockPos, OreGlowData> glowing = getClientGlowingBlocksForPlayer(mc.player.getUUID());
            if (glowing.isEmpty()) {
                return;
            }

            Camera camera = mc.gameRenderer.getMainCamera();
            double camX = camera.getPosition().x;
            double camY = camera.getPosition().y;
            double camZ = camera.getPosition().z;

            // Get player position for distance calculation
            BlockPos playerPos = mc.player.blockPosition();

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

            // Save OpenGL state
            boolean depthTestEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
            boolean blendEnabled = GL11.glIsEnabled(GL11.GL_BLEND);

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(false);

            for (Map.Entry<BlockPos, OreGlowData> entry : glowing.entrySet()) {
                BlockPos pos = entry.getKey();
                OreGlowData data = entry.getValue();

                if (System.currentTimeMillis() > clientExpirationTimes.getOrDefault(mc.player.getUUID(), 0L)) {
                    continue;
                }

                BlockState state = mc.level.getBlockState(pos);
                if (state.isAir()) {
                    continue;
                }

                // Calculate distance from player to block
                double distance = Math.sqrt(
                        (pos.getX() - playerPos.getX()) * (pos.getX() - playerPos.getX()) +
                                (pos.getY() - playerPos.getY()) * (pos.getY() - playerPos.getY()) +
                                (pos.getZ() - playerPos.getZ()) * (pos.getZ() - playerPos.getZ())
                );

                // Calculate alpha based on distance
                int alpha = calculateAlphaBasedOnDistance((float)distance);

                renderGlowBlock(poseStack, buffers, pos, data.color, alpha, camX, camY, camZ);
            }

            buffers.endBatch();

            // Restore OpenGL state
            GL11.glDepthMask(true);
            if (depthTestEnabled) {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
            if (!blendEnabled) {
                GL11.glDisable(GL11.GL_BLEND);
            }
        }

        private static int calculateAlphaBasedOnDistance(float distance) {
            if (distance <= FADE_START_DISTANCE) {
                // Very close - minimum alpha (most translucent)
                return MIN_ALPHA;
            } else if (distance >= FADE_END_DISTANCE) {
                // Far away - maximum alpha (least translucent)
                return MAX_ALPHA;
            } else {
                // In between - interpolate alpha
                float t = (distance - FADE_START_DISTANCE) / (FADE_END_DISTANCE - FADE_START_DISTANCE);
                return (int)(MIN_ALPHA + (MAX_ALPHA - MIN_ALPHA) * t);
            }
        }

        private static void renderGlowBlock(PoseStack poseStack, MultiBufferSource.BufferSource buffers,
                                            BlockPos pos, int color, int alpha, double camX, double camY, double camZ) {
            poseStack.pushPose();
            poseStack.translate(pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ);

            VertexConsumer consumer = buffers.getBuffer(RenderType.debugQuads());
            renderFullBlock(consumer, poseStack, color, alpha);

            poseStack.popPose();
        }

        private static void renderFullBlock(VertexConsumer consumer, PoseStack poseStack, int color, int alpha) {
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix = pose.pose();

            // Extract RGB from hex color, use dynamic alpha
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            // Alpha is passed as parameter based on distance

            // Slightly shrink the block to avoid z-fighting
            float minX = 0.001F;
            float minY = 0.001F;
            float minZ = 0.001F;
            float maxX = 0.999F;
            float maxY = 0.999F;
            float maxZ = 0.999F;

            // Draw all 6 faces
            // Bottom face
            addQuad(consumer, matrix, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, alpha);
            // Top face
            addQuad(consumer, matrix, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ, r, g, b, alpha);
            // North face
            addQuad(consumer, matrix, maxX, minY, minZ, minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, alpha);
            // South face
            addQuad(consumer, matrix, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, alpha);
            // West face
            addQuad(consumer, matrix, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, alpha);
            // East face
            addQuad(consumer, matrix, maxX, minY, maxZ, maxX, minY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, alpha);
        }

        private static void addQuad(VertexConsumer consumer, Matrix4f matrix,
                                    float x1, float y1, float z1,
                                    float x2, float y2, float z2,
                                    float x3, float y3, float z3,
                                    float x4, float y4, float z4,
                                    int r, int g, int b, int a) {
            consumer.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a);
            consumer.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a);
            consumer.addVertex(matrix, x3, y3, z3).setColor(r, g, b, a);
            consumer.addVertex(matrix, x4, y4, z4).setColor(r, g, b, a);
        }
    }

    // GUI overlay for displaying ore name under crosshair
    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class OreNameOverlay {

        // Track last ore name to avoid flickering
        private static Block lastLookedAtOre = null;
        private static long lastDisplayTime = 0;
        private static final long DISPLAY_DURATION = 1000; // ms to keep displaying after looking away

        @SubscribeEvent
        public static void onRenderGui(RenderGuiEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) {
                return;
            }

            // Check if player has active ore sight
            if (!hasActiveOreSight(mc.player)) {
                lastLookedAtOre = null;
                return;
            }

            // Get player's cultivation realm
            int playerRealm = getPlayerRealm(mc.player);

            // Check if player has reached the required realm for name display
            if (playerRealm < MIN_REALM_FOR_NAME_DISPLAY) {
                return;
            }

            // Get the ore block the player is currently looking at based on the glow
            Block lookedAtOre = getLookedAtOreFromGlow(mc.player);

            // Update tracking
            if (lookedAtOre != null) {
                lastLookedAtOre = lookedAtOre;
                lastDisplayTime = System.currentTimeMillis();
            } else {
                // If not looking at an ore, check if we should still display the last one
                if (System.currentTimeMillis() - lastDisplayTime > DISPLAY_DURATION) {
                    lastLookedAtOre = null;
                }
            }

            // If we have an ore to display
            if (lastLookedAtOre != null) {
                renderOreTooltip(event.getGuiGraphics(), lastLookedAtOre, mc, playerRealm);
            }
        }

        private static void renderOreTooltip(GuiGraphics guiGraphics, Block oreBlock, Minecraft mc, int playerRealm) {
            // Get the ore's display name
            Component oreName = oreBlock.getName();

            // Create a styled component based on player's realm
            MutableComponent displayText = Component.empty();

            // Add realm-specific prefix or styling
            if (playerRealm >= 10) {
                // Higher realms get more detailed info
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(oreBlock);
                if (blockId != null) {
                    displayText = Component.literal("§6[Master Miner]§r ")
                            .append(oreName.copy().withStyle(style -> style.withColor(0xFFFF00)));
                }
            } else if (playerRealm >= 7) {
                // Medium realms get colored names
                displayText = oreName.copy().withStyle(style -> style.withColor(0x55FF55));
            } else {
                // Basic display for lower realms
                displayText = oreName.copy();
            }

            // Get screen dimensions
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            // Position under the crosshair (center of screen)
            int centerX = screenWidth / 2;
            int centerY = screenHeight / 2;

            // Position text below crosshair with offset
            int textX = centerX;
            int textY = centerY + 12; // 12 pixels below crosshair

            // Get text width for background and centering
            Font font = mc.font;
            int textWidth = font.width(displayText);

            // Center the text horizontally
            textX = centerX - (textWidth / 2);

            // Smaller tooltip dimensions
            int bgPaddingH = 6;  // Reduced from 10
            int bgPaddingV = 2;  // Reduced from 6
            int bgColor = 0x80000000; // Black with 50% alpha

            // Draw smaller semi-transparent background
            guiGraphics.fill(
                    textX - bgPaddingH,
                    textY - bgPaddingV,
                    textX + textWidth + bgPaddingH,
                    textY + font.lineHeight + bgPaddingV,
                    bgColor
            );

            // Draw subtle border (thinner)
            int borderColor = getBorderColorForOre(oreBlock, playerRealm);
            int borderThickness = 1; // Reduced from 2

            // Top border (subtle)
            guiGraphics.fill(
                    textX - bgPaddingH,
                    textY - bgPaddingV,
                    textX + textWidth + bgPaddingH,
                    textY - bgPaddingV + borderThickness,
                    borderColor
            );

            // Bottom border (subtle)
            guiGraphics.fill(
                    textX - bgPaddingH,
                    textY + font.lineHeight + bgPaddingV - borderThickness,
                    textX + textWidth + bgPaddingH,
                    textY + font.lineHeight + bgPaddingV,
                    borderColor
            );

            // Left border (subtle)
            guiGraphics.fill(
                    textX - bgPaddingH,
                    textY - bgPaddingV,
                    textX - bgPaddingH + borderThickness,
                    textY + font.lineHeight + bgPaddingV,
                    borderColor
            );

            // Right border (subtle)
            guiGraphics.fill(
                    textX + textWidth + bgPaddingH - borderThickness,
                    textY - bgPaddingV,
                    textX + textWidth + bgPaddingH,
                    textY + font.lineHeight + bgPaddingV,
                    borderColor
            );

            // Draw the text with shadow for better readability
            guiGraphics.drawString(font, displayText, textX, textY, 0xFFFFFF, true);

            // For very high realms, add additional info below (smaller)
            if (playerRealm >= 12) {
                // Add rarity indicator or other info
                Component rarityText = getRarityText(oreBlock);
                if (rarityText != null) {
                    int rarityY = textY + font.lineHeight + bgPaddingV + 2;
                    int rarityWidth = font.width(rarityText);
                    int rarityX = centerX - (rarityWidth / 2);

                    // Draw smaller rarity background
                    int rarityBgColor = 0x60000000;
                    int rarityBgPaddingH = 5;
                    int rarityBgPaddingV = 1;

                    guiGraphics.fill(
                            rarityX - rarityBgPaddingH,
                            rarityY - rarityBgPaddingV,
                            rarityX + rarityWidth + rarityBgPaddingH,
                            rarityY + font.lineHeight + rarityBgPaddingV,
                            rarityBgColor
                    );

                    // Draw rarity text
                    guiGraphics.drawString(font, rarityText, rarityX, rarityY, 0xFFAA00, true);
                }
            }

            // Optional: Draw a small icon or indicator next to the text (smaller)
            if (playerRealm >= 8) {
                // Draw a small ore-like square next to the name
                int iconSize = 6;
                int iconX = textX - bgPaddingH - iconSize - 1;
                int iconY = textY + (font.lineHeight - iconSize) / 2;

                int oreColor = getColorForOre(oreBlock);
                int r = (oreColor >> 16) & 0xFF;
                int g = (oreColor >> 8) & 0xFF;
                int b = oreColor & 0xFF;

                // Draw icon with subtle border
                guiGraphics.fill(iconX - 1, iconY - 1, iconX + iconSize + 1, iconY + iconSize + 1, 0x80000000);
                guiGraphics.fill(iconX, iconY, iconX + iconSize, iconY + iconSize, (200 << 24) | (r << 16) | (g << 8) | b);
            }
        }

        private static int getBorderColorForOre(Block oreBlock, int playerRealm) {
            // Return different border colors based on ore rarity and player realm
            if (playerRealm >= 10) {
                // High realms get colored borders
                if (oreBlock == Blocks.DIAMOND_ORE || oreBlock == Blocks.DEEPSLATE_DIAMOND_ORE) {
                    return 0x8000FFFF; // Cyan with transparency
                } else if (oreBlock == Blocks.EMERALD_ORE || oreBlock == Blocks.DEEPSLATE_EMERALD_ORE) {
                    return 0x8000FF00; // Green with transparency
                } else if (oreBlock == Blocks.ANCIENT_DEBRIS) {
                    return 0x80FF6600; // Orange with transparency
                } else if (oreBlock == Blocks.NETHERITE_BLOCK) {
                    return 0x80990000; // Dark Red with transparency
                } else if (oreBlock == Blocks.GOLD_ORE || oreBlock == Blocks.DEEPSLATE_GOLD_ORE) {
                    return 0x80FFFF00; // Yellow with transparency
                }
            }
            return 0x80AA00; // Default orange with transparency
        }

        private static int getColorForOre(Block oreBlock) {
            // Get the configured color for this ore
            OreColorConfig config = oreColorConfig.get(oreBlock);
            if (config != null) {
                return config.color;
            }
            return 0xFFFFFF; // Default white
        }

        private static Component getRarityText(Block oreBlock) {
            // Define rarity based on block type
            if (oreBlock == Blocks.DIAMOND_ORE || oreBlock == Blocks.DEEPSLATE_DIAMOND_ORE) {
                return Component.literal("§b✨ Rare");
            } else if (oreBlock == Blocks.EMERALD_ORE || oreBlock == Blocks.DEEPSLATE_EMERALD_ORE) {
                return Component.literal("§a★ Uncommon");
            } else if (oreBlock == Blocks.ANCIENT_DEBRIS) {
                return Component.literal("§6🔥 Legendary");
            } else if (oreBlock == Blocks.NETHERITE_BLOCK) {
                return Component.literal("§4⚔ Mythical");
            } else if (oreBlock == Blocks.GOLD_ORE || oreBlock == Blocks.DEEPSLATE_GOLD_ORE) {
                return Component.literal("§e⚜ Valuable");
            } else if (oreBlock == Blocks.IRON_ORE || oreBlock == Blocks.DEEPSLATE_IRON_ORE) {
                return Component.literal("§7⚒ Common");
            }
            return Component.literal("§7Common");
        }
    }
}