package net.thejadeproject.ascension.items.artifacts;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.network.chat.Component;
import net.thejadeproject.ascension.events.api.SpatialRuptureAPI;
import net.thejadeproject.ascension.items.artifacts.bases.BaseTeleportTalisman;

import java.util.concurrent.CompletableFuture;

public class SpatialRuptureTalismanT3 extends BaseTeleportTalisman {
    private static final int TELEPORT_RADIUS = 7500;
    private static final int COOLDOWN_TICKS = 20 * 60 * 20; // 20 minutes

    public SpatialRuptureTalismanT3(Properties properties) {
        super(properties.rarity(Rarity.RARE));
    }

    // NBT key methods
    @Override protected String getCooldownTag() { return "SpatialRuptureT3_Cooldown"; }
    @Override protected String getCooldownTimeTag() { return "SpatialRuptureT3_CooldownTime"; }
    @Override protected String getCountdownTag() { return "SpatialRuptureT3_Countdown"; }
    @Override protected String getInitialPosTag() { return "SpatialRuptureT3_InitialPos"; }
    @Override protected String getInitialHealthTag() { return "SpatialRuptureT3_InitialHealth"; }
    @Override protected String getUsedHandTag() { return "SpatialRuptureT3_UsedHand"; }
    @Override protected String getUsedSlotTag() { return "SpatialRuptureT3_UsedSlot"; }

    // Config
    @Override protected int getCooldownTicks() { return COOLDOWN_TICKS; }
    @Override protected int getCountdownTicks() { return 5 * TICKS_PER_SECOND; }
    @Override protected Rarity getTalismanRarity() { return Rarity.EPIC; }
    @Override protected String getDisplayNameKey() { return "item.ascension.spatial_rupture_talisman_t3"; }

    @Override
    protected void performTeleport(ServerPlayer player, ItemStack usedStack, int usedSlot) {
        ServerLevel level = (ServerLevel) player.level();

        // Async teleport
        CompletableFuture<Boolean> future = SpatialRuptureAPI.randomTeleport(player, level, TELEPORT_RADIUS);

        future.thenAccept(success -> {
            level.getServer().execute(() -> {
                finalizeTeleport(player, usedStack, usedSlot, success, true, "ascension.teleport.success.random");
            });
        });
    }
}