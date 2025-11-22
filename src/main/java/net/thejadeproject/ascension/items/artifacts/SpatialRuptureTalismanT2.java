package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.thejadeproject.ascension.events.api.SpatialRuptureAPI;

import java.util.concurrent.CompletableFuture;

public class SpatialRuptureTalismanT2 extends Item {
    private static final int TELEPORT_RADIUS = 5000; // 5k blocks radius
    private static final int COOLDOWN_TICKS = 40 * 60 * 20; // 40 minutes in ticks

    private static final String GLOBAL_COOLDOWN_TAG = "SpatialRuptureCooldownT2";
    private static final String GLOBAL_COOLDOWN_TIME_TAG = "SpatialRuptureCooldownTimeT2";

    public SpatialRuptureTalismanT2(Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.RARE));
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

        // Use the API for teleportation with T2 radius
        CompletableFuture<Boolean> teleportFuture = SpatialRuptureAPI.randomTeleport(serverPlayer, serverLevel, TELEPORT_RADIUS);

        teleportFuture.thenAccept(success -> {
            serverLevel.getServer().execute(() -> {
                if (success) {
                    setGlobalCooldown(serverPlayer, COOLDOWN_TICKS);
                    player.getCooldowns().addCooldown(this, 10);

                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    BlockPos playerPos = serverPlayer.blockPosition();
                    serverLevel.playSound(null, playerPos, SoundEvents.ENDERMAN_TELEPORT,
                            SoundSource.PLAYERS, 1.0F, 1.0F);
                    serverLevel.sendParticles(ParticleTypes.PORTAL,
                            playerPos.getX() + 0.5, playerPos.getY() + 1, playerPos.getZ() + 0.5,
                            50, 0.5, 1, 0.5, 0.1);

                    player.displayClientMessage(net.minecraft.network.chat.Component.literal("§aTeleported to a random location!"), true);
                } else {
                    player.displayClientMessage(net.minecraft.network.chat.Component.literal("§cNo safe teleport location found!"), true);
                }
            });
        });

        // Play departure effects
        serverLevel.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        serverLevel.sendParticles(ParticleTypes.PORTAL,
                player.getX(), player.getY() + 1, player.getZ(),
                50, 0.5, 1, 0.5, 0.1);

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