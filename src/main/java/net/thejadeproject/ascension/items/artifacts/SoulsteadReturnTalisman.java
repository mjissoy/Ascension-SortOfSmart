package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class SoulsteadReturnTalisman extends Item {
    private static final int COOLDOWN_TICKS = 5 * 60 * 20; // 60 minutes in ticks

    private static final String GLOBAL_COOLDOWN_TAG = "SoulsteadReturnCooldownHome";
    private static final String GLOBAL_COOLDOWN_TIME_TAG = "SoulsteadReturnCooldownTimeHome";

    public SoulsteadReturnTalisman(Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (isOnGlobalCooldown(player)) {
            if (!level.isClientSide) {
                int remainingTicks = getRemainingGlobalCooldownTicks(player);
                int remainingSeconds = remainingTicks / 20;
                int minutes = remainingSeconds / 60;
                int seconds = remainingSeconds % 60;
                player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                        String.format("§cTalisman on cooldown! %d:%02d remaining", minutes, seconds)), true);
            }
            return InteractionResultHolder.fail(itemstack);
        }

        if (level.isClientSide) {
            level.playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.consume(itemstack);
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;
        ServerLevel serverLevel = (ServerLevel) level;

        // Get the target dimension and position
        ResourceKey<Level> respawnDim = serverPlayer.getRespawnDimension();
        ServerLevel targetLevel = serverLevel.getServer().getLevel(respawnDim);
        if (targetLevel == null) {
            targetLevel = serverLevel.getServer().overworld();
        }

        BlockPos respawnPos = serverPlayer.getRespawnPosition();
        if (respawnPos == null) {
            respawnPos = targetLevel.getSharedSpawnPos();
        }

        double x = respawnPos.getX() + 0.5D;
        double y = respawnPos.getY();
        double z = respawnPos.getZ() + 0.5D;

        // Play departure effects
        serverLevel.playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        serverLevel.sendParticles(ParticleTypes.PORTAL,
                serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ(),
                50, 0.5, 1, 0.5, 0.1);

        // Perform the teleport
        serverPlayer.teleportTo(targetLevel, x, y, z, serverPlayer.getYRot(), serverPlayer.getXRot());

        // Teleportation successful
        setGlobalCooldown(serverPlayer, COOLDOWN_TICKS);
        player.getCooldowns().addCooldown(this, 10);

        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        // Play arrival effects
        BlockPos newPos = serverPlayer.blockPosition();
        targetLevel.playSound(null, newPos, SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        targetLevel.sendParticles(ParticleTypes.PORTAL,
                newPos.getX() + 0.5, newPos.getY() + 1, newPos.getZ() + 0.5,
                50, 0.5, 1, 0.5, 0.1);

        player.displayClientMessage(net.minecraft.network.chat.Component.literal("§aTeleported to your spawn point!"), true);

        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            updateGlobalCooldown(player);

            int remainingTicks = getRemainingGlobalCooldownTicks(player);
            if (remainingTicks > 0) {
                player.getCooldowns().addCooldown(this, Math.min(remainingTicks, 200));
            } else if (player.getCooldowns().isOnCooldown(this)) {
                player.getCooldowns().removeCooldown(this);
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return !isItemOnCooldown(stack) && super.isFoil(stack);
    }

    private boolean isItemOnCooldown(ItemStack stack) {
        return false;
    }

    private boolean isOnGlobalCooldown(Player player) {
        return getRemainingGlobalCooldownTicks(player) > 0;
    }

    private int getRemainingGlobalCooldownTicks(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains(GLOBAL_COOLDOWN_TAG)) {
            return persistentData.getInt(GLOBAL_COOLDOWN_TAG);
        }
        return 0;
    }

    private void setGlobalCooldown(Player player, int cooldownTicks) {
        CompoundTag persistentData = player.getPersistentData();
        persistentData.putInt(GLOBAL_COOLDOWN_TAG, cooldownTicks);
        persistentData.putLong(GLOBAL_COOLDOWN_TIME_TAG, System.currentTimeMillis());
    }

    private void updateGlobalCooldown(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains(GLOBAL_COOLDOWN_TAG)) {
            int remainingCooldown = persistentData.getInt(GLOBAL_COOLDOWN_TAG);
            if (remainingCooldown > 0) {
                long lastUpdateTime = persistentData.getLong(GLOBAL_COOLDOWN_TIME_TAG);
                long currentTime = System.currentTimeMillis();
                long timePassed = (currentTime - lastUpdateTime) / 50;

                if (timePassed > 0) {
                    remainingCooldown = Math.max(0, remainingCooldown - (int) timePassed);
                    persistentData.putInt(GLOBAL_COOLDOWN_TAG, remainingCooldown);
                    persistentData.putLong(GLOBAL_COOLDOWN_TIME_TAG, currentTime);
                }
            }
        }
    }
}