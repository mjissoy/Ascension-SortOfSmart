package net.thejadeproject.ascension.progression.skills.active_skills.space_dao;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.thejadeproject.ascension.items.ModItems;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SpaceInfusionActiveSkill extends AbstractActiveSkill {

    private static final DustParticleOptions PURPLE_DUST =
            new DustParticleOptions(new Vector3f(0.5f, 0.0f, 0.8f), 1.0f);
    private static final DustParticleOptions DARK_PURPLE_DUST =
            new DustParticleOptions(new Vector3f(0.3f, 0.0f, 0.5f), 1.0f);

    private static final String SKILL_ID = "ascension:space_infusion";
    private static final int COOLDOWN_TICKS = 15 * 20;

    // Transient sessions - not persisted
    private static final Map<UUID, ActiveSession> ACTIVE_SESSIONS = new HashMap<>();

    private static class ActiveSession {
        ItemEntity floatingItem;
        Vec3 fixedPosition;
        int ticksActive = 0;
        float rotation = 0.0f;
        float rotationSpeed = 1.0f;
    }

    public static class SpaceInfusionData implements IPersistentSkillData {
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

    public SpaceInfusionActiveSkill() {
        super(Component.translatable("ascension.skill.active.space_infusion"));
        this.path = "ascension:essence";
        this.qiCost = 25.0;

        NeoForge.EVENT_BUS.addListener(this::onPlayerTick);
    }

    private SpaceInfusionData getData(Player player) {
        var skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        var metaData = skillData.getActiveSkill(SKILL_ID);
        if (metaData != null && metaData.data instanceof SpaceInfusionData data) {
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
        return COOLDOWN_TICKS;
    }

    @Override
    public int maxCastingTicks() {
        return 0;
    }

    @Override
    public void cast(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        if (level.isClientSide()) return;

        SpaceInfusionData data = getData(player);
        if (data == null) return;

        if (data.getCooldown() > 0) {
            int seconds = (data.getCooldown() + 19) / 20;
            player.displayClientMessage(
                    Component.literal("Space Infusion is on cooldown! (" + seconds + "s)"), true);
            return;
        }

        if (!isInEndDimension(level)) {
            player.displayClientMessage(
                    Component.literal("§cThis skill can only be used in The End!§r"), true);
            return;
        }

        ItemStack spiritualStone = findSpiritualStone(player);
        if (spiritualStone.isEmpty()) {
            player.displayClientMessage(
                    Component.literal("§cYou need a Spiritual Stone to use this skill!§r"), true);
            return;
        }

        spiritualStone.shrink(1);
        data.setCooldown(COOLDOWN_TICKS);
        player.syncData(ModAttachments.PLAYER_SKILL_DATA);

        Vec3 lookVec = player.getLookAngle().normalize();
        Vec3 playerPos = player.position();
        Vec3 fixedPosition = playerPos.add(lookVec.x, player.getEyeHeight() - 0.3, lookVec.z);

        ItemStack floatingStone = new ItemStack(ModItems.SPIRITUAL_STONE.get(), 1);
        ItemEntity itemEntity = new ItemEntity(level,
                fixedPosition.x, fixedPosition.y, fixedPosition.z,
                floatingStone);

        itemEntity.setNoGravity(true);
        itemEntity.setInvulnerable(true);
        itemEntity.setPickUpDelay(Integer.MAX_VALUE);
        itemEntity.setDeltaMovement(0, 0, 0);
        level.addFreshEntity(itemEntity);

        ActiveSession session = new ActiveSession();
        session.floatingItem = itemEntity;
        session.fixedPosition = fixedPosition;
        ACTIVE_SESSIONS.put(player.getUUID(), session);

        spawnInitialParticles(level, fixedPosition);

        player.displayClientMessage(Component.literal("§dSpace Infusion begins..."), true);
    }

    @Override
    public void onPreCast() {}

    private boolean isInEndDimension(Level level) {
        return level.dimension().location().getPath().equals("the_end");
    }

    private ItemStack findSpiritualStone(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() == ModItems.SPIRITUAL_STONE.get()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        SpaceInfusionData data = getData(player);
        if (data != null && data.getCooldown() > 0) {
            data.decrementCooldown();
            if (data.getCooldown() == 0) {
                player.syncData(ModAttachments.PLAYER_SKILL_DATA);
            }
        }

        UUID playerId = player.getUUID();
        ActiveSession session = ACTIVE_SESSIONS.get(playerId);
        if (session == null) return;

        Level level = player.level();

        if (!isInEndDimension(level)) {
            cleanupSession(session);
            ACTIVE_SESSIONS.remove(playerId);
            player.displayClientMessage(
                    Component.literal("§cSpace Infusion fades as you leave The End§r"), true);
            return;
        }

        session.ticksActive++;
        session.rotation += session.rotationSpeed;
        if (session.rotation > 360.0f) session.rotation -= 360.0f;

        if (session.ticksActive < 100) {
            session.rotationSpeed = 1.0f + (9.0f * (session.ticksActive / 100.0f));
        }

        if (session.floatingItem != null && session.floatingItem.isAlive()) {
            session.floatingItem.setPos(session.fixedPosition.x, session.fixedPosition.y, session.fixedPosition.z);
            session.floatingItem.setDeltaMovement(0, 0, 0);

            if (level instanceof ServerLevel serverLevel) {
                if (session.ticksActive % 3 == 0) {
                    spawnParticlePullEffects(serverLevel, session);
                }
                if (session.ticksActive % 5 == 0) {
                    spawnGlowParticles(serverLevel, session);
                }
            }
        } else {
            cleanupSession(session);
            ACTIVE_SESSIONS.remove(playerId);
            player.displayClientMessage(
                    Component.literal("§cThe Spiritual Stone was destroyed!§r"), true);
            return;
        }

        if (session.ticksActive >= 100) {
            completeSkill(player, session);
            cleanupSession(session);
            ACTIVE_SESSIONS.remove(playerId);
        }
    }

    private void spawnInitialParticles(Level level, Vec3 position) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        for (int i = 0; i < 10; i++) {
            double angle = level.random.nextDouble() * Math.PI * 2;
            double radius = 0.2 + level.random.nextDouble() * 0.2;
            double x = position.x + radius * Math.cos(angle);
            double z = position.z + radius * Math.sin(angle);
            double y = position.y + (level.random.nextDouble() - 0.5) * 0.2;

            serverLevel.sendParticles(PURPLE_DUST, x, y, z, 1, 0, 0.02, 0, 0.01);
        }
    }

    private void spawnParticlePullEffects(ServerLevel serverLevel, ActiveSession session) {
        Vec3 center = session.fixedPosition;
        int particleCount = serverLevel.random.nextInt(2) + 1;

        for (int i = 0; i < particleCount; i++) {
            double distance = 2.0 + serverLevel.random.nextDouble() * 2.0;
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double verticalAngle = serverLevel.random.nextDouble() * Math.PI;

            double x = center.x + distance * Math.cos(angle) * Math.sin(verticalAngle);
            double y = center.y + distance * Math.cos(verticalAngle);
            double z = center.z + distance * Math.sin(angle) * Math.sin(verticalAngle);

            double dx = (center.x - x) * 0.05;
            double dy = (center.y - y) * 0.05;
            double dz = (center.z - z) * 0.05;

            DustParticleOptions color = serverLevel.random.nextBoolean() ? PURPLE_DUST : DARK_PURPLE_DUST;

            serverLevel.sendParticles(color, x, y, z, 1, dx, dy, dz, 0.03);
        }
    }

    private void spawnGlowParticles(ServerLevel serverLevel, ActiveSession session) {
        Vec3 center = session.fixedPosition;

        for (int i = 0; i < 2; i++) {
            double angle = Math.toRadians(session.rotation + (i * 180.0));
            double radius = 0.25;
            double x = center.x + radius * Math.cos(angle);
            double z = center.z + radius * Math.sin(angle);
            double y = center.y + 0.1 * Math.sin(angle * 2);

            serverLevel.sendParticles(ParticleTypes.GLOW, x, y, z, 1, 0, 0, 0, 0.01);
        }
    }

    private void completeSkill(Player player, ActiveSession session) {
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        Vec3 center = session.fixedPosition;

        serverLevel.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.PLAYERS, 1.0f, 0.5f);

        for (int i = 0; i < 20; i++) {
            double angle = serverLevel.random.nextDouble() * Math.PI * 2;
            double distance = serverLevel.random.nextDouble() * 0.8;
            double x = center.x + distance * Math.cos(angle);
            double z = center.z + distance * Math.sin(angle);
            double y = center.y + (serverLevel.random.nextDouble() - 0.5) * 0.8;

            serverLevel.sendParticles(ParticleTypes.GLOW, x, y, z, 1, 0, 0.05, 0, 0.05);
        }

        ItemStack spatialStone = new ItemStack(ModItems.SPATIAL_STONE_TIER_1.get(), 1);
        ItemEntity droppedItem = new ItemEntity(level, center.x, center.y, center.z, spatialStone);
        droppedItem.setDefaultPickUpDelay();
        level.addFreshEntity(droppedItem);

        player.displayClientMessage(
                Component.literal("§aSpace Infusion complete! A Spatial Stone has formed.§r"), true);
    }

    private void cleanupSession(ActiveSession session) {
        if (session.floatingItem != null && session.floatingItem.isAlive()) {
            session.floatingItem.discard();
            session.floatingItem = null;
        }
    }

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        ActiveSession session = ACTIVE_SESSIONS.remove(player.getUUID());
        if (session != null) {
            cleanupSession(session);
        }
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance() {
        return new SpaceInfusionData();
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(CompoundTag tag) {
        SpaceInfusionData data = new SpaceInfusionData();
        data.readData(tag);
        return data;
    }
}