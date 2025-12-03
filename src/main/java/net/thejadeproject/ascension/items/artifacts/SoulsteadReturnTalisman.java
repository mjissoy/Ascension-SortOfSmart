package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class SoulsteadReturnTalisman extends Item {
    private static final int COOLDOWN_TICKS = 5 * 60 * 20; // 5 minutes in ticks
    private static final int COUNTDOWN_TICKS = 5 * 20; // 5 seconds in ticks

    private static final String GLOBAL_COOLDOWN_TAG = "SoulsteadReturnCooldownHome";
    private static final String GLOBAL_COOLDOWN_TIME_TAG = "SoulsteadReturnCooldownTimeHome";
    private static final String COUNTDOWN_TAG = "SoulsteadReturnCountdown";
    private static final String INITIAL_POS_TAG = "SoulsteadReturnInitialPos";
    private static final String INITIAL_HEALTH_TAG = "SoulsteadReturnInitialHealth";

    public SoulsteadReturnTalisman(Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public Component getName(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("CooldownMinutes") && tag.contains("CooldownSeconds")) {
                int minutes = tag.getInt("CooldownMinutes");
                int seconds = tag.getInt("CooldownSeconds");
                return Component.translatable("item.ascension.soulstead_return_talisman.cooldown", minutes, seconds);
            }
        }
        return Component.translatable("item.ascension.soulstead_return_talisman");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (isOnGlobalCooldown(player)) {
            return InteractionResultHolder.fail(itemstack);
        }

        if (level.isClientSide) {
            level.playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.consume(itemstack);
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;

        // Start countdown
        startCountdown(serverPlayer, itemstack);

        // Play departure effects
        ServerLevel serverLevel = (ServerLevel) level;
        serverLevel.playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        serverLevel.sendParticles(ParticleTypes.PORTAL,
                serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ(),
                50, 0.5, 1, 0.5, 0.1);

        return InteractionResultHolder.success(itemstack);
    }

    private void startCountdown(ServerPlayer player, ItemStack itemstack) {
        CompoundTag persistentData = player.getPersistentData();
        persistentData.putInt(COUNTDOWN_TAG, COUNTDOWN_TICKS);

        // Store initial position for movement check
        CompoundTag posTag = new CompoundTag();
        posTag.putDouble("x", player.getX());
        posTag.putDouble("y", player.getY());
        posTag.putDouble("z", player.getZ());
        persistentData.put(INITIAL_POS_TAG, posTag);

        // Store initial health for damage check
        persistentData.putFloat(INITIAL_HEALTH_TAG, player.getHealth());


        // Initial countdown message
        player.displayClientMessage(Component.translatable("ascension.tooltip.teleporting_in_seconds", 5), true);
    }

    private void attemptTeleport(ServerPlayer player) {
        ServerLevel serverLevel = (ServerLevel) player.level();

        // Get the target dimension and position
        ResourceKey<Level> respawnDim = player.getRespawnDimension();
        ServerLevel targetLevel = serverLevel.getServer().getLevel(respawnDim);
        if (targetLevel == null) {
            targetLevel = serverLevel.getServer().overworld();
        }

        BlockPos respawnPos = player.getRespawnPosition();
        if (respawnPos == null) {
            respawnPos = targetLevel.getSharedSpawnPos();
        }

        double x = respawnPos.getX() + 0.5D;
        double y = respawnPos.getY();
        double z = respawnPos.getZ() + 0.5D;

        // Perform the teleport
        player.teleportTo(targetLevel, x, y, z, player.getYRot(), player.getXRot());

        // Teleportation successful
        setGlobalCooldown(player, COOLDOWN_TICKS);
        player.getCooldowns().addCooldown(this, 10);

        // Play arrival effects
        BlockPos newPos = player.blockPosition();
        targetLevel.playSound(null, newPos, SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        targetLevel.sendParticles(ParticleTypes.PORTAL,
                newPos.getX() + 0.5, newPos.getY() + 1, newPos.getZ() + 0.5,
                50, 0.5, 1, 0.5, 0.1);

        player.displayClientMessage(Component.translatable("ascension.tooltip.teleported_to_spawn"), true);

        // Consume the item now that teleport is successful
        ItemStack heldItem = player.getMainHandItem();
        if (!player.getAbilities().instabuild && heldItem.getItem() == this) {
            heldItem.shrink(1);
        }

        // Clear countdown data
        clearCountdownData(player);
    }

    private void cancelTeleport(ServerPlayer player, String reason) {
        player.displayClientMessage(Component.translatable("ascension.tooltip.teleport_cancelled", reason), true);
        clearCountdownData(player);
    }

    private void clearCountdownData(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        persistentData.remove(COUNTDOWN_TAG);
        persistentData.remove(INITIAL_POS_TAG);
        persistentData.remove(INITIAL_HEALTH_TAG);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            updateGlobalCooldown(player);

            // Update cooldown display on the item stack using new data component system
            int remainingTicks = getRemainingGlobalCooldownTicks(player);
            if (remainingTicks > 0) {
                int remainingSeconds = remainingTicks / 20;
                int minutes = remainingSeconds / 60;
                int seconds = remainingSeconds % 60;

                CompoundTag cooldownTag = new CompoundTag();
                cooldownTag.putInt("CooldownMinutes", minutes);
                cooldownTag.putInt("CooldownSeconds", seconds);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(cooldownTag));
            } else {
                // Remove cooldown data when cooldown is over
                stack.remove(DataComponents.CUSTOM_DATA);
            }

            // Handle countdown
            CompoundTag persistentData = player.getPersistentData();
            if (persistentData.contains(COUNTDOWN_TAG)) {
                int countdown = persistentData.getInt(COUNTDOWN_TAG);

                // Check for cancellation conditions
                if (hasPlayerMoved(player) || hasPlayerTakenDamage(player) || !hasItemInMainHand(player)) {
                    String reason = hasPlayerTakenDamage(player) ?
                            Component.translatable("ascension.tooltip.damage_taken").getString() :
                            (hasPlayerMoved(player) ?
                                    Component.translatable("ascension.tooltip.movement_detected").getString() :
                                    Component.translatable("ascension.tooltip.item_not_in_hand").getString());
                    cancelTeleport(player, reason);
                    return;
                }

                if (countdown > 0) {
                    countdown--;
                    persistentData.putInt(COUNTDOWN_TAG, countdown);

                    // Update countdown message every second
                    if (countdown % 20 == 0) {
                        int seconds = countdown / 20;
                        player.displayClientMessage(Component.translatable("ascension.tooltip.teleporting_in_seconds", seconds), true);
                    }

                    if (countdown == 0) {
                        attemptTeleport(player);
                    }
                }
            }

            if (remainingTicks > 0) {
                player.getCooldowns().addCooldown(this, Math.min(remainingTicks, 200));
            } else if (player.getCooldowns().isOnCooldown(this)) {
                player.getCooldowns().removeCooldown(this);
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    private boolean hasItemInMainHand(ServerPlayer player) {
        ItemStack mainHandItem = player.getMainHandItem();
        return mainHandItem.getItem() == this;
    }

    private boolean hasPlayerMoved(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains(INITIAL_POS_TAG)) {
            CompoundTag posTag = persistentData.getCompound(INITIAL_POS_TAG);
            double initialX = posTag.getDouble("x");
            double initialY = posTag.getDouble("y");
            double initialZ = posTag.getDouble("z");

            // Allow small movements (like looking around) but not significant position changes
            double distanceMoved = Math.sqrt(
                    Math.pow(player.getX() - initialX, 2) +
                            Math.pow(player.getY() - initialY, 2) +
                            Math.pow(player.getZ() - initialZ, 2)
            );

            return distanceMoved > 0.5; // Allow small position adjustments
        }
        return false;
    }

    private boolean hasPlayerTakenDamage(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains(INITIAL_HEALTH_TAG) && persistentData.contains(INITIAL_POS_TAG)) {
            float initialHealth = persistentData.getFloat(INITIAL_HEALTH_TAG);
            return player.getHealth() < initialHealth;
        }
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        // Remove the cooldown check from foil effect
        return super.isFoil(stack);
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