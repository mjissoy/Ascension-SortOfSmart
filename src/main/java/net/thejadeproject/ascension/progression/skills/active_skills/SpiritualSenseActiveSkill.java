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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpiritualSenseActiveSkill extends AbstractActiveSkill {

    private static final String SKILL_ID = "ascension:spiritual_sense";

    // Client-side rendering data (not persisted)
    private static final Map<UUID, SenseData> CLIENT_SENSE_DATA = new ConcurrentHashMap<>();
    private static final Map<UUID, PlayerSenseInfo> CLIENT_SENSE_INFO = new ConcurrentHashMap<>();
    private static final Map<UUID, Set<Integer>> CLIENT_GLOWING_ENTITIES = new ConcurrentHashMap<>();

    // Server-side transient active sessions (not persisted)
    private static final Map<UUID, SenseSession> ACTIVE_SESSIONS = new ConcurrentHashMap<>();

    // Persistent data class
    public static class SpiritualSenseData implements IPersistentSkillData {
        private int cooldownTicks = 0;

        @Override
        public CompoundTag writeData() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("Cooldown", cooldownTicks);
            return tag;
        }

        @Override
        public void readData(CompoundTag tag) {
            this.cooldownTicks = tag.getInt("Cooldown");
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf) {
            buf.writeInt(cooldownTicks);
        }

        @Override
        public void decode(RegistryFriendlyByteBuf buf) {
            this.cooldownTicks = buf.readInt();
        }

        public int getCooldown() { return cooldownTicks; }
        public void setCooldown(int cd) { this.cooldownTicks = cd; }
        public void decrementCooldown() { if (cooldownTicks > 0) cooldownTicks--; }
    }

    // Server-side session data (transient)
    public static class SenseSession {
        public Vec3 center;
        public final float maxRadius;
        public final long startTime;
        public final long expansionEndTime;
        public final long expansionDuration;
        public final long activeDuration;
        public final int playerRealm;
        public boolean expansionComplete = false;
        public final Map<Integer, EntityInfo> detectedEntities = new HashMap<>();
        public final Set<Integer> glowingEntities = new HashSet<>();

        public SenseSession(Vec3 center, float maxRadius, long startTime, long expansionEndTime,
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
            if (expansionComplete) return maxRadius;
            float progress = Math.min(1.0f, (float)(currentTime - startTime) / expansionDuration);
            if (progress >= 1.0f) expansionComplete = true;
            return maxRadius * progress;
        }

        public boolean isActive(long currentTime) {
            return currentTime < expansionEndTime + activeDuration;
        }
    }

    // Client-side data class
    public static class SenseData {
        public Vec3 center;
        public final float maxRadius;
        public final long startTime;
        public final long expansionEndTime;
        public final long expansionDuration;
        public final long activeDuration;
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
            if (expansionComplete) return maxRadius;
            float progress = Math.min(1.0f, (float)(currentTime - startTime) / expansionDuration);
            if (progress >= 1.0f) expansionComplete = true;
            return maxRadius * progress;
        }

        public boolean isActive(long currentTime) {
            return currentTime < expansionEndTime + activeDuration;
        }
    }

    public static class EntityInfo {
        public final int entityId;
        public final UUID entityUuid;
        public final String name;
        public final int essenceRealm;
        public final int bodyRealm;
        public final int intentRealm;
        public final float health;
        public final float maxHealth;
        public final boolean isPlayer;
        public final EntityType<?> entityType;
        public final int count;
        public final boolean canSeeDetails;

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
            this.entityType = entityType;
            this.count = 1;
            this.canSeeDetails = canSeeDetails;
        }

        public EntityInfo(String name, EntityType<?> entityType, int count, float totalHealth, float totalMaxHealth) {
            this.entityId = -1;
            this.entityUuid = UUID.randomUUID();
            this.name = name;
            this.essenceRealm = 0;
            this.bodyRealm = 0;
            this.intentRealm = 0;
            this.health = totalHealth / count;
            this.maxHealth = totalMaxHealth / count;
            this.isPlayer = false;
            this.entityType = entityType;
            this.count = count;
            this.canSeeDetails = true;
        }

        public String getDisplayName() {
            return count > 1 ? name + " x" + count : name;
        }

        public boolean isGrouped() {
            return count > 1;
        }

        public int getHighestRealm() {
            return Math.max(Math.max(essenceRealm, bodyRealm), intentRealm);
        }
    }

    public static class PlayerSenseInfo {
        public final List<EntityInfo> entities;
        public final int totalEntities;
        public final int totalGroups;
        public final long updateTime;

        public PlayerSenseInfo(List<EntityInfo> entities, int totalEntities, int totalGroups) {
            this.entities = entities;
            this.totalEntities = totalEntities;
            this.totalGroups = totalGroups;
            this.updateTime = System.currentTimeMillis();
        }
    }

    public SpiritualSenseActiveSkill() {
        super(Component.translatable("ascension.skill.active.spiritual_sense"));
        this.path = "ascension:essence";
        this.qiCost = 20.0;
    }

    private static SpiritualSenseData getData(Player player) {
        var skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        var metaData = skillData.getActiveSkill(SKILL_ID);
        if (metaData != null && metaData.data instanceof SpiritualSenseData data) {
            return data;
        }
        return null;
    }

    public static Map<UUID, SenseData> getPlayerSenseDataMap() {
        return CLIENT_SENSE_DATA;
    }

    public static Map<UUID, PlayerSenseInfo> getPlayerSenseInfoMap() {
        return CLIENT_SENSE_INFO;
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
        return 20 * 60;
    }

    @Override
    public int maxCastingTicks() {
        return 0;
    }

    @Override
    public void cast(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        if (level.isClientSide()) return;

        SpiritualSenseData data = getData(player);
        if (data == null) {
            // Debug line - remove after fixing
            AscensionCraft.LOGGER.warn("Spiritual Sense cast failed: data is null for player {}", player.getName().getString());
            player.displayClientMessage(Component.literal("§cSkill data not initialized. Try relogging."), true);
            return;
        }

        UUID playerId = player.getUUID();
        clearPlayerSenseData(level, playerId);
        ACTIVE_SESSIONS.remove(playerId);

        int majorRealm = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(this.path).majorRealm;
        float maxRadius = 16.0f + (majorRealm * 8.0f);
        long startTime = System.currentTimeMillis();
        long expansionDuration = 2000;
        long activeDuration = 8000 + (majorRealm * 4000);
        long expansionEndTime = startTime + expansionDuration;

        SenseSession session = new SenseSession(
                player.position(), maxRadius, startTime, expansionEndTime,
                expansionDuration, activeDuration, majorRealm
        );

        ACTIVE_SESSIONS.put(playerId, session);

        data.setCooldown(getCooldown());
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);

        if (player instanceof ServerPlayer serverPlayer) {
            SyncSpiritualSensePacket packet = new SyncSpiritualSensePacket(
                    playerId, player.position(), maxRadius, startTime, expansionEndTime,
                    expansionDuration, activeDuration, majorRealm
            );
            serverPlayer.connection.send(packet);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.ENDER_EYE_DEATH,
                SoundSource.PLAYERS, 1.0F, 1.2F);
    }

    @Override
    public void onPreCast() {}

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        UUID playerId = player.getUUID();
        clearPlayerSenseData(player.level(), playerId);
        ACTIVE_SESSIONS.remove(playerId);

        if (player instanceof ServerPlayer serverPlayer) {
            ClearSpiritualSensePacket packet = new ClearSpiritualSensePacket(playerId);
            serverPlayer.connection.send(packet);
        }
    }

    private static void clearPlayerSenseData(Level level, UUID playerId) {
        SenseSession session = ACTIVE_SESSIONS.get(playerId);
        if (session != null && session.glowingEntities != null) {
            for (int entityId : session.glowingEntities) {
                Entity entity = level.getEntity(entityId);
                if (entity != null) entity.setGlowingTag(false);
            }
            session.glowingEntities.clear();
        }
    }

    public static void updateDetectedEntities(Level level, Player player) {
        if (level.isClientSide()) return;

        UUID playerId = player.getUUID();
        SenseSession session = ACTIVE_SESSIONS.get(playerId);
        if (session == null) return;

        SpiritualSenseData data = getData(player);
        if (data != null && data.getCooldown() > 0) {
            data.decrementCooldown();
            if (data.getCooldown() == 0 || data.getCooldown() % 20 == 0) {
                player.syncData(ModAttachments.PLAYER_SKILL_DATA);
            }
        }

        long currentTime = System.currentTimeMillis();
        if (!session.isActive(currentTime)) {
            clearPlayerSenseData(level, playerId);
            ACTIVE_SESSIONS.remove(playerId);

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClearSpiritualSensePacket(playerId));
            }
            return;
        }

        session.center = player.position();
        float currentRadius = session.getCurrentRadius(currentTime);

        AABB detectionBox = new AABB(
                session.center.x - currentRadius, session.center.y - currentRadius, session.center.z - currentRadius,
                session.center.x + currentRadius, session.center.y + currentRadius, session.center.z + currentRadius
        );

        List<Entity> entities = level.getEntities(null, detectionBox);
        Map<Integer, EntityInfo> detectedEntities = new HashMap<>();
        Set<Integer> currentGlowing = new HashSet<>();
        List<EntityInfo> playerEntities = new ArrayList<>();
        Map<EntityType<?>, List<Entity>> entityGroups = new HashMap<>();

        for (Entity entity : entities) {
            if (entity == player) continue;
            int entityId = entity.getId();
            currentGlowing.add(entityId);
            entity.setGlowingTag(true);

            if (entity instanceof Player targetPlayer) {
                int essenceRealm = targetPlayer.getData(ModAttachments.PLAYER_DATA)
                        .getCultivationData().getPathData("ascension:essence").majorRealm;
                int bodyRealm = targetPlayer.getData(ModAttachments.PLAYER_DATA)
                        .getCultivationData().getPathData("ascension:body").majorRealm;
                int intentRealm = targetPlayer.getData(ModAttachments.PLAYER_DATA)
                        .getCultivationData().getPathData("ascension:intent").majorRealm;

                float health = targetPlayer.getHealth();
                float maxHealth = targetPlayer.getMaxHealth();
                int targetHighestRealm = Math.max(Math.max(essenceRealm, bodyRealm), intentRealm);
                boolean canSeeDetails = session.playerRealm >= targetHighestRealm;

                if (!canSeeDetails) {
                    health = Math.min(health, 20.0f);
                    maxHealth = 20.0f;
                }

                playerEntities.add(new EntityInfo(entityId, entity.getUUID(), entity.getName().getString(),
                        essenceRealm, bodyRealm, intentRealm, health, maxHealth, true, EntityType.PLAYER, canSeeDetails));
            } else {
                entityGroups.computeIfAbsent(entity.getType(), k -> new ArrayList<>()).add(entity);
            }
        }

        for (var entry : entityGroups.entrySet()) {
            EntityType<?> type = entry.getKey();
            List<Entity> group = entry.getValue();

            if (group.size() == 1) {
                Entity e = group.get(0);
                float health = 0, maxHealth = 0;
                if (e instanceof LivingEntity le) {
                    health = le.getHealth();
                    maxHealth = le.getMaxHealth();
                }
                detectedEntities.put(e.getId(), new EntityInfo(e.getId(), e.getUUID(), e.getName().getString(),
                        0, 0, 0, health, maxHealth, false, type, true));
            } else {
                float totalHealth = 0, totalMaxHealth = 0;
                for (Entity e : group) {
                    if (e instanceof LivingEntity le) {
                        totalHealth += le.getHealth();
                        totalMaxHealth += le.getMaxHealth();
                    }
                }
                String name = group.get(0).getName().getString();
                detectedEntities.put(group.get(0).getId(), new EntityInfo(name, type, group.size(), totalHealth, totalMaxHealth));
            }
        }

        playerEntities.forEach(e -> detectedEntities.put(e.entityId, e));

        Set<Integer> previousGlowing = session.glowingEntities;
        for (int id : previousGlowing) {
            if (!currentGlowing.contains(id)) {
                Entity e = level.getEntity(id);
                if (e != null) e.setGlowingTag(false);
            }
        }
        session.glowingEntities.clear();
        session.glowingEntities.addAll(currentGlowing);

        List<EntityInfo> entityList = new ArrayList<>(detectedEntities.values());
        entityList.sort((a, b) -> {
            if (a.isPlayer && !b.isPlayer) return -1;
            if (!a.isPlayer && b.isPlayer) return 1;
            if (a.isGrouped() && !b.isGrouped()) return -1;
            if (!a.isGrouped() && b.isGrouped()) return 1;
            return a.name.compareToIgnoreCase(b.name);
        });

        PlayerSenseInfo senseInfo = new PlayerSenseInfo(entityList, entities.size() - 1, entityList.size());

        if (player instanceof ServerPlayer serverPlayer) {
            List<SyncSpiritualSenseEntitiesPacket.EntityData> entityDataList = new ArrayList<>();
            for (EntityInfo info : entityList) {
                entityDataList.add(new SyncSpiritualSenseEntitiesPacket.EntityData(
                        info.entityId, info.entityUuid, info.name, info.essenceRealm, info.bodyRealm,
                        info.intentRealm, info.health, info.maxHealth, info.isPlayer, info.canSeeDetails,
                        info.count, info.isGrouped()
                ));
            }

            SyncSpiritualSenseEntitiesPacket packet = new SyncSpiritualSenseEntitiesPacket(
                    playerId, entityDataList, entities.size() - 1, entityList.size()
            );
            serverPlayer.connection.send(packet);
        }
    }

    public static boolean hasActiveSense(Player player) {
        if (player.level().isClientSide()) {
            return CLIENT_SENSE_DATA.containsKey(player.getUUID());
        }
        SenseSession session = ACTIVE_SESSIONS.get(player.getUUID());
        return session != null && session.isActive(System.currentTimeMillis());
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance() {
        return new SpiritualSenseData();
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(CompoundTag tag) {
        SpiritualSenseData data = new SpiritualSenseData();
        data.readData(tag);
        return data;
    }

    @EventBusSubscriber(modid = AscensionCraft.MOD_ID)
    public static class ServerTickHandler {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Pre event) {
            if (!event.getEntity().level().isClientSide()) {
                updateDetectedEntities(event.getEntity().level(), event.getEntity());
            }
        }
    }

    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, value = Dist.CLIENT)
    public static class SpiritualSenseRenderer {

        private static final int SPHERE_SEGMENTS = 64;
        private static final int SPHERE_RINGS = 32;
        private static final float SPHERE_OPACITY = 0.6f;
        private static final int SPHERE_COLOR = 0x3366FF;

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
            SenseData senseData = CLIENT_SENSE_DATA.get(playerId);
            if (senseData == null) {
                return;
            }

            long currentTime = System.currentTimeMillis();
            if (!senseData.isActive(currentTime)) {
                CLIENT_SENSE_DATA.remove(playerId);
                CLIENT_SENSE_INFO.remove(playerId);
                return;
            }

            Camera camera = mc.gameRenderer.getMainCamera();
            Vec3 cameraPos = camera.getPosition();

            float currentRadius = senseData.getCurrentRadius(currentTime);

            Vec3 playerPosition = mc.player.position();
            senseData.center = playerPosition;

            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.disableCull();

            RenderSystem.setShader(GameRenderer::getPositionColorShader);

            renderSphere(poseStack, buffers, senseData.center, currentRadius, cameraPos);

            buffers.endBatch();

            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
        }

        private static void renderSphere(PoseStack poseStack, MultiBufferSource.BufferSource buffers,
                                         Vec3 center, float radius, Vec3 cameraPos) {
            poseStack.pushPose();
            poseStack.translate(center.x - cameraPos.x, center.y - cameraPos.y, center.z - cameraPos.z);

            VertexConsumer consumer = buffers.getBuffer(RenderType.debugQuads());
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix = pose.pose();

            int color = SPHERE_COLOR;
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            int a = (int)(SPHERE_OPACITY * 255);

            drawWireframeSphere(consumer, matrix, radius, r, g, b, a);

            poseStack.popPose();
        }

        private static void drawWireframeSphere(VertexConsumer consumer, Matrix4f matrix, float radius,
                                                int r, int g, int b, int a) {
            for (int i = 0; i <= SPHERE_RINGS; i++) {
                float theta = (float) i / SPHERE_RINGS * (float) Math.PI;
                float y = radius * (float) Math.cos(theta);
                float ringRadius = radius * (float) Math.sin(theta);

                drawRing(consumer, matrix, ringRadius, y, r, g, b, a);
            }

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

                consumer.addVertex(matrix, prevX, y, prevZ).setColor(r, g, b, a);
                consumer.addVertex(matrix, x, y, z).setColor(r, g, b, a);

                prevX = x;
                prevZ = z;
            }
        }

        private static void drawMeridian(VertexConsumer consumer, Matrix4f matrix, float radius, float phi,
                                         int r, int g, int b, int a) {
            for (int i = 0; i < SPHERE_RINGS; i++) {
                float theta = (float) i / SPHERE_RINGS * (float) Math.PI;
                float y1 = radius * (float) Math.cos(theta);
                float y2 = radius * (float) Math.cos((float)(i + 1) / SPHERE_RINGS * Math.PI);

                float x1 = radius * (float) Math.sin(theta) * (float) Math.cos(phi);
                float z1 = radius * (float) Math.sin(theta) * (float) Math.sin(phi);

                float x2 = radius * (float) Math.sin((float)(i + 1) / SPHERE_RINGS * Math.PI) * (float) Math.cos(phi);
                float z2 = radius * (float) Math.sin((float)(i + 1) / SPHERE_RINGS * Math.PI) * (float) Math.sin(phi);

                consumer.addVertex(matrix, x1, y1, z1).setColor(r, g, b, a);
                consumer.addVertex(matrix, x2, y2, z2).setColor(r, g, b, a);
            }
        }
    }

    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, value = Dist.CLIENT)
    public static class SpiritualSenseOverlay {

        private static final long DISPLAY_DURATION = 7000;
        private static final int ENTITIES_PER_PAGE = 5;
        private static final long SCROLL_INTERVAL = 1500;

        private static final Map<UUID, ScrollState> playerScrollStates = new ConcurrentHashMap<>();

        private static class ScrollState {
            long lastScrollTime;
            int currentOffset;

            ScrollState() {
                this.lastScrollTime = System.currentTimeMillis();
                this.currentOffset = 0;
            }
        }

        @SubscribeEvent
        public static void onRenderGui(RenderGuiEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) {
                return;
            }

            if (!hasActiveSense(mc.player)) {
                playerScrollStates.remove(mc.player.getUUID());
                return;
            }

            PlayerSenseInfo senseInfo = CLIENT_SENSE_INFO.get(mc.player.getUUID());
            if (senseInfo == null || senseInfo.entities.isEmpty()) {
                return;
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - senseInfo.updateTime > DISPLAY_DURATION) {
                playerScrollStates.remove(mc.player.getUUID());
                return;
            }

            renderEntityInfoOverlay(event.getGuiGraphics(), senseInfo, mc);
        }

        private static void renderEntityInfoOverlay(GuiGraphics guiGraphics, PlayerSenseInfo senseInfo, Minecraft mc) {
            Font font = mc.font;
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            int rightMargin = 15;
            int topMargin = 40;
            int maxWidth = 220;

            int lineHeight = font.lineHeight + 1;
            int headerLines = 2;
            int totalHeight = (headerLines + ENTITIES_PER_PAGE) * lineHeight + 15;

            int startX = screenWidth - maxWidth - rightMargin;
            int startY = topMargin;

            int bgColor = 0x80000000;
            guiGraphics.fill(startX - 5, startY - 5,
                    screenWidth - rightMargin + 5, startY + totalHeight,
                    bgColor);

            int borderColor = 0x803366FF;
            guiGraphics.fill(startX - 5, startY - 5, startX - 4, startY + totalHeight, borderColor);
            guiGraphics.fill(startX - 5, startY - 5, screenWidth - rightMargin + 5, startY - 4, borderColor);
            guiGraphics.fill(screenWidth - rightMargin + 4, startY - 5,
                    screenWidth - rightMargin + 5, startY + totalHeight, borderColor);
            guiGraphics.fill(startX - 5, startY + totalHeight - 1,
                    screenWidth - rightMargin + 5, startY + totalHeight, borderColor);

            UUID playerId = mc.player.getUUID();
            ScrollState scrollState = playerScrollStates.computeIfAbsent(playerId, k -> new ScrollState());

            long currentTime = System.currentTimeMillis();
            if (currentTime - scrollState.lastScrollTime > SCROLL_INTERVAL) {
                scrollState.lastScrollTime = currentTime;
                scrollState.currentOffset += ENTITIES_PER_PAGE;

                if (scrollState.currentOffset >= senseInfo.entities.size()) {
                    scrollState.currentOffset = 0;
                }
            }

            int totalPages = (int) Math.ceil((double) senseInfo.entities.size() / ENTITIES_PER_PAGE);
            int currentPage = totalPages > 0 ? (scrollState.currentOffset / ENTITIES_PER_PAGE) + 1 : 1;

            MutableComponent title = Component.literal("§9Spiritual Sense §7(" + senseInfo.totalEntities + ")");
            if (totalPages > 1) {
                title.append(Component.literal(" §8[" + currentPage + "/" + totalPages + "]"));
            }

            guiGraphics.drawString(font, title, startX, startY, 0xFFFFFF, false);

            guiGraphics.fill(startX, startY + lineHeight, startX + maxWidth, startY + lineHeight + 1, 0x803366FF);

            int yOffset = startY + lineHeight * 2;
            int entitiesDrawn = 0;

            for (int i = scrollState.currentOffset;
                 i < senseInfo.entities.size() && entitiesDrawn < ENTITIES_PER_PAGE;
                 i++, entitiesDrawn++) {

                EntityInfo entity = senseInfo.entities.get(i);

                if (entity.isPlayer) {
                    renderPlayerInfo(guiGraphics, font, entity, startX, yOffset, maxWidth);
                } else {
                    renderEntityInfo(guiGraphics, font, entity, startX, yOffset, maxWidth);
                }

                yOffset += lineHeight;
            }

            if (entitiesDrawn < ENTITIES_PER_PAGE && scrollState.currentOffset == 0) {
                for (int i = entitiesDrawn; i < ENTITIES_PER_PAGE; i++) {
                    if (i == entitiesDrawn) {
                        Component scanningText = Component.literal("§8... scanning ...");
                        guiGraphics.drawString(font, scanningText, startX, yOffset, 0xAAAAAA, false);
                    }
                    yOffset += lineHeight;
                }
            } else if (entitiesDrawn < ENTITIES_PER_PAGE) {
                Component endText = Component.literal("§8... end ...");
                guiGraphics.drawString(font, endText, startX, yOffset, 0xAAAAAA, false);
                yOffset += lineHeight;

                long timeSinceLastScroll = currentTime - scrollState.lastScrollTime;
                long timeUntilNextCycle = SCROLL_INTERVAL - timeSinceLastScroll;
                int seconds = (int) Math.ceil(timeUntilNextCycle / 1000.0);

                if (seconds > 0) {
                    Component cycleText = Component.literal("§8Next: " + seconds + "s");
                    guiGraphics.drawString(font, cycleText, startX, yOffset, 0xAAAAAA, false);
                }
            }

            yOffset = startY + totalHeight - lineHeight + 2;
            Component scrollIndicator = Component.literal("§7↕ " + (scrollState.currentOffset + 1) + "-" +
                    Math.min(scrollState.currentOffset + ENTITIES_PER_PAGE, senseInfo.entities.size()));
            guiGraphics.drawString(font, scrollIndicator, startX, yOffset, 0xAAAAAA, false);
        }

        private static void renderPlayerInfo(GuiGraphics guiGraphics, Font font, EntityInfo player,
                                             int x, int y, int maxWidth) {
            String namePrefix = "§b☻ ";
            String nameText = namePrefix + player.name;

            float healthPercent = player.maxHealth > 0 ? player.health / player.maxHealth : 1.0f;
            int healthColor = getHealthColor(healthPercent);
            String healthText = String.format(" §c♥ %.1f/%.1f", player.health, player.maxHealth);

            String realmsText;
            if (player.canSeeDetails) {
                realmsText = String.format(" §7[§eE%s §6B%s §dI%s§7]",
                        player.essenceRealm, player.bodyRealm, player.intentRealm);
            } else {
                realmsText = " §7[§8???§7]";
            }

            String fullText = nameText + healthText + realmsText;

            int textWidth = font.width(fullText);
            if (textWidth > maxWidth) {
                int availableWidth = maxWidth - font.width(healthText + realmsText) - 10;
                String truncatedName = truncateText(font, player.name, availableWidth);
                fullText = namePrefix + truncatedName + healthText + realmsText;
            }

            guiGraphics.drawString(font, fullText, x, y, 0xFFFFFF, false);

            int barWidth = 60;
            int barHeight = 2;
            int barX = x;
            int barY = y + font.lineHeight - 2;

            guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0x80000000);

            int healthWidth = (int)(barWidth * healthPercent);
            guiGraphics.fill(barX, barY, barX + healthWidth, barY + barHeight, healthColor);
        }

        private static void renderEntityInfo(GuiGraphics guiGraphics, Font font, EntityInfo entity,
                                             int x, int y, int maxWidth) {
            String icon = "§8⚲ ";
            String entityName = entity.getDisplayName();

            String lowerName = entityName.toLowerCase();
            if (lowerName.contains("zombie")) icon = "§2☠ ";
            else if (lowerName.contains("skeleton")) icon = "§7☠ ";
            else if (lowerName.contains("spider")) icon = "§8🕷 ";
            else if (lowerName.contains("creeper")) icon = "§a💣 ";
            else if (lowerName.contains("enderman")) icon = "§5👁 ";

            String nameText = icon + entityName;

            String healthText = "";
            if (entity.maxHealth > 0) {
                float healthPercent = entity.health / entity.maxHealth;
                healthText = String.format(" §c♥ %.1f/%.1f", entity.health, entity.maxHealth);
            }

            String fullText = nameText + healthText;

            guiGraphics.drawString(font, fullText, x, y, 0xFFFFFF, false);

            if (entity.maxHealth > 0) {
                float healthPercent = entity.health / entity.maxHealth;
                int healthColor = getHealthColor(healthPercent);

                int barWidth = 45;
                int barHeight = 2;
                int barX = x;
                int barY = y + font.lineHeight - 2;

                guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0x80000000);

                int healthWidth = (int)(barWidth * healthPercent);
                guiGraphics.fill(barX, barY, barX + healthWidth, barY + barHeight, healthColor);
            }
        }

        private static int getHealthColor(float healthPercent) {
            if (healthPercent > 0.7f) return 0x8000FF00;
            if (healthPercent > 0.3f) return 0x80FFFF00;
            return 0x80FF0000;
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