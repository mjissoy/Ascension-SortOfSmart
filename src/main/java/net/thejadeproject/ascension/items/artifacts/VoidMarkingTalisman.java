package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidMarkingTalisman extends Item {
    private static final int COOLDOWN_TICKS = 30 * 20; // 30 seconds in ticks
    private static final int COUNTDOWN_TICKS = 5 * 20; // 5 seconds in ticks

    // Component data keys
    private static final String SAVED_X = "saved_x";
    private static final String SAVED_Y = "saved_y";
    private static final String SAVED_Z = "saved_z";
    private static final String SAVED_DIMENSION = "saved_dimension";

    // Player cooldown data keys
    private static final String COOLDOWN_TAG = "VoidMarkingCooldown";
    private static final String COOLDOWN_TIME_TAG = "VoidMarkingCooldownTime";
    private static final String COUNTDOWN_TAG = "VoidMarkingCountdown";
    private static final String INITIAL_POS_TAG = "VoidMarkingInitialPos";
    private static final String INITIAL_HEALTH_TAG = "VoidMarkingInitialHealth";

    public VoidMarkingTalisman(Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.RARE));
    }

    @Override
    public Component getName(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("CooldownMinutes") && tag.contains("CooldownSeconds")) {
                int minutes = tag.getInt("CooldownMinutes");
                int seconds = tag.getInt("CooldownSeconds");
                return Component.translatable("item.ascension.void_marking_talisman.cooldown", minutes, seconds);
            }
        }
        return Component.translatable("item.ascension.void_marking_talisman");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            // Save current location
            if (level.isClientSide) {
                level.playSound(player, player.blockPosition(), SoundEvents.ENDER_EYE_DEATH,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResultHolder.consume(itemstack);
            }

            saveLocation(itemstack, player.blockPosition(), level.dimension());

            player.displayClientMessage(Component.translatable("ascension.tooltip.location_saved"), true);
            level.playSound(null, player.blockPosition(), SoundEvents.ENDER_EYE_DEATH,
                    SoundSource.PLAYERS, 1.0F, 1.0F);

            return InteractionResultHolder.success(itemstack);
        } else {
            // Teleport to saved location
            if (isOnCooldown(player)) {
                return InteractionResultHolder.fail(itemstack);
            }

            if (!hasSavedLocation(itemstack)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.translatable("ascension.tooltip.no_location_saved"), true);
                }
                return InteractionResultHolder.fail(itemstack);
            }

            if (level.isClientSide) {
                level.playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResultHolder.consume(itemstack);
            }

            ServerPlayer serverPlayer = (ServerPlayer) player;

            // Start countdown for teleportation
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

        // Consume item immediately
        if (!player.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        // Initial countdown message
        player.displayClientMessage(Component.translatable("ascension.tooltip.teleporting_in_seconds", 5), true);
    }

    private void attemptTeleport(ServerPlayer player) {
        ServerLevel currentLevel = (ServerLevel) player.level();

        // Get saved location data from component
        SavedLocationData locationData = getSavedLocation(player.getMainHandItem());
        if (locationData == null) {
            player.displayClientMessage(Component.translatable("ascension.tooltip.failed_read_location"), true);
            clearCountdownData(player);
            return;
        }

        // Get target dimension using ResourceKey
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, locationData.dimension);
        ServerLevel targetLevel = currentLevel.getServer().getLevel(dimensionKey);

        if (targetLevel == null) {
            player.displayClientMessage(Component.translatable("ascension.tooltip.cannot_teleport_dimension"), true);
            clearCountdownData(player);
            return;
        }

        // Perform the teleport
        player.teleportTo(targetLevel, locationData.x, locationData.y, locationData.z,
                player.getYRot(), player.getXRot());

        // Set cooldown
        setCooldown(player, COOLDOWN_TICKS);
        player.getCooldowns().addCooldown(this, 10);

        // Play arrival effects
        BlockPos newPos = player.blockPosition();
        targetLevel.playSound(null, newPos, SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        targetLevel.sendParticles(ParticleTypes.PORTAL,
                newPos.getX() + 0.5, newPos.getY() + 1, newPos.getZ() + 0.5,
                50, 0.5, 1, 0.5, 0.1);

        player.displayClientMessage(Component.translatable("ascension.tooltip.teleported_to_saved"), true);

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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        SavedLocationData locationData = getSavedLocation(stack);
        if (locationData != null) {
            tooltip.add(Component.translatable("ascension.tooltip.saved_location"));
            tooltip.add(Component.translatable("ascension.tooltip.coordinates",
                    String.format("%.1f", locationData.x),
                    String.format("%.1f", locationData.y),
                    String.format("%.1f", locationData.z)));
            tooltip.add(Component.translatable("ascension.tooltip.dimension", locationData.dimension));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            updateCooldown(player);

            // Update cooldown display on the item stack using new data component system
            int remainingTicks = getRemainingCooldownTicks(player);
            if (remainingTicks > 0) {
                int remainingSeconds = remainingTicks / 20;
                int minutes = remainingSeconds / 60;
                int seconds = remainingSeconds % 60;

                // Get existing custom data or create new
                CompoundTag cooldownTag = new CompoundTag();
                CustomData existingData = stack.get(DataComponents.CUSTOM_DATA);
                if (existingData != null) {
                    cooldownTag = existingData.copyTag();
                }

                cooldownTag.putInt("CooldownMinutes", minutes);
                cooldownTag.putInt("CooldownSeconds", seconds);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(cooldownTag));
            } else {
                // Remove only cooldown data, keep saved location data
                CustomData existingData = stack.get(DataComponents.CUSTOM_DATA);
                if (existingData != null) {
                    CompoundTag tag = existingData.copyTag();
                    tag.remove("CooldownMinutes");
                    tag.remove("CooldownSeconds");
                    if (!tag.isEmpty()) {
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    } else {
                        stack.remove(DataComponents.CUSTOM_DATA);
                    }
                }
            }

            // Handle countdown
            CompoundTag persistentData = player.getPersistentData();
            if (persistentData.contains(COUNTDOWN_TAG)) {
                int countdown = persistentData.getInt(COUNTDOWN_TAG);

                // Check for cancellation conditions
                if (hasPlayerMoved(player) || hasPlayerTakenDamage(player)) {
                    String reason = hasPlayerTakenDamage(player) ?
                            Component.translatable("ascension.tooltip.damage_taken").getString() :
                            Component.translatable("ascension.tooltip.movement_detected").getString();
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
        return hasSavedLocation(stack);
    }

    private void saveLocation(ItemStack stack, BlockPos pos, ResourceKey<Level> dimension) {
        // Create component data for the saved location
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(createLocationTag(pos, dimension)));
    }

    private CompoundTag createLocationTag(BlockPos pos, ResourceKey<Level> dimension) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble(SAVED_X, pos.getX() + 0.5D);
        tag.putDouble(SAVED_Y, pos.getY());
        tag.putDouble(SAVED_Z, pos.getZ() + 0.5D);
        tag.putString(SAVED_DIMENSION, dimension.location().toString());
        return tag;
    }

    private @Nullable SavedLocationData getSavedLocation(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains(SAVED_X) && tag.contains(SAVED_Y) && tag.contains(SAVED_Z) && tag.contains(SAVED_DIMENSION)) {
                return new SavedLocationData(
                        tag.getDouble(SAVED_X),
                        tag.getDouble(SAVED_Y),
                        tag.getDouble(SAVED_Z),
                        ResourceLocation.parse(tag.getString(SAVED_DIMENSION))
                );
            }
        }
        return null;
    }

    private boolean hasSavedLocation(ItemStack stack) {
        return getSavedLocation(stack) != null;
    }

    private boolean isOnCooldown(Player player) {
        return getRemainingCooldownTicks(player) > 0;
    }

    private int getRemainingCooldownTicks(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains(COOLDOWN_TAG)) {
            return persistentData.getInt(COOLDOWN_TAG);
        }
        return 0;
    }

    private void setCooldown(Player player, int cooldownTicks) {
        CompoundTag persistentData = player.getPersistentData();
        persistentData.putInt(COOLDOWN_TAG, cooldownTicks);
        persistentData.putLong(COOLDOWN_TIME_TAG, System.currentTimeMillis());
    }

    private void updateCooldown(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (persistentData.contains(COOLDOWN_TAG)) {
            int remainingCooldown = persistentData.getInt(COOLDOWN_TAG);
            if (remainingCooldown > 0) {
                long lastUpdateTime = persistentData.getLong(COOLDOWN_TIME_TAG);
                long currentTime = System.currentTimeMillis();
                long timePassed = (currentTime - lastUpdateTime) / 50;

                if (timePassed > 0) {
                    remainingCooldown = Math.max(0, remainingCooldown - (int) timePassed);
                    persistentData.putInt(COOLDOWN_TAG, remainingCooldown);
                    persistentData.putLong(COOLDOWN_TIME_TAG, currentTime);
                }
            }
        }
    }

    // Helper record to store location data
    private record SavedLocationData(double x, double y, double z, ResourceLocation dimension) {}
}