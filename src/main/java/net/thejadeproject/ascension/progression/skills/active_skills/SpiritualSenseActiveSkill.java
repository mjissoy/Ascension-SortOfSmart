package net.thejadeproject.ascension.progression.skills.active_skills;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.network.packets.ClearSpiritualSensePacket;
import net.thejadeproject.ascension.network.packets.SyncSpiritualSenseEntitiesPacket;
import net.thejadeproject.ascension.network.packets.SyncSpiritualSensePacket;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpiritualSenseActiveSkill extends AbstractActiveSkill {

    // Store sense data per player - separate maps for client and server
    private static final Map<UUID, SenseData> playerSenseData = new ConcurrentHashMap<>();

    // Store detected entities for each player (key: player UUID, value: map of entity ID to entity info)
    private static final Map<UUID, Map<Integer, EntityInfo>> playerDetectedEntities = new ConcurrentHashMap<>();

    // Store player information for display
    private static final Map<UUID, PlayerSenseInfo> playerSenseInfo = new ConcurrentHashMap<>();

    // Store currently glowing entities to clear them later
    private static final Map<UUID, Set<Integer>> playerGlowingEntities = new ConcurrentHashMap<>();

    public SpiritualSenseActiveSkill() {
        super(Component.translatable("ascension.skill.active.spiritual_sense"));
        this.path = "ascension:essence";
        this.qiCost = 20.0;
    }

    // Add getters for client-side access
    public static Map<UUID, SenseData> getPlayerSenseDataMap() {
        return playerSenseData;
    }

    public static Map<UUID, PlayerSenseInfo> getPlayerSenseInfoMap() {
        return playerSenseInfo;
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
    public void cast(int castingTicksElapsed, Level level, Player player) {
        if (level.isClientSide()) return;

        UUID playerId = player.getUUID();

        // Clear previous sense data
        clearPlayerSenseData(level, playerId);

        playerSenseData.remove(playerId);
        playerDetectedEntities.remove(playerId);
        playerSenseInfo.remove(playerId);
        playerGlowingEntities.remove(playerId);

        // Get player's cultivation level
        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;

        // Calculate radius: base 16 + 8 per realm
        float maxRadius = 16.0f + (majorRealm * 8.0f);

        // Calculate duration: 2 seconds for expansion
        long startTime = System.currentTimeMillis();
        long expansionDuration = 2000; // 2 seconds in milliseconds

        // Calculate active duration: base 5 seconds + 3 seconds per realm
        long activeDuration = 8000 + (majorRealm * 4000);
        long expansionEndTime = startTime + expansionDuration;

        // Create sense data
        SenseData senseData = new SenseData(
                player.position(),
                maxRadius,
                startTime,
                expansionEndTime,
                expansionDuration,
                activeDuration,
                majorRealm
        );

        playerSenseData.put(playerId, senseData);
        playerGlowingEntities.put(playerId, new HashSet<>());

        // Send packet to client to sync sense data
        if (player instanceof ServerPlayer serverPlayer) {
            SyncSpiritualSensePacket packet = new SyncSpiritualSensePacket(
                    playerId,
                    player.position(),
                    maxRadius,
                    startTime,
                    expansionEndTime,
                    expansionDuration,
                    activeDuration,
                    majorRealm
            );
            serverPlayer.connection.send(packet);
        }

        // Play sound effect
        level.playSound(null, player.blockPosition(), SoundEvents.ENDER_EYE_DEATH,
                SoundSource.PLAYERS, 1.0F, 1.2F);
    }

    @Override
    public void onPreCast() {
        // Nothing needed here
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        UUID playerId = player.getUUID();
        clearPlayerSenseData(player.level(), playerId);

        playerSenseData.remove(playerId);
        playerDetectedEntities.remove(playerId);
        playerSenseInfo.remove(playerId);
        playerGlowingEntities.remove(playerId);

        // Send packet to client to clear sense data
        if (player instanceof ServerPlayer serverPlayer) {
            ClearSpiritualSensePacket packet = new ClearSpiritualSensePacket(playerId);
            serverPlayer.connection.send(packet);
        }
    }

    private static void clearPlayerSenseData(Level level, UUID playerId) {
        // Clear glowing effects
        Set<Integer> glowingEntities = playerGlowingEntities.get(playerId);
        if (glowingEntities != null) {
            for (int entityId : glowingEntities) {
                Entity entity = level.getEntity(entityId);
                if (entity != null) {
                    entity.setGlowingTag(false);
                }
            }
        }

        if (playerGlowingEntities.containsKey(playerId)) {
            playerGlowingEntities.get(playerId).clear();
        }
    }

    public static class SenseData {
        public Vec3 center; // Changed to public for packet access
        public final float maxRadius;
        public final long startTime;
        public final long expansionEndTime;
        public final long expansionDuration;
        public final long activeDuration; // Added for realm-based duration
        public final int playerRealm;
        public boolean expansionComplete = false;

        public SenseData(Vec3 center, float maxRadius, long startTime, long expansionEndTime,
                         long expansionDuration, long activeDuration, int playerRealm) {
            this.center = center;
            this.maxRadius = maxRadius;
            this.startTime = startTime;
            this.expansionEndTime = expansionEndTime;
            this.expansionDuration = expansionDuration;
            this.activeDuration = activeDuration;
            this.playerRealm = playerRealm;
        }

        public float getCurrentRadius(long currentTime) {
            if (expansionComplete) {
                return maxRadius;
            }

            float progress = Math.min(1.0f, (float)(currentTime - startTime) / expansionDuration);
            if (progress >= 1.0f) {
                expansionComplete = true;
            }
            return maxRadius * progress;
        }

        public boolean isActive(long currentTime) {
            return currentTime < expansionEndTime + activeDuration;
        }
    }

    public static class EntityInfo {
        final int entityId;
        final UUID entityUuid;
        final String name;
        final int essenceRealm;
        final int bodyRealm;
        final int intentRealm;
        final float health;
        final float maxHealth;
        final boolean isPlayer;
        final long detectionTime;
        final EntityType<?> entityType; // Added for grouping
        final int count; // Added for grouped entities
        final boolean canSeeDetails; // Added for realm-based obscurity

        // Constructor for single entity
        public EntityInfo(int entityId, UUID entityUuid, String name, int essenceRealm, int bodyRealm,
                          int intentRealm, float health, float maxHealth, boolean isPlayer,
                          EntityType<?> entityType, boolean canSeeDetails) {
            this.entityId = entityId;
            this.entityUuid = entityUuid;
            this.name = name;
            this.essenceRealm = essenceRealm;
            this.bodyRealm = bodyRealm;
            this.intentRealm = intentRealm;
            this.health = health;
            this.maxHealth = maxHealth;
            this.isPlayer = isPlayer;
            this.detectionTime = System.currentTimeMillis();
            this.entityType = entityType;
            this.count = 1;
            this.canSeeDetails = canSeeDetails;
        }

        // Constructor for grouped entities
        public EntityInfo(String name, EntityType<?> entityType, int count, float totalHealth, float totalMaxHealth) {
            this.entityId = -1; // No specific entity ID for groups
            this.entityUuid = UUID.randomUUID(); // Generate a random UUID for the group
            this.name = name;
            this.essenceRealm = 0;
            this.bodyRealm = 0;
            this.intentRealm = 0;
            this.health = totalHealth / count; // Average health
            this.maxHealth = totalMaxHealth / count; // Average max health
            this.isPlayer = false;
            this.detectionTime = System.currentTimeMillis();
            this.entityType = entityType;
            this.count = count;
            this.canSeeDetails = true; // Mobs always show details
        }

        String getDisplayName() {
            if (count > 1) {
                return name + " x" + count;
            }
            return name;
        }

        boolean isGrouped() {
            return count > 1;
        }

        // Helper method to get highest realm for comparison
        int getHighestRealm() {
            return Math.max(Math.max(essenceRealm, bodyRealm), intentRealm);
        }
    }

    public static class PlayerSenseInfo {
        final List<EntityInfo> entities;
        final int totalEntities;
        final int totalGroups; // Total number of groups/individual entities displayed
        final long updateTime;

        public PlayerSenseInfo(List<EntityInfo> entities, int totalEntities, int totalGroups) {
            this.entities = entities;
            this.totalEntities = totalEntities;
            this.totalGroups = totalGroups;
            this.updateTime = System.currentTimeMillis();
        }
    }

    public static void updateDetectedEntities(Level level, Player player) {
        if (level.isClientSide()) return;

        UUID playerId = player.getUUID();
        SenseData senseData = playerSenseData.get(playerId);
        if (senseData == null) return;

        long currentTime = System.currentTimeMillis();

        // Check if sense is still active
        if (!senseData.isActive(currentTime)) {
            clearPlayerSenseData(level, playerId);
            playerSenseData.remove(playerId);
            playerDetectedEntities.remove(playerId);
            playerSenseInfo.remove(playerId);
            playerGlowingEntities.remove(playerId);

            // Send packet to client to clear sense data
            if (player instanceof ServerPlayer serverPlayer) {
                ClearSpiritualSensePacket packet = new ClearSpiritualSensePacket(playerId);
                serverPlayer.connection.send(packet);
            }
            return;
        }

        // Update the center to follow the player
        senseData.center = player.position();

        float currentRadius = senseData.getCurrentRadius(currentTime);

        // Create AABB for detection
        AABB detectionBox = new AABB(
                senseData.center.x - currentRadius,
                senseData.center.y - currentRadius,
                senseData.center.z - currentRadius,
                senseData.center.x + currentRadius,
                senseData.center.y + currentRadius,
                senseData.center.z + currentRadius
        );

        // Get all entities in the detection area
        List<Entity> entities = level.getEntities(null, detectionBox);
        Map<Integer, EntityInfo> detectedEntities = new HashMap<>();
        Set<Integer> currentGlowingEntities = new HashSet<>();

        // Group non-player entities by type
        Map<EntityType<?>, List<Entity>> entityGroups = new HashMap<>();
        List<EntityInfo> playerEntities = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity == player) continue; // Skip the player themselves

            int entityId = entity.getId();
            currentGlowingEntities.add(entityId);

            // Set glowing effect
            entity.setGlowingTag(true);

            boolean isPlayer = entity instanceof Player;

            if (isPlayer) {
                // Players are handled individually, not grouped
                Player targetPlayer = (Player) entity;
                int essenceRealm = targetPlayer.getData(ModAttachments.PLAYER_DATA)
                        .getCultivationData().getPathData("ascension:essence").majorRealm;
                int bodyRealm = targetPlayer.getData(ModAttachments.PLAYER_DATA)
                        .getCultivationData().getPathData("ascension:body").majorRealm;
                int intentRealm = targetPlayer.getData(ModAttachments.PLAYER_DATA)
                        .getCultivationData().getPathData("ascension:intent").majorRealm;

                float health = targetPlayer.getHealth();
                float maxHealth = targetPlayer.getMaxHealth();

                // Check if user can see target's details (user's realm must be >= target's highest realm)
                boolean canSeeDetails = true;
                int targetHighestRealm = Math.max(Math.max(essenceRealm, bodyRealm), intentRealm);
                if (senseData.playerRealm < targetHighestRealm) {
                    // User cannot see details, show default MC values
                    canSeeDetails = false;
                    // Show health as 20 (default MC health) but capped at actual health if lower
                    health = Math.min(health, 20.0f);
                    maxHealth = 20.0f;
                    // Don't show actual realm values (they will be hidden in display)
                }

                EntityInfo entityInfo = new EntityInfo(
                        entityId, entity.getUUID(), entity.getName().getString(),
                        essenceRealm, bodyRealm, intentRealm,
                        health, maxHealth, true, EntityType.PLAYER, canSeeDetails
                );

                playerEntities.add(entityInfo);
                detectedEntities.put(entityId, entityInfo);
            } else {
                // Group non-player entities by type
                EntityType<?> entityType = entity.getType();
                entityGroups.computeIfAbsent(entityType, k -> new ArrayList<>()).add(entity);
            }
        }

        // Process grouped entities
        for (Map.Entry<EntityType<?>, List<Entity>> entry : entityGroups.entrySet()) {
            EntityType<?> entityType = entry.getKey();
            List<Entity> groupEntities = entry.getValue();

            if (groupEntities.size() == 1) {
                // Single entity, don't group
                Entity entity = groupEntities.get(0);
                float health = 0;
                float maxHealth = 0;
                if (entity instanceof LivingEntity livingEntity) {
                    health = livingEntity.getHealth();
                    maxHealth = livingEntity.getMaxHealth();
                }

                EntityInfo entityInfo = new EntityInfo(
                        entity.getId(), entity.getUUID(), entity.getName().getString(),
                        0, 0, 0, health, maxHealth, false, entityType, true
                );

                detectedEntities.put(entity.getId(), entityInfo);
            } else {
                // Group multiple entities of same type
                float totalHealth = 0;
                float totalMaxHealth = 0;

                for (Entity entity : groupEntities) {
                    if (entity instanceof LivingEntity livingEntity) {
                        totalHealth += livingEntity.getHealth();
                        totalMaxHealth += livingEntity.getMaxHealth();
                    }
                }

                String entityName = groupEntities.get(0).getName().getString();
                EntityInfo groupInfo = new EntityInfo(
                        entityName, entityType, groupEntities.size(), totalHealth, totalMaxHealth
                );

                // Use the first entity's ID for the group
                detectedEntities.put(groupEntities.get(0).getId(), groupInfo);
            }
        }

        // Add player entities to detectedEntities
        for (EntityInfo playerInfo : playerEntities) {
            detectedEntities.put(playerInfo.entityId, playerInfo);
        }

        // Clear glowing for entities that are no longer detected
        Set<Integer> previousGlowing = playerGlowingEntities.get(playerId);
        if (previousGlowing != null) {
            for (int entityId : previousGlowing) {
                if (!currentGlowingEntities.contains(entityId)) {
                    Entity entity = level.getEntity(entityId);
                    if (entity != null) {
                        entity.setGlowingTag(false);
                    }
                }
            }
        }

        playerDetectedEntities.put(playerId, detectedEntities);
        playerGlowingEntities.put(playerId, currentGlowingEntities);

        // Create player sense info for display
        List<EntityInfo> entityList = new ArrayList<>(detectedEntities.values());
        entityList.sort((a, b) -> {
            // Players first, then grouped entities, then by name
            if (a.isPlayer && !b.isPlayer) return -1;
            if (!a.isPlayer && b.isPlayer) return 1;
            if (a.isGrouped() && !b.isGrouped()) return -1;
            if (!a.isGrouped() && b.isGrouped()) return 1;
            return a.name.compareToIgnoreCase(b.name);
        });

        int totalGroups = entityList.size();
        PlayerSenseInfo senseInfo = new PlayerSenseInfo(entityList, entities.size() - 1, totalGroups); // -1 for player themselves
        playerSenseInfo.put(playerId, senseInfo);

        // Send packet to client to sync detected entities
        if (player instanceof ServerPlayer serverPlayer) {
            // Convert EntityInfo list to EntityData list for packet
            List<SyncSpiritualSenseEntitiesPacket.EntityData> entityDataList = new ArrayList<>();
            for (EntityInfo entityInfo : entityList) {
                entityDataList.add(new SyncSpiritualSenseEntitiesPacket.EntityData(
                        entityInfo.entityId,
                        entityInfo.entityUuid,
                        entityInfo.name,
                        entityInfo.essenceRealm,
                        entityInfo.bodyRealm,
                        entityInfo.intentRealm,
                        entityInfo.health,
                        entityInfo.maxHealth,
                        entityInfo.isPlayer,
                        entityInfo.canSeeDetails,
                        entityInfo.count,
                        entityInfo.isGrouped()
                ));
            }

            SyncSpiritualSenseEntitiesPacket packet = new SyncSpiritualSenseEntitiesPacket(
                    playerId,
                    entityDataList,
                    entities.size() - 1,
                    totalGroups
            );
            serverPlayer.connection.send(packet);
        }
    }

    public static boolean hasActiveSense(Player player) {
        SenseData data = playerSenseData.get(player.getUUID());
        return data != null && data.isActive(System.currentTimeMillis());
    }

    public static PlayerSenseInfo getPlayerSenseInfo(Player player) {
        return playerSenseInfo.get(player.getUUID());
    }

    // Server tick handler for updating detected entities
    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
    public static class ServerTickHandler {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Pre event) {
            if (!event.getEntity().level().isClientSide()) {
                updateDetectedEntities(event.getEntity().level(), event.getEntity());
            }
        }
    }

    // Client-side rendering
    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class SpiritualSenseRenderer {

        private static final int SPHERE_SEGMENTS = 64;
        private static final int SPHERE_RINGS = 32;
        private static final float SPHERE_OPACITY = 0.6f;
        private static final int SPHERE_COLOR = 0x3366FF; // Blue color

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
            SenseData senseData = playerSenseData.get(playerId);
            if (senseData == null) {
                return;
            }

            long currentTime = System.currentTimeMillis();
            if (!senseData.isActive(currentTime)) {
                // Clear glow effects when sense ends
                playerSenseData.remove(playerId);
                playerSenseInfo.remove(playerId);
                return;
            }

            Camera camera = mc.gameRenderer.getMainCamera();
            Vec3 cameraPos = camera.getPosition();

            float currentRadius = senseData.getCurrentRadius(currentTime);

            // Update the sphere center to follow the player
            Vec3 playerPosition = mc.player.position();
            senseData.center = playerPosition; // Update center to player's current position

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

            // Save OpenGL state
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();

            // Use proper shader
            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            // Render the expanding sphere
            renderSphere(poseStack, buffers, senseData.center, currentRadius, cameraPos);

            buffers.endBatch();

            // Restore OpenGL state
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
        }

        private static void renderSphere(PoseStack poseStack, MultiBufferSource.BufferSource buffers,
                                         Vec3 center, float radius, Vec3 cameraPos) {
            poseStack.pushPose();
            poseStack.translate(center.x - cameraPos.x, center.y - cameraPos.y, center.z - cameraPos.z);

            // Create a custom render type for position-only rendering
            VertexConsumer consumer = buffers.getBuffer(RenderType.debugQuads());
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix = pose.pose();

            int color = SPHERE_COLOR;
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            int a = (int)(SPHERE_OPACITY * 255);

            // Draw a simple wireframe sphere using position-only vertices
            // We'll draw rings and meridians separately for clarity
            drawWireframeSphere(consumer, matrix, radius, r, g, b, a);

            poseStack.popPose();
        }

        private static void drawWireframeSphere(VertexConsumer consumer, Matrix4f matrix, float radius,
                                                int r, int g, int b, int a) {
            // Draw latitude rings
            for (int i = 0; i <= SPHERE_RINGS; i++) {
                float theta = (float) i / SPHERE_RINGS * (float) Math.PI;
                float y = radius * (float) Math.cos(theta);
                float ringRadius = radius * (float) Math.sin(theta);

                drawRing(consumer, matrix, ringRadius, y, r, g, b, a);
            }

            // Draw longitude lines
            for (int i = 0; i < SPHERE_SEGMENTS; i++) {
                float phi = (float) i / SPHERE_SEGMENTS * 2.0f * (float) Math.PI;
                drawMeridian(consumer, matrix, radius, phi, r, g, b, a);
            }
        }

        private static void drawRing(VertexConsumer consumer, Matrix4f matrix, float ringRadius, float y,
                                     int r, int g, int b, int a) {
            float prevX = ringRadius;
            float prevZ = 0;

            for (int i = 1; i <= SPHERE_SEGMENTS; i++) {
                float angle = (float) i / SPHERE_SEGMENTS * 2.0f * (float) Math.PI;
                float x = ringRadius * (float) Math.cos(angle);
                float z = ringRadius * (float) Math.sin(angle);

                // Draw line from previous point to current point
                consumer.addVertex(matrix, prevX, y, prevZ).setColor(r, g, b, a);
                consumer.addVertex(matrix, x, y, z).setColor(r, g, b, a);

                prevX = x;
                prevZ = z;
            }
        }

        private static void drawMeridian(VertexConsumer consumer, Matrix4f matrix, float radius, float phi,
                                         int r, int g, int b, int a) {
            float prevX = radius * (float) Math.sin(0) * (float) Math.cos(phi);
            float prevY = radius * (float) Math.cos(0);
            float prevZ = radius * (float) Math.sin(0) * (float) Math.sin(phi);

            for (int i = 1; i <= SPHERE_RINGS; i++) {
                float theta = (float) i / SPHERE_RINGS * (float) Math.PI;
                float x = radius * (float) Math.sin(theta) * (float) Math.cos(phi);
                float y = radius * (float) Math.cos(theta);
                float z = radius * (float) Math.sin(theta) * (float) Math.sin(phi);

                // Draw line from previous point to current point
                consumer.addVertex(matrix, prevX, prevY, prevZ).setColor(r, g, b, a);
                consumer.addVertex(matrix, x, y, z).setColor(r, g, b, a);

                prevX = x;
                prevY = y;
                prevZ = z;
            }
        }
    }

    // GUI overlay for displaying entity information
    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class SpiritualSenseOverlay {

        private static final long DISPLAY_DURATION = 7000; // 7 seconds
        private static final int ENTITIES_PER_PAGE = 5; // Number of entities to show at once
        private static final long SCROLL_INTERVAL = 1500; // 1.5 seconds per entity/page

        // Store scroll state per player
        private static final Map<UUID, ScrollState> playerScrollStates = new ConcurrentHashMap<>();

        private static class ScrollState {
            long lastScrollTime;
            int currentOffset; // How many entities we've scrolled past
            int entitiesShown; // How many entities were in the last scan

            ScrollState() {
                this.lastScrollTime = System.currentTimeMillis();
                this.currentOffset = 0;
                this.entitiesShown = 0;
            }
        }

        @SubscribeEvent
        public static void onRenderGui(RenderGuiEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) {
                return;
            }

            // Check if player has active spiritual sense
            if (!hasActiveSense(mc.player)) {
                playerScrollStates.remove(mc.player.getUUID());
                return;
            }

            PlayerSenseInfo senseInfo = getPlayerSenseInfo(mc.player);
            if (senseInfo == null || senseInfo.entities.isEmpty()) {
                return;
            }

            // Check if we should still display
            if (System.currentTimeMillis() - senseInfo.updateTime > DISPLAY_DURATION) {
                playerScrollStates.remove(mc.player.getUUID());
                return;
            }

            renderEntityInfoOverlay(event.getGuiGraphics(), senseInfo, mc);
        }

        private static void renderEntityInfoOverlay(GuiGraphics guiGraphics, PlayerSenseInfo senseInfo, Minecraft mc) {
            Font font = mc.font;
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            // Position on right side of screen - reduced size
            int rightMargin = 15;
            int topMargin = 40;
            int maxWidth = 220; // Reduced from 300 for smaller overlay

            // Calculate total height needed (fixed height for cycling display)
            int lineHeight = font.lineHeight + 1; // Reduced spacing
            int headerLines = 2; // Title + separator
            int totalHeight = (headerLines + ENTITIES_PER_PAGE) * lineHeight + 15; // Reduced padding

            int startX = screenWidth - maxWidth - rightMargin;
            int startY = topMargin;

            // Draw background
            int bgColor = 0x80000000; // Semi-transparent black
            guiGraphics.fill(startX - 5, startY - 5,
                    screenWidth - rightMargin + 5, startY + totalHeight,
                    bgColor);

            // Draw border
            int borderColor = 0x803366FF; // Blue border
            guiGraphics.fill(startX - 5, startY - 5, startX - 4, startY + totalHeight, borderColor); // Left
            guiGraphics.fill(startX - 5, startY - 5, screenWidth - rightMargin + 5, startY - 4, borderColor); // Top
            guiGraphics.fill(screenWidth - rightMargin + 4, startY - 5,
                    screenWidth - rightMargin + 5, startY + totalHeight, borderColor); // Right
            guiGraphics.fill(startX - 5, startY + totalHeight - 1,
                    screenWidth - rightMargin + 5, startY + totalHeight, borderColor); // Bottom

            // Draw title with pagination info
            UUID playerId = mc.player.getUUID();
            ScrollState scrollState = playerScrollStates.computeIfAbsent(playerId, k -> new ScrollState());

            // Update scroll offset if enough time has passed
            long currentTime = System.currentTimeMillis();
            if (currentTime - scrollState.lastScrollTime > SCROLL_INTERVAL) {
                scrollState.lastScrollTime = currentTime;
                scrollState.currentOffset += ENTITIES_PER_PAGE;

                // If we've scrolled past all entities, reset to start
                if (scrollState.currentOffset >= senseInfo.entities.size()) {
                    scrollState.currentOffset = 0;
                }
            }

            scrollState.entitiesShown = senseInfo.entities.size();

            // Calculate current page info
            int totalPages = (int) Math.ceil((double) senseInfo.entities.size() / ENTITIES_PER_PAGE);
            int currentPage = totalPages > 0 ? (scrollState.currentOffset / ENTITIES_PER_PAGE) + 1 : 1;

            MutableComponent title = Component.literal("§9Sense §7(" + senseInfo.totalEntities + ")"); // Simplified title
            if (totalPages > 1) {
                title.append(Component.literal(" §8[" + currentPage + "/" + totalPages + "]"));
            }

            guiGraphics.drawString(font, title, startX, startY, 0xFFFFFF, false);

            // Draw separator
            guiGraphics.fill(startX, startY + lineHeight, startX + maxWidth, startY + lineHeight + 1, 0x803366FF);

            // Draw entity information for current page
            int yOffset = startY + lineHeight * 2;
            int entitiesDrawn = 0;

            for (int i = scrollState.currentOffset;
                 i < senseInfo.entities.size() && entitiesDrawn < ENTITIES_PER_PAGE;
                 i++, entitiesDrawn++) {

                EntityInfo entity = senseInfo.entities.get(i);

                if (entity.isPlayer) {
                    // Player info with realms and health
                    renderPlayerInfo(guiGraphics, font, entity, startX, yOffset, maxWidth);
                } else {
                    // Entity info - use getDisplayName() which includes count for groups
                    renderEntityInfo(guiGraphics, font, entity, startX, yOffset, maxWidth);
                }

                yOffset += lineHeight;
            }

            // If we didn't fill all slots, show a "scanning..." message
            if (entitiesDrawn < ENTITIES_PER_PAGE && scrollState.currentOffset == 0) {
                // This is the first page and we have less than a full page
                for (int i = entitiesDrawn; i < ENTITIES_PER_PAGE; i++) {
                    if (i == entitiesDrawn) {
                        Component scanningText = Component.literal("§8... scanning ...");
                        guiGraphics.drawString(font, scanningText, startX, yOffset, 0xAAAAAA, false);
                    }
                    yOffset += lineHeight;
                }
            } else if (entitiesDrawn < ENTITIES_PER_PAGE) {
                // We've reached the end, show end indicator
                Component endText = Component.literal("§8... end ...");
                guiGraphics.drawString(font, endText, startX, yOffset, 0xAAAAAA, false);
                yOffset += lineHeight;

                // Calculate when next scan will start (based on scroll interval)
                long timeSinceLastScroll = currentTime - scrollState.lastScrollTime;
                long timeUntilNextCycle = SCROLL_INTERVAL - timeSinceLastScroll;
                int seconds = (int) Math.ceil(timeUntilNextCycle / 1000.0);

                if (seconds > 0) {
                    Component cycleText = Component.literal("§8Next: " + seconds + "s");
                    guiGraphics.drawString(font, cycleText, startX, yOffset, 0xAAAAAA, false);
                }
            }

            // Draw scroll indicator at bottom
            yOffset = startY + totalHeight - lineHeight + 2;
            Component scrollIndicator = Component.literal("§7↕ " + (scrollState.currentOffset + 1) + "-" +
                    Math.min(scrollState.currentOffset + ENTITIES_PER_PAGE, senseInfo.entities.size()));
            guiGraphics.drawString(font, scrollIndicator, startX, yOffset, 0xAAAAAA, false);
        }

        private static void renderPlayerInfo(GuiGraphics guiGraphics, Font font, EntityInfo player,
                                             int x, int y, int maxWidth) {
            // Player name with player icon
            String namePrefix = "§b☻ "; // Player icon
            String nameText = namePrefix + player.name;

            // Health bar
            float healthPercent = player.maxHealth > 0 ? player.health / player.maxHealth : 1.0f;
            int healthColor = getHealthColor(healthPercent);
            String healthText = String.format(" §c♥ %.1f/%.1f", player.health, player.maxHealth);

            // Realms - using the three cultivation paths
            String realmsText;
            if (player.canSeeDetails) {
                realmsText = String.format(" §7[§eE%s §6B%s §dI%s§7]",
                        player.essenceRealm, player.bodyRealm, player.intentRealm);
            } else {
                realmsText = " §7[§8???§7]"; // Hidden realms for higher realm players
            }

            // Combine all parts
            String fullText = nameText + healthText + realmsText;

            // Check if text fits
            int textWidth = font.width(fullText);
            if (textWidth > maxWidth) {
                // Truncate name if needed
                int availableWidth = maxWidth - font.width(healthText + realmsText) - 10;
                String truncatedName = truncateText(font, player.name, availableWidth);
                fullText = namePrefix + truncatedName + healthText + realmsText;
            }

            guiGraphics.drawString(font, fullText, x, y, 0xFFFFFF, false);

            // Draw health bar background (smaller)
            int barWidth = 60; // Reduced from 80
            int barHeight = 2; // Reduced from 3
            int barX = x;
            int barY = y + font.lineHeight - 2;

            guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0x80000000);

            // Draw health bar
            int healthWidth = (int)(barWidth * healthPercent);
            guiGraphics.fill(barX, barY, barX + healthWidth, barY + barHeight, healthColor);
        }

        private static void renderEntityInfo(GuiGraphics guiGraphics, Font font, EntityInfo entity,
                                             int x, int y, int maxWidth) {
            // Entity name with appropriate icon - use getDisplayName() to show count for groups
            String icon = "§8⚲ "; // Generic entity icon
            String entityName = entity.getDisplayName(); // This includes "xN" for groups

            // Apply special icons for common mob types
            String lowerName = entityName.toLowerCase();
            if (lowerName.contains("zombie")) icon = "§2☠ ";
            else if (lowerName.contains("skeleton")) icon = "§7☠ ";
            else if (lowerName.contains("spider")) icon = "§8🕷 ";
            else if (lowerName.contains("creeper")) icon = "§a💣 ";
            else if (lowerName.contains("enderman")) icon = "§5👁 ";

            String nameText = icon + entityName;

            // Health if available
            String healthText = "";
            if (entity.maxHealth > 0) {
                float healthPercent = entity.health / entity.maxHealth;
                healthText = String.format(" §c♥ %.1f/%.1f", entity.health, entity.maxHealth);
            }

            String fullText = nameText + healthText;

            guiGraphics.drawString(font, fullText, x, y, 0xFFFFFF, false);

            // Draw health bar if entity has health (smaller)
            if (entity.maxHealth > 0) {
                float healthPercent = entity.health / entity.maxHealth;
                int healthColor = getHealthColor(healthPercent);

                int barWidth = 45; // Reduced from 60
                int barHeight = 2;
                int barX = x;
                int barY = y + font.lineHeight - 2;

                guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0x80000000);

                int healthWidth = (int)(barWidth * healthPercent);
                guiGraphics.fill(barX, barY, barX + healthWidth, barY + barHeight, healthColor);
            }
        }

        private static int getHealthColor(float healthPercent) {
            if (healthPercent > 0.7f) return 0x8000FF00; // Green
            if (healthPercent > 0.3f) return 0x80FFFF00; // Yellow
            return 0x80FF0000; // Red
        }

        private static String truncateText(Font font, String text, int maxWidth) {
            if (font.width(text) <= maxWidth) return text;

            String ellipsis = "...";
            int ellipsisWidth = font.width(ellipsis);

            for (int i = text.length() - 1; i >= 0; i--) {
                String truncated = text.substring(0, i) + ellipsis;
                if (font.width(truncated) <= maxWidth) {
                    return truncated;
                }
            }

            return ellipsis;
        }
    }
}