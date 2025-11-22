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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidMarkingTalisman extends Item {
    private static final int COOLDOWN_TICKS = 30 * 20; // 30 seconds in ticks

    // Component data keys
    private static final String SAVED_X = "saved_x";
    private static final String SAVED_Y = "saved_y";
    private static final String SAVED_Z = "saved_z";
    private static final String SAVED_DIMENSION = "saved_dimension";

    // Player cooldown data keys
    private static final String COOLDOWN_TAG = "VoidMarkingCooldown";
    private static final String COOLDOWN_TIME_TAG = "VoidMarkingCooldownTime";

    public VoidMarkingTalisman(Properties properties) {
        super(properties.stacksTo(1));
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

            player.displayClientMessage(Component.literal("§aLocation saved!"), true);
            level.playSound(null, player.blockPosition(), SoundEvents.ENDER_EYE_DEATH,
                    SoundSource.PLAYERS, 1.0F, 1.0F);

            return InteractionResultHolder.success(itemstack);
        } else {
            // Teleport to saved location
            if (isOnCooldown(player)) {
                if (!level.isClientSide) {
                    int remainingTicks = getRemainingCooldownTicks(player);
                    int remainingSeconds = (remainingTicks + 19) / 20; // Round up
                    player.displayClientMessage(Component.literal(
                            String.format("§cTalisman on cooldown! %d seconds remaining", remainingSeconds)), true);
                }
                return InteractionResultHolder.fail(itemstack);
            }

            if (!hasSavedLocation(itemstack)) {
                if (!level.isClientSide) {
                    player.displayClientMessage(Component.literal("§cNo location saved! Shift-right-click to save current position."), true);
                }
                return InteractionResultHolder.fail(itemstack);
            }

            if (level.isClientSide) {
                level.playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResultHolder.consume(itemstack);
            }

            ServerPlayer serverPlayer = (ServerPlayer) player;
            ServerLevel currentLevel = (ServerLevel) level;

            // Get saved location data from component
            SavedLocationData locationData = getSavedLocation(itemstack);
            if (locationData == null) {
                player.displayClientMessage(Component.literal("§cFailed to read saved location!"), true);
                return InteractionResultHolder.fail(itemstack);
            }

            // Get target dimension using ResourceKey
            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, locationData.dimension);
            ServerLevel targetLevel = currentLevel.getServer().getLevel(dimensionKey);

            if (targetLevel == null) {
                player.displayClientMessage(Component.literal("§cCannot teleport to saved dimension!"), true);
                return InteractionResultHolder.fail(itemstack);
            }

            // Play departure effects
            currentLevel.playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            currentLevel.sendParticles(ParticleTypes.PORTAL,
                    serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ(),
                    50, 0.5, 1, 0.5, 0.1);

            // Perform the teleport
            serverPlayer.teleportTo(targetLevel, locationData.x, locationData.y, locationData.z,
                    serverPlayer.getYRot(), serverPlayer.getXRot());

            // Set cooldown
            setCooldown(player, COOLDOWN_TICKS);
            player.getCooldowns().addCooldown(this, 10);

            // Play arrival effects
            BlockPos newPos = serverPlayer.blockPosition();
            targetLevel.playSound(null, newPos, SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            targetLevel.sendParticles(ParticleTypes.PORTAL,
                    newPos.getX() + 0.5, newPos.getY() + 1, newPos.getZ() + 0.5,
                    50, 0.5, 1, 0.5, 0.1);

            player.displayClientMessage(Component.literal("§aTeleported to saved location!"), true);

            return InteractionResultHolder.success(itemstack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);

        SavedLocationData locationData = getSavedLocation(stack);
        if (locationData != null) {
            tooltip.add(Component.literal("§7Saved Location:"));
            tooltip.add(Component.literal(String.format("§7X: %.1f, Y: %.1f, Z: %.1f",
                    locationData.x, locationData.y, locationData.z)));
            tooltip.add(Component.literal("§7Dimension: " + locationData.dimension));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, net.minecraft.world.entity.Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            updateCooldown(player);

            int remainingTicks = getRemainingCooldownTicks(player);
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