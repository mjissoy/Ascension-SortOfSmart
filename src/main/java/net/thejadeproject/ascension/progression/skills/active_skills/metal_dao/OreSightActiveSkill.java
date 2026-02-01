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
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.thejadeproject.ascension.progression.skills.active_skills.physique_skills.kitsune_skills.FoxfireManipulationActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OreSightActiveSkill extends AbstractActiveSkill {

    private static final String SKILL_ID = "ascension:ore_sight";
    private static final int MIN_REALM_FOR_NAME_DISPLAY = 5;

    // Client-side rendering cache (not persisted)
    private static final Map<UUID, Map<BlockPos, OreGlowData>> clientGlowingBlocks = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> clientExpirationTimes = new ConcurrentHashMap<>();

    public static class OreGlowData {
        public final int color;
        public final long expirationTime;
        public final Block block;

        public OreGlowData(int color, long expirationTime, Block block) {
            this.color = color;
            this.expirationTime = expirationTime;
            this.block = block;
        }
    }

    private static final Map<Block, Integer> DEFAULT_ORE_COLORS;
    static {
        Map<Block, Integer> tempMap = new HashMap<>();
        tempMap.put(Blocks.COAL_ORE, 0x2F2F2F);
        tempMap.put(Blocks.DEEPSLATE_COAL_ORE, 0x2F2F2F);
        tempMap.put(Blocks.IRON_ORE, 0xD8D8D8);
        tempMap.put(Blocks.DEEPSLATE_IRON_ORE, 0xD8D8D8);
        tempMap.put(Blocks.COPPER_ORE, 0xB87333);
        tempMap.put(Blocks.DEEPSLATE_COPPER_ORE, 0xB87333);
        tempMap.put(Blocks.GOLD_ORE, 0xFFD700);
        tempMap.put(Blocks.DEEPSLATE_GOLD_ORE, 0xFFD700);
        tempMap.put(Blocks.NETHER_GOLD_ORE, 0xFFD700);
        tempMap.put(Blocks.DIAMOND_ORE, 0x00FFFF);
        tempMap.put(Blocks.DEEPSLATE_DIAMOND_ORE, 0x00FFFF);
        tempMap.put(Blocks.EMERALD_ORE, 0x00FF00);
        tempMap.put(Blocks.DEEPSLATE_EMERALD_ORE, 0x00FF00);
        tempMap.put(Blocks.LAPIS_ORE, 0x0000FF);
        tempMap.put(Blocks.DEEPSLATE_LAPIS_ORE, 0x0000FF);
        tempMap.put(Blocks.REDSTONE_ORE, 0xFF0000);
        tempMap.put(Blocks.DEEPSLATE_REDSTONE_ORE, 0xFF0000);
        tempMap.put(Blocks.NETHER_QUARTZ_ORE, 0xF0F0F0);
        tempMap.put(Blocks.ANCIENT_DEBRIS, 0x8B4513);
        DEFAULT_ORE_COLORS = Collections.unmodifiableMap(tempMap);
    }

    public static class OreSightData implements IPersistentSkillData {
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

    public OreSightActiveSkill() {
        super(Component.translatable("ascension.skill.active.ore_sight"));
        this.path = "ascension:essence";
        this.qiCost = 15.0;
    }

    private OreSightData getData(Player player) {
        var skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        var metaData = skillData.getActiveSkill(SKILL_ID);
        if (metaData != null && metaData.data instanceof OreSightData data) {
            return data;
        }
        return null;
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

        OreSightData data = getData(player);
        if (data == null) return;

        if (data.getCooldown() > 0) {
            int seconds = (data.getCooldown() + 19) / 20;
            player.displayClientMessage(Component.literal("Ore Sight on cooldown: " + seconds + "s"), true);
            return;
        }

        int majorRealm = player.getData(ModAttachments.PLAYER_DATA)
                .getCultivationData().getPathData(this.path).majorRealm;
        int durationSeconds = 10 + (majorRealm * 5);
        long expirationTime = System.currentTimeMillis() + (durationSeconds * 1000L);
        data.setCooldown(getCooldown());

        int detectionRadius = 10 + majorRealm;
        BlockPos playerPos = player.blockPosition();
        Map<BlockPos, SyncOreSightPacket.BlockData> packetData = new HashMap<>();
        int oresFound = 0;

        for (BlockPos checkPos : BlockPos.betweenClosed(
                playerPos.offset(-detectionRadius, -detectionRadius, -detectionRadius),
                playerPos.offset(detectionRadius, detectionRadius, detectionRadius))) {

            BlockState blockState = level.getBlockState(checkPos);
            Block block = blockState.getBlock();

            if (isOreBlock(block)) {
                Integer color = getColorForOre(block);
                if (color != null) {
                    packetData.put(checkPos.immutable(),
                            new SyncOreSightPacket.BlockData(color, expirationTime, BuiltInRegistries.BLOCK.getKey(block)));
                    oresFound++;

                    if (level instanceof ServerLevel serverLevel) {
                        spawnOreParticle(serverLevel, checkPos, color);
                    }
                }
            }
        }

        player.syncData(ModAttachments.PLAYER_SKILL_DATA);

        if (player instanceof ServerPlayer serverPlayer) {
            SyncOreSightPacket packet = new SyncOreSightPacket(player.getUUID(), packetData, durationSeconds);
            serverPlayer.connection.send(packet);
        }

        if (oresFound > 0) {
            level.playSound(null, playerPos, SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.PLAYERS, 0.8F, 1.2F);
        } else {
            level.playSound(null, playerPos, SoundEvents.NOTE_BLOCK_BASS.value(),
                    SoundSource.PLAYERS, 0.5F, 0.8F);
        }
    }

    @Override
    public void onPreCast() {}

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClearOreSightPacket(player.getUUID()));
        }
    }

    public void onPlayerTick(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) return;

        Player player = event.getEntity();
        OreSightData data = getData(player);
        if (data == null) return;

        if (data.getCooldown() > 0) {
            data.decrementCooldown();
            if (data.getCooldown() == 0 || data.getCooldown() % 20 == 0) {
                player.syncData(ModAttachments.PLAYER_SKILL_DATA);
            }
        }
    }

    private boolean isOreBlock(Block block) {
        if (DEFAULT_ORE_COLORS.containsKey(block)) return true;

        ResourceLocation blockName = BuiltInRegistries.BLOCK.getKey(block);
        if (blockName == null) return false;

        String name = blockName.getPath().toLowerCase();
        return name.contains("ore") &&
                !name.contains("shore") && !name.contains("more") && !name.contains("core") &&
                !name.contains("store") && !name.contains("before") && !name.contains("explore") &&
                !name.contains("ignore") && !name.contains("restore") && !name.contains("fore");
    }

    private static Integer getColorForOre(Block block) {
        return DEFAULT_ORE_COLORS.get(block);
    }

    private void spawnOreParticle(ServerLevel level, BlockPos pos, int color) {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;

        DustParticleOptions particle = new DustParticleOptions(new Vector3f(r, g, b), 1.0f);

        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 0.3;
            double y = pos.getY() + 1.0 + level.random.nextDouble() * 0.5;
            double z = pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 0.3;

            level.sendParticles(particle, x, y, z, 1, 0, 0, 0, 0);
        }
    }

    // Static methods for client-side packet handling
    public static void updateClientGlowingBlocks(UUID playerId, Map<BlockPos, OreGlowData> blocks, long durationSeconds) {
        clientGlowingBlocks.put(playerId, blocks);
        clientExpirationTimes.put(playerId, System.currentTimeMillis() + (durationSeconds * 1000L));
    }

    public static void clearClientGlowingBlocks(UUID playerId) {
        clientGlowingBlocks.remove(playerId);
        clientExpirationTimes.remove(playerId);
    }

    public static Map<BlockPos, OreGlowData> getClientGlowingBlocksForPlayer(UUID playerId) {
        Long expiration = clientExpirationTimes.get(playerId);
        if (expiration != null && System.currentTimeMillis() > expiration) {
            clientGlowingBlocks.remove(playerId);
            clientExpirationTimes.remove(playerId);
            return Collections.emptyMap();
        }
        return clientGlowingBlocks.getOrDefault(playerId, Collections.emptyMap());
    }

    public static boolean hasActiveOreSight(Player player) {
        if (player.level().isClientSide()) {
            Map<BlockPos, OreGlowData> blocks = getClientGlowingBlocksForPlayer(player.getUUID());
            return blocks != null && !blocks.isEmpty();
        }
        return false;
    }

    public static int getPlayerRealm(Player player) {
        return player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData("ascension:essence").majorRealm;
    }

    public static Block getLookedAtOreFromGlow(Player player) {
        Minecraft mc = Minecraft.getInstance();
        Map<BlockPos, OreGlowData> glowingBlocks = getClientGlowingBlocksForPlayer(player.getUUID());
        if (glowingBlocks.isEmpty()) return null;

        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getLookAngle();
        double maxDistance = 64.0;

        BlockPos closestBlock = null;
        double closestDistance = Double.MAX_VALUE;

        for (BlockPos pos : glowingBlocks.keySet()) {
            AABB blockAABB = new AABB(
                    pos.getX() - 0.1, pos.getY() - 0.1, pos.getZ() - 0.1,
                    pos.getX() + 1.1, pos.getY() + 1.1, pos.getZ() + 1.1
            );

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
            if (data != null) return data.block;
        }
        return null;
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance() {
        return new OreSightData();
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(CompoundTag tag) {
        OreSightData data = new OreSightData();
        data.readData(tag);
        return data;
    }
    @Override
    public IPersistentSkillData getPersistentDataInstance(RegistryFriendlyByteBuf buf) {
        OreSightData data = new OreSightData();
        data.decode(buf);
        return data;
    }

    // Client-side rendering
    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, value = Dist.CLIENT)
    public static class OreGlowRenderer {

        private static final int MAX_ALPHA = 180;
        private static final int MIN_ALPHA = 30;
        private static final float FADE_START_DISTANCE = 3.0f;
        private static final float FADE_END_DISTANCE = 10.0f;

        @SubscribeEvent
        public static void onRenderLevel(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
                return;
            }

            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null || mc.player == null) return;

            Map<BlockPos, OreGlowData> glowing = getClientGlowingBlocksForPlayer(mc.player.getUUID());
            if (glowing.isEmpty()) return;

            Camera camera = mc.gameRenderer.getMainCamera();
            double camX = camera.getPosition().x;
            double camY = camera.getPosition().y;
            double camZ = camera.getPosition().z;

            BlockPos playerPos = mc.player.blockPosition();
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();

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
                if (state.isAir()) continue;

                double distance = Math.sqrt(
                        (pos.getX() - playerPos.getX()) * (pos.getX() - playerPos.getX()) +
                                (pos.getY() - playerPos.getY()) * (pos.getY() - playerPos.getY()) +
                                (pos.getZ() - playerPos.getZ()) * (pos.getZ() - playerPos.getZ())
                );

                int alpha = calculateAlphaBasedOnDistance((float)distance);
                renderGlowBlock(poseStack, buffers, pos, data.color, alpha, camX, camY, camZ);
            }

            buffers.endBatch();

            GL11.glDepthMask(true);
            if (depthTestEnabled) GL11.glEnable(GL11.GL_DEPTH_TEST);
            if (!blendEnabled) GL11.glDisable(GL11.GL_BLEND);
        }

        private static int calculateAlphaBasedOnDistance(float distance) {
            if (distance <= FADE_START_DISTANCE) return MIN_ALPHA;
            if (distance >= FADE_END_DISTANCE) return MAX_ALPHA;
            float t = (distance - FADE_START_DISTANCE) / (FADE_END_DISTANCE - FADE_START_DISTANCE);
            return (int)(MIN_ALPHA + (MAX_ALPHA - MIN_ALPHA) * t);
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

            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            float minX = 0.001F;
            float minY = 0.001F;
            float minZ = 0.001F;
            float maxX = 0.999F;
            float maxY = 0.999F;
            float maxZ = 0.999F;

            addQuad(consumer, matrix, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, alpha);
            addQuad(consumer, matrix, minX, maxY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, minX, maxY, minZ, r, g, b, alpha);
            addQuad(consumer, matrix, maxX, minY, minZ, minX, minY, minZ, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, alpha);
            addQuad(consumer, matrix, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, alpha);
            addQuad(consumer, matrix, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, alpha);
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
    @EventBusSubscriber(modid = AscensionCraft.MOD_ID, value = Dist.CLIENT)
    public static class OreNameOverlay {

        private static Block lastLookedAtOre = null;
        private static long lastDisplayTime = 0;
        private static final long DISPLAY_DURATION = 1000;

        @SubscribeEvent
        public static void onRenderGui(RenderGuiEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.level == null) return;

            if (!hasActiveOreSight(mc.player)) {
                lastLookedAtOre = null;
                return;
            }

            int playerRealm = getPlayerRealm(mc.player);
            if (playerRealm < MIN_REALM_FOR_NAME_DISPLAY) return;

            Block lookedAtOre = getLookedAtOreFromGlow(mc.player);

            if (lookedAtOre != null) {
                lastLookedAtOre = lookedAtOre;
                lastDisplayTime = System.currentTimeMillis();
            } else {
                if (System.currentTimeMillis() - lastDisplayTime > DISPLAY_DURATION) {
                    lastLookedAtOre = null;
                }
            }

            if (lastLookedAtOre != null) {
                renderOreTooltip(event.getGuiGraphics(), lastLookedAtOre, mc, playerRealm);
            }
        }

        private static void renderOreTooltip(GuiGraphics guiGraphics, Block oreBlock, Minecraft mc, int playerRealm) {
            Component oreName = oreBlock.getName();
            MutableComponent displayText = Component.empty();

            if (playerRealm >= 10) {
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(oreBlock);
                if (blockId != null) {
                    displayText = Component.literal("§6[Master Miner]§r ")
                            .append(oreName.copy().withStyle(style -> style.withColor(0xFFFF00)));
                }
            } else if (playerRealm >= 7) {
                displayText = oreName.copy().withStyle(style -> style.withColor(0x55FF55));
            } else {
                displayText = oreName.copy();
            }

            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            int centerX = screenWidth / 2;
            int centerY = screenHeight / 2;

            Font font = mc.font;
            int textWidth = font.width(displayText);
            int textX = centerX - (textWidth / 2);
            int textY = centerY + 12;

            int bgPaddingH = 6;
            int bgPaddingV = 2;
            int bgColor = 0x80000000;

            guiGraphics.fill(
                    textX - bgPaddingH, textY - bgPaddingV,
                    textX + textWidth + bgPaddingH, textY + font.lineHeight + bgPaddingV,
                    bgColor
            );

            int borderColor = getBorderColorForOre(oreBlock, playerRealm);
            int borderThickness = 1;

            guiGraphics.fill(textX - bgPaddingH, textY - bgPaddingV, textX + textWidth + bgPaddingH, textY - bgPaddingV + borderThickness, borderColor);
            guiGraphics.fill(textX - bgPaddingH, textY + font.lineHeight + bgPaddingV - borderThickness, textX + textWidth + bgPaddingH, textY + font.lineHeight + bgPaddingV, borderColor);
            guiGraphics.fill(textX - bgPaddingH, textY - bgPaddingV, textX - bgPaddingH + borderThickness, textY + font.lineHeight + bgPaddingV, borderColor);
            guiGraphics.fill(textX + textWidth + bgPaddingH - borderThickness, textY - bgPaddingV, textX + textWidth + bgPaddingH, textY + font.lineHeight + bgPaddingV, borderColor);

            guiGraphics.drawString(font, displayText, textX, textY, 0xFFFFFF, true);

            if (playerRealm >= 12) {
                Component rarityText = getRarityText(oreBlock);
                if (rarityText != null) {
                    int rarityY = textY + font.lineHeight + bgPaddingV + 2;
                    int rarityWidth = font.width(rarityText);
                    int rarityX = centerX - (rarityWidth / 2);

                    int rarityBgColor = 0x60000000;
                    int rarityBgPaddingH = 5;
                    int rarityBgPaddingV = 1;

                    guiGraphics.fill(
                            rarityX - rarityBgPaddingH, rarityY - rarityBgPaddingV,
                            rarityX + rarityWidth + rarityBgPaddingH, rarityY + font.lineHeight + rarityBgPaddingV,
                            rarityBgColor
                    );

                    guiGraphics.drawString(font, rarityText, rarityX, rarityY, 0xFFAA00, true);
                }
            }

            if (playerRealm >= 8) {
                int iconSize = 6;
                int iconX = textX - bgPaddingH - iconSize - 1;
                int iconY = textY + (font.lineHeight - iconSize) / 2;

                int oreColor = getColorForOre(oreBlock);
                int r = (oreColor >> 16) & 0xFF;
                int g = (oreColor >> 8) & 0xFF;
                int b = oreColor & 0xFF;

                guiGraphics.fill(iconX - 1, iconY - 1, iconX + iconSize + 1, iconY + iconSize + 1, 0x80000000);
                guiGraphics.fill(iconX, iconY, iconX + iconSize, iconY + iconSize, (200 << 24) | (r << 16) | (g << 8) | b);
            }
        }

        private static int getBorderColorForOre(Block oreBlock, int playerRealm) {
            if (playerRealm >= 10) {
                if (oreBlock == Blocks.DIAMOND_ORE || oreBlock == Blocks.DEEPSLATE_DIAMOND_ORE) return 0x8000FFFF;
                else if (oreBlock == Blocks.EMERALD_ORE || oreBlock == Blocks.DEEPSLATE_EMERALD_ORE) return 0x8000FF00;
                else if (oreBlock == Blocks.ANCIENT_DEBRIS) return 0x80FF6600;
                else if (oreBlock == Blocks.GOLD_ORE || oreBlock == Blocks.DEEPSLATE_GOLD_ORE) return 0x80FFFF00;
            }
            return 0x80AA00;
        }

        private static Component getRarityText(Block oreBlock) {
            if (oreBlock == Blocks.DIAMOND_ORE || oreBlock == Blocks.DEEPSLATE_DIAMOND_ORE)
                return Component.literal("§b✨ Rare");
            else if (oreBlock == Blocks.EMERALD_ORE || oreBlock == Blocks.DEEPSLATE_EMERALD_ORE)
                return Component.literal("§a★ Uncommon");
            else if (oreBlock == Blocks.ANCIENT_DEBRIS)
                return Component.literal("§6🔥 Legendary");
            else if (oreBlock == Blocks.GOLD_ORE || oreBlock == Blocks.DEEPSLATE_GOLD_ORE)
                return Component.literal("§e⚜ Valuable");
            else if (oreBlock == Blocks.IRON_ORE || oreBlock == Blocks.DEEPSLATE_IRON_ORE)
                return Component.literal("§7⚒ Common");
            return Component.literal("§7Common");
        }
    }
}