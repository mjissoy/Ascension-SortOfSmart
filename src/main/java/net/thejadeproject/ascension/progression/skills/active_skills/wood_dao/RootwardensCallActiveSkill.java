package net.thejadeproject.ascension.progression.skills.active_skills.wood_dao;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
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
import net.neoforged.neoforge.common.NeoForge;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.progression.skills.AbstractActiveSkill;
import net.thejadeproject.ascension.progression.skills.data.IPersistentSkillData;
import net.thejadeproject.ascension.progression.skills.data.casting.CastType;
import net.thejadeproject.ascension.progression.skills.data.casting.ICastData;
import net.thejadeproject.ascension.data_attachments.ModAttachments;
import org.joml.Vector3f;

import java.util.*;

public class RootwardensCallActiveSkill extends AbstractActiveSkill {

    // Client-only rendering data (not persisted)
    private static final Map<UUID, ClientWoodData> CLIENT_RENDER_DATA = new HashMap<>();

    private static final String SKILL_ID = "ascension:rootwardens_call";
    private static final int BASE_RADIUS = 6;
    private static final int BASE_COOLDOWN_TICKS = 20 * 30; // 30 seconds
    private static final int RADIUS_INCREASE_PER_REALM = 2;
    private static final int COOLDOWN_REDUCTION_PER_REALM_TICKS = 40;
    private static final int MIN_COOLDOWN_TICKS = 20 * 5;
    private static final Vector3f GREEN_PARTICLE_COLOR = new Vector3f(0.0f, 1.0f, 0.0f);

    private static class ClientWoodData {
        final Set<BlockPos> allWoodBlocks = new HashSet<>();
        final List<Set<BlockPos>> connectedComponents = new ArrayList<>();
        final long expirationTime;

        ClientWoodData(long durationMs) {
            this.expirationTime = System.currentTimeMillis() + durationMs;
        }
    }

    public static class RootwardenData implements IPersistentSkillData {
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

    public RootwardensCallActiveSkill() {
        super(Component.translatable("ascension.skill.active.rootwardens_call"));
        this.path = "ascension:essence";
        this.qiCost = 25.0;
    }

    private RootwardenData getData(Player player) {
        var skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        var metaData = skillData.getActiveSkill(SKILL_ID);
        if (metaData != null && metaData.data instanceof RootwardenData data) {
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
        return CastType.LONG;
    }

    @Override
    public int getCooldown() {
        return BASE_COOLDOWN_TICKS;
    }

    public int getAdjustedCooldown(Player player) {
        int majorRealm = getPlayerMajorRealm(player);
        int adjustedTicks = BASE_COOLDOWN_TICKS - (majorRealm * COOLDOWN_REDUCTION_PER_REALM_TICKS);
        return Math.max(adjustedTicks, MIN_COOLDOWN_TICKS);
    }

    public int getAdjustedRadius(Player player) {
        int majorRealm = getPlayerMajorRealm(player);
        return BASE_RADIUS + (majorRealm * RADIUS_INCREASE_PER_REALM);
    }

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
        return 100; // 5 seconds
    }

    @Override
    public void cast(int castingTicksElapsed, Level level, Player player, ICastData castData) {
        if (level.isClientSide()) {
            handleClientCast(castingTicksElapsed, level, player);
        } else {
            handleServerCast(castingTicksElapsed, level, player);
        }
    }

    private void handleClientCast(int castingTicksElapsed, Level level, Player player) {
        if (castingTicksElapsed == 0) {
            findAndDisplayWoodBlocks(level, player);
            level.playSound(null, player.blockPosition(), SoundEvents.GROWING_PLANT_CROP,
                    SoundSource.PLAYERS, 0.8F, 0.9F);
        } else if (castingTicksElapsed >= maxCastingTicks() - 1) {
            CLIENT_RENDER_DATA.remove(player.getUUID());
            level.playSound(null, player.blockPosition(), SoundEvents.WOOD_BREAK,
                    SoundSource.PLAYERS, 1.0F, 0.8F);
        }
    }

    private void handleServerCast(int castingTicksElapsed, Level level, Player player) {
        RootwardenData data = getData(player);
        if (data == null) return;

        if (castingTicksElapsed >= maxCastingTicks() - 1) {
            if (data.getCooldown() <= 0) {
                harvestWoodBlocks(level, player);
                data.setCooldown(getAdjustedCooldown(player));
                player.syncData(ModAttachments.PLAYER_SKILL_DATA);
            } else {
                int seconds = (data.getCooldown() + 19) / 20;
                player.displayClientMessage(Component.literal("Rootwarden's Call on cooldown: " + seconds + "s"), true);
            }
        }
    }

    public void onServerTick(Player player) {
        RootwardenData data = getData(player);
        if (data != null && data.getCooldown() > 0) {
            data.decrementCooldown();
            if (data.getCooldown() == 0) {
                player.syncData(ModAttachments.PLAYER_SKILL_DATA);
            }
        }
    }

    @Override
    public void onPreCast() {}

    @Override
    public void onSkillRemoved(Player player) {
        super.onSkillRemoved(player);
        CLIENT_RENDER_DATA.remove(player.getUUID());
    }

    private void findAndDisplayWoodBlocks(Level level, Player player) {
        UUID playerId = player.getUUID();
        BlockPos playerPos = player.blockPosition();
        int radius = getAdjustedRadius(player);

        ClientWoodData data = new ClientWoodData(30000);
        Set<BlockPos> allWoodPositions = new HashSet<>();

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

        data.allWoodBlocks.addAll(allWoodPositions);
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

        CLIENT_RENDER_DATA.put(playerId, data);
    }

    private void floodFillWood(BlockPos start, Set<BlockPos> component,
                               Set<BlockPos> visited, Set<BlockPos> allWoodPositions) {
        Stack<BlockPos> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            BlockPos current = stack.pop();
            if (visited.contains(current)) continue;
            visited.add(current);
            component.add(current);

            for (Direction direction : Direction.values()) {
                BlockPos neighbor = current.relative(direction);
                if (allWoodPositions.contains(neighbor) && !visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
    }

    private boolean isWoodBlock(BlockState state) {
        if (state == null || state.isAir()) return false;

        try {
            if (state.is(BlockTags.LOGS)) return true;
        } catch (Exception e) {}

        Block block = state.getBlock();
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
        if (blockId != null) {
            String path = blockId.getPath().toLowerCase();
            boolean isWoodLike = path.contains("log") || path.contains("wood") ||
                    path.contains("plank") || path.contains("stem") || path.contains("hyphae");
            boolean isExcluded = path.contains("leaf") || path.contains("sapling") ||
                    path.contains("fungus") || path.contains("nylium") || path.contains("wart") ||
                    path.contains("shroomlight") || path.contains("mushroom") || path.contains("vine");

            return isWoodLike && !isExcluded;
        }
        return false;
    }

    private void harvestWoodBlocks(Level level, Player player) {
        BlockPos playerPos = player.blockPosition();
        int radius = getAdjustedRadius(player);
        Set<BlockPos> allWoodPositions = new HashSet<>();

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
        ParticleOptions greenParticle = new DustParticleOptions(GREEN_PARTICLE_COLOR, 1.5f);

        for (BlockPos woodPos : allWoodPositions) {
            if (!level.isLoaded(woodPos)) continue;
            BlockState state = level.getBlockState(woodPos);
            if (!isWoodBlock(state)) continue;

            LootParams.Builder lootParamsBuilder = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(woodPos))
                    .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, player)
                    .withParameter(LootContextParams.BLOCK_STATE, state);

            List<ItemStack> drops = state.getDrops(lootParamsBuilder);
            if (drops.isEmpty()) {
                drops = state.getBlock().getDrops(state, serverLevel, woodPos, null, player, ItemStack.EMPTY);
            }

            if (!player.isCreative() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
                spawnGreenParticles(serverLevel, woodPos, greenParticle);
                level.setBlock(woodPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);

                for (ItemStack drop : drops) {
                    if (!drop.isEmpty()) {
                        ItemEntity itemEntity = new ItemEntity(level,
                                player.getX(), player.getY() + 1.0, player.getZ(), drop.copy());
                        itemEntity.setDefaultPickUpDelay();
                        level.addFreshEntity(itemEntity);
                    }
                }
            } else if (player.isCreative()) {
                spawnGreenParticles(serverLevel, woodPos, greenParticle);
                level.setBlock(woodPos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
            }

            level.playSound(null, woodPos, state.getSoundType().getBreakSound(),
                    SoundSource.BLOCKS, 1.0F, 0.8F);
        }
    }

    private void spawnGreenParticles(ServerLevel level, BlockPos pos, ParticleOptions particle) {
        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        for (int i = 0; i < 8; i++) {
            double offsetX = (level.random.nextDouble() - 0.5) * 0.8;
            double offsetY = (level.random.nextDouble() - 0.5) * 0.8;
            double offsetZ = (level.random.nextDouble() - 0.5) * 0.8;

            level.sendParticles(particle,
                    centerX + offsetX, centerY + offsetY, centerZ + offsetZ,
                    1, 0.0, 0.0, 0.0, 0.1);
        }

        for (int i = 0; i < 3; i++) {
            level.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    centerX, centerY, centerZ, 1, 0.0, 0.0, 0.0, 0.2);
        }
    }

    public static ClientWoodData getClientWoodData(UUID playerId) {
        ClientWoodData data = CLIENT_RENDER_DATA.get(playerId);
        if (data != null && System.currentTimeMillis() > data.expirationTime) {
            CLIENT_RENDER_DATA.remove(playerId);
            return null;
        }
        return data;
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance() {
        return new RootwardenData();
    }

    @Override
    public IPersistentSkillData getPersistentDataInstance(CompoundTag tag) {
        RootwardenData data = new RootwardenData();
        data.readData(tag);
        return data;
    }
}