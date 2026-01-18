package net.thejadeproject.ascension.items.artifacts.bases;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class BaseTeleportTalisman extends Item {
    // Performance constants
    protected static final int TICKS_PER_SECOND = 20;
    protected static final double MOVEMENT_THRESHOLD_SQ = 1.0 * 1.0; // Squared distance for performance
    protected static final double Y_MOVEMENT_THRESHOLD = 0.5;

    // Standardized translation keys (all talismans use these)
    public static final String TRANSLOC_COUNTDOWN = "ascension.teleport.countdown";
    public static final String TRANSLOC_CANCELLED = "ascension.teleport.cancelled";
    public static final String TRANSLOC_CANCEL_MOVE = "ascension.teleport.cancel.movement";
    public static final String TRANSLOC_CANCEL_DAMAGE = "ascension.teleport.cancel.damage";
    public static final String TRANSLOC_CANCEL_NO_ITEM = "ascension.teleport.cancel.no_item";

    // Generic active teleport flag
    public static final String TELEPORT_ACTIVE = "TeleportActive";

    public BaseTeleportTalisman(Properties properties) {
        super(properties.stacksTo(16));
    }

    // Abstract methods for unique NBT keys per subclass
    protected abstract String getCooldownTag();
    protected abstract String getCooldownTimeTag();
    protected abstract String getCountdownTag();
    protected abstract String getInitialPosTag();
    protected abstract String getInitialHealthTag();
    protected abstract String getUsedHandTag();
    protected abstract String getUsedSlotTag();

    // Abstract configuration
    protected abstract int getCooldownTicks();
    protected abstract int getCountdownTicks();
    protected abstract Rarity getTalismanRarity();
    protected abstract String getDisplayNameKey();

    // Abstract teleport execution (subclass implements specific teleport logic)
    protected abstract void performTeleport(ServerPlayer player, ItemStack usedStack, int usedSlot);

    @Override
    public Component getName(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTag();
            if (tag.contains("CooldownMinutes") && tag.contains("CooldownSeconds")) {
                int minutes = tag.getInt("CooldownMinutes");
                int seconds = tag.getInt("CooldownSeconds");
                return Component.translatable(getDisplayNameKey() + ".cooldown", minutes, seconds);
            }
        }
        return Component.translatable(getDisplayNameKey());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (isOnCooldown(player)) {
            return InteractionResultHolder.fail(stack);
        }

        // Client side: just play sound
        if (level.isClientSide) {
            level.playSound(player, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResultHolder.consume(stack);
        }

        ServerPlayer serverPlayer = (ServerPlayer) player;

        // Track exact slot: main hand = current selected, offhand = 40
        int slot = (hand == InteractionHand.MAIN_HAND) ?
                serverPlayer.getInventory().selected : 40;

        startCountdown(serverPlayer, hand, slot);
        playDepartureEffects(serverPlayer);

        return InteractionResultHolder.success(stack);
    }

    protected void startCountdown(ServerPlayer player, InteractionHand hand, int slot) {
        CompoundTag data = player.getPersistentData();
        data.putInt(getCountdownTag(), getCountdownTicks());
        data.putBoolean(TELEPORT_ACTIVE, true);

        // Store position
        CompoundTag posTag = new CompoundTag();
        posTag.putDouble("x", player.getX());
        posTag.putDouble("y", player.getY());
        posTag.putDouble("z", player.getZ());
        posTag.putBoolean("onGround", player.onGround());
        data.put(getInitialPosTag(), posTag);

        // Store health
        data.putFloat(getInitialHealthTag(), player.getHealth());

        // Store hand and slot for precise consumption
        data.putString(getUsedHandTag(), hand.name());
        data.putInt(getUsedSlotTag(), slot);

        // Initial message
        player.displayClientMessage(
                Component.translatable(TRANSLOC_COUNTDOWN, getCountdownTicks() / TICKS_PER_SECOND),
                true
        );
    }

    protected void playDepartureEffects(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        level.playSound(null, player.blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.PORTAL,
                player.getX(), player.getY() + 1, player.getZ(),
                50, 0.5, 1, 0.5, 0.1);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof ServerPlayer player) {
            updateCooldownDisplay(stack, player);
            handleCountdown(player);
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    protected void updateCooldownDisplay(ItemStack stack, ServerPlayer player) {
        int remainingTicks = getRemainingCooldownTicks(player);
        if (remainingTicks > 0) {
            int totalSeconds = remainingTicks / TICKS_PER_SECOND;
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;

            CompoundTag tag = new CompoundTag();
            tag.putInt("CooldownMinutes", minutes);
            tag.putInt("CooldownSeconds", seconds);

            // Let subclasses preserve extra data (like saved locations)
            preserveCustomData(stack, tag);

            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            player.getCooldowns().addCooldown(this, Math.min(remainingTicks, 200));
        } else {
            // Only clear cooldown display, preserve other data
            clearCooldownDisplayOnly(stack);

            if (player.getCooldowns().isOnCooldown(this)) {
                player.getCooldowns().removeCooldown(this);
            }
        }
    }

    // Override in VoidMarkingTalisman to preserve saved location data
    protected void preserveCustomData(ItemStack stack, CompoundTag cooldownTag) {
        // Default: replace all data
    }

    // Override in VoidMarkingTalisman
    protected void clearCooldownDisplayOnly(ItemStack stack) {
        stack.remove(DataComponents.CUSTOM_DATA);
    }

    protected void handleCountdown(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains(getCountdownTag())) {
            return;
        }

        int countdown = data.getInt(getCountdownTag());
        if (countdown <= 0) {
            return;
        }

        // Check cancellation conditions
        String cancelReason = getCancellationReason(player);
        if (cancelReason != null) {
            cancelTeleport(player, cancelReason);
            return;
        }

        // Decrement counter
        countdown--;
        data.putInt(getCountdownTag(), countdown);

        // Update message every second
        if (countdown % TICKS_PER_SECOND == 0) {
            int seconds = countdown / TICKS_PER_SECOND;
            player.displayClientMessage(
                    Component.translatable(TRANSLOC_COUNTDOWN, seconds),
                    true
            );
        }

        // Execute teleport when ready
        if (countdown == 0) {
            executeTeleport(player);
        }
    }

    @Nullable
    protected String getCancellationReason(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        // Verify item is still in the correct slot/hand
        if (!hasCorrectItemInSlot(player)) {
            return TRANSLOC_CANCEL_NO_ITEM;
        }

        // Check for significant movement
        if (hasSignificantMovement(player)) {
            return TRANSLOC_CANCEL_MOVE;
        }

        // Check for health loss (damage)
        if (data.contains(getInitialHealthTag())) {
            float initialHealth = data.getFloat(getInitialHealthTag());
            if (player.getHealth() < initialHealth - 0.001F) {
                return TRANSLOC_CANCEL_DAMAGE;
            }
        }

        return null;
    }

    protected boolean hasCorrectItemInSlot(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains(getUsedHandTag()) || !data.contains(getUsedSlotTag())) {
            return false;
        }

        String handName = data.getString(getUsedHandTag());
        int expectedSlot = data.getInt(getUsedSlotTag());

        InteractionHand hand = InteractionHand.valueOf(handName);
        ItemStack itemInHand = player.getItemInHand(hand);

        // Verify item type matches
        if (itemInHand.getItem() != this) {
            return false;
        }

        // For main hand, verify slot hasn't changed
        if (hand == InteractionHand.MAIN_HAND) {
            return player.getInventory().selected == expectedSlot;
        }

        return true; // Offhand doesn't use slot index
    }

    protected boolean hasSignificantMovement(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (!data.contains(getInitialPosTag())) {
            return false;
        }

        CompoundTag posTag = data.getCompound(getInitialPosTag());
        double x0 = posTag.getDouble("x");
        double y0 = posTag.getDouble("y");
        double z0 = posTag.getDouble("z");
        boolean wasOnGround = posTag.getBoolean("onGround");

        double dx = player.getX() - x0;
        double dy = player.getY() - y0;
        double dz = player.getZ() - z0;

        // Horizontal movement check
        if (dx * dx + dz * dz > MOVEMENT_THRESHOLD_SQ) {
            return true;
        }

        // Vertical check (allow small falls/jumps if on ground)
        if (Math.abs(dy) > Y_MOVEMENT_THRESHOLD) {
            if (!wasOnGround || Math.abs(dy) > 1.0) {
                return true;
            }
        }

        // Riding entities cancel teleport
        return player.getVehicle() != null;
    }

    protected void executeTeleport(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();

        String handName = data.getString(getUsedHandTag());
        int slot = data.getInt(getUsedSlotTag());

        InteractionHand hand = InteractionHand.valueOf(handName);
        ItemStack usedStack = player.getItemInHand(hand);

        // Final validation
        if (usedStack.getItem() != this) {
            cancelTeleport(player, TRANSLOC_CANCEL_NO_ITEM);
            return;
        }

        // Subclass implements actual teleport logic
        performTeleport(player, usedStack, slot);
    }

    protected void cancelTeleport(ServerPlayer player, String reasonKey) {
        player.displayClientMessage(
                Component.translatable(TRANSLOC_CANCELLED, Component.translatable(reasonKey)),
                true
        );
        clearCountdownData(player);
    }

    protected void clearCountdownData(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.remove(getCountdownTag());
        data.remove(TELEPORT_ACTIVE);
        data.remove(getInitialPosTag());
        data.remove(getInitialHealthTag());
        data.remove(getUsedHandTag());
        data.remove(getUsedSlotTag());
    }

    // Cooldown methods
    protected boolean isOnCooldown(Player player) {
        return getRemainingCooldownTicks(player) > 0;
    }

    protected int getRemainingCooldownTicks(Player player) {
        CompoundTag data = player.getPersistentData();
        String cooldownTag = getCooldownTag();
        String timeTag = getCooldownTimeTag();

        if (!data.contains(cooldownTag) || !data.contains(timeTag)) {
            return 0;
        }

        int remaining = data.getInt(cooldownTag);
        long lastUpdate = data.getLong(timeTag);
        long elapsedTicks = (System.currentTimeMillis() - lastUpdate) / 50;

        return Math.max(0, remaining - (int) elapsedTicks);
    }

    protected void startCooldown(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.putInt(getCooldownTag(), getCooldownTicks());
        data.putLong(getCooldownTimeTag(), System.currentTimeMillis());
    }

    // Helper methods for subclasses
    protected void playArrivalEffects(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        BlockPos pos = player.blockPosition();
        level.playSound(null, pos, SoundEvents.ENDERMAN_TELEPORT,
                SoundSource.PLAYERS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.PORTAL,
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                50, 0.5, 1, 0.5, 0.1);
    }

    protected void consumeItem(ServerPlayer player, ItemStack stack, int slot) {
        if (player.getAbilities().instabuild) {
            return;
        }

        // Try exact slot first (most reliable)
        if (slot >= 0 && slot < player.getInventory().getContainerSize()) {
            ItemStack inSlot = player.getInventory().getItem(slot);
            if (inSlot.getItem() == this) {
                inSlot.shrink(1);
                return;
            }
        }

        // Fallback to whichever hand currently holds this item
        if (player.getMainHandItem().getItem() == this) {
            player.getMainHandItem().shrink(1);
        } else if (player.getOffhandItem().getItem() == this) {
            player.getOffhandItem().shrink(1);
        }
    }

    protected boolean isPlayerValid(ServerPlayer player) {
        return player.isAlive() && !player.hasDisconnected() && player.getServer() != null;
    }

    /**
     * Finalizes teleport result for async operations (handles both success and failure)
     * @param player The player
     * @param usedStack The item stack used
     * @param usedSlot The slot where the item was
     * @param success Whether teleport succeeded
     * @param consumeOnFailure Whether to consume item even on failure (true for SpatialRupture)
     * @param successMessageKey Translation key for success message
     */
    protected void finalizeTeleport(ServerPlayer player, ItemStack usedStack, int usedSlot,
                                    boolean success, boolean consumeOnFailure, String successMessageKey) {
        if (!isPlayerValid(player)) {
            clearCountdownData(player);
            return;
        }

        if (success) {
            playArrivalEffects(player);
            startCooldown(player);
            consumeItem(player, usedStack, usedSlot);
            player.displayClientMessage(Component.translatable(successMessageKey), true);
        } else {
            if (consumeOnFailure) {
                consumeItem(player, usedStack, usedSlot);
            }
            player.displayClientMessage(Component.translatable("ascension.teleport.failed.no_safe_location"), true);
        }

        clearCountdownData(player);
    }
}