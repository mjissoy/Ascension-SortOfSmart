package net.thejadeproject.ascension.sects.missions;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.sects.Sect;
import net.thejadeproject.ascension.sects.SectManager;

import java.util.*;

public class SectMissionEventHandler {

    private final Map<UUID, Map<String, Integer>> playerKillCounts = new HashMap<>();
    // FIXED: Use weak references to prevent memory leaks
    private static final Set<UUID> notifiedPlayers = Collections.newSetFromMap(new WeakHashMap<>());

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            checkMissionCompletions(player);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (entity instanceof Mob && source.getEntity() instanceof ServerPlayer player) {
            String mobName = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
            trackMobKill(player, mobName);
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // Reserved for future craft-based missions
        }
    }

    private void trackMobKill(ServerPlayer player, String mobName) {
        UUID playerId = player.getUUID();
        playerKillCounts.putIfAbsent(playerId, new HashMap<>());
        Map<String, Integer> kills = playerKillCounts.get(playerId);
        kills.put(mobName, kills.getOrDefault(mobName, 0) + 1);
        updateKillMissions(player, mobName);
    }

    private void updateKillMissions(ServerPlayer player, String mobName) {
        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager == null) return;
        Sect sect = manager.getPlayerSect(player.getUUID());
        if (sect == null) return;
        for (SectMission mission : sect.getMissions()) {
            if (mission.isAcceptedBy(player.getUUID())) {
                MissionProgress progress = mission.getProgress(player.getUUID());
                if (progress != null && !progress.isCompleted()) {
                    updateMissionProgress(player, sect, mission, progress);
                }
            }
        }
    }

    private void checkMissionCompletions(ServerPlayer player) {
        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager == null) return;
        Sect sect = manager.getPlayerSect(player.getUUID());
        if (sect == null) return;
        for (SectMission mission : sect.getMissions()) {
            if (mission.isAcceptedBy(player.getUUID())) {
                MissionProgress progress = mission.getProgress(player.getUUID());
                if (progress != null && !progress.isCompleted()) {
                    updateMissionProgress(player, sect, mission, progress);
                    if (progress.canComplete() && !notifiedPlayers.contains(player.getUUID())) {
                        sendCompletionToast(player, mission);
                        notifiedPlayers.add(player.getUUID());
                    }
                }
            }
        }
    }

    private void sendCompletionToast(ServerPlayer player, SectMission mission) {
        player.displayClientMessage(
                Component.literal("§6§lMission Complete! §e" + mission.getDisplayName() + " §7- Use /sect missions to claim"),
                true
        );
        player.sendSystemMessage(Component.literal("§a✓ Mission '" + mission.getDisplayName() + "' can be completed! Use §e/sect missions§a to claim your reward."));
    }

    private void updateMissionProgress(ServerPlayer player, Sect sect, SectMission mission, MissionProgress progress) {
        if (progress.isCompleted()) {
            return;
        }
        boolean allRequirementsMet = true;
        Map<Integer, Integer> currentProgress = new HashMap<>(progress.getProgress());
        for (int i = 0; i < mission.getRequirements().size(); i++) {
            MissionRequirement requirement = mission.getRequirements().get(i);
            int requiredAmount = requirement.getCount();
            int currentAmount = currentProgress.getOrDefault(i, 0);
            switch (requirement.getType()) {
                case ITEM:
                    int itemCount = countPlayerItems(player, requirement.getTarget());
                    currentProgress.put(i, Math.min(itemCount, requiredAmount));
                    if (itemCount < requiredAmount) {
                        allRequirementsMet = false;
                    }
                    break;
                case KILL_MOB:
                    int killCount = playerKillCounts.getOrDefault(player.getUUID(), new HashMap<>())
                            .getOrDefault(requirement.getTarget(), 0);
                    currentProgress.put(i, Math.min(killCount, requiredAmount));
                    if (killCount < requiredAmount) {
                        allRequirementsMet = false;
                    }
                    break;
            }
        }
        progress.setProgress(currentProgress);
        progress.setCanComplete(allRequirementsMet);
        if (allRequirementsMet) {
            progress.setCompleted(true);
        }
        mission.acceptedBy.put(player.getUUID(), progress);
        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager != null) {
            manager.setDirty();
        }
    }

    private int countPlayerItems(ServerPlayer player, String itemId) {
        try {
            ResourceLocation itemLocation = ResourceLocation.parse(itemId);
            Item requiredItem = BuiltInRegistries.ITEM.get(itemLocation);
            if (requiredItem == null) return 0;
            Inventory inventory = player.getInventory();
            int totalCount = 0;
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty() && stack.getItem() == requiredItem) {
                    totalCount += stack.getCount();
                }
            }
            return totalCount;
        } catch (Exception e) {
            return 0;
        }
    }

    // FIXED: Per-player mission completion (no longer deletes global mission)
    public static boolean claimMissionReward(ServerPlayer player, SectMission mission) {
        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager == null) return false;
        Sect sect = manager.getPlayerSect(player.getUUID());
        if (sect == null) return false;
        MissionProgress progress = mission.getProgress(player.getUUID());
        if (progress == null || !progress.canComplete()) {
            return false;
        }
        List<ItemStack> submittedItems = removeAndCollectRequiredItems(player, mission);
        sect.addMissionSubmission(mission.getMissionId(), submittedItems);
        giveMissionRewards(player, sect, mission);
        // FIXED: Only remove player's progress, not the mission itself
        mission.completeMission(player.getUUID());
        // FIXED: Clear notification so they can be notified again for repeatable missions
        notifiedPlayers.remove(player.getUUID());
        manager.setDirty();
        player.sendSystemMessage(Component.literal("§aMission '" + mission.getDisplayName() + "' completed! Rewards claimed."));
        return true;
    }

    public static boolean claimMissionReward(ServerPlayer player, UUID missionId) {
        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager == null) return false;
        Sect sect = manager.getPlayerSect(player.getUUID());
        if (sect == null) return false;
        SectMission mission = sect.getMission(missionId);
        if (mission == null) return false;
        return claimMissionReward(player, mission);
    }

    private static void giveMissionRewards(ServerPlayer player, Sect sect, SectMission mission) {
        sect.addPlayerMerit(player.getUUID(), mission.getRewardMerit());
        ItemStack rewardItem = mission.getRewardItem();
        if (!rewardItem.isEmpty()) {
            ItemStack rewardCopy = rewardItem.copy();
            if (player.getInventory().add(rewardCopy)) {
                String itemName = rewardCopy.getHoverName().getString();
                player.sendSystemMessage(Component.literal("§aYou received §e" + mission.getRewardMerit() + " Merit Points§a and §e" +
                        itemName + "§a for completing '" + mission.getDisplayName() + "'!"));
            } else {
                player.drop(rewardCopy, false);
                String itemName = rewardCopy.getHoverName().getString();
                player.sendSystemMessage(Component.literal("§aYou received §e" + mission.getRewardMerit() + " Merit Points§a for completing '" + mission.getDisplayName() + "'!"));
                player.sendSystemMessage(Component.literal("§cYour inventory was full, so §e" + itemName + "§c was dropped on the ground!"));
            }
        } else {
            player.sendSystemMessage(Component.literal("§aYou received §e" + mission.getRewardMerit() + " Merit Points§a for completing '" + mission.getDisplayName() + "'!"));
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            playerKillCounts.remove(player.getUUID());
            notifiedPlayers.remove(player.getUUID());
        }
    }

    private static List<ItemStack> removeAndCollectRequiredItems(ServerPlayer player, SectMission mission) {
        List<ItemStack> collectedItems = new ArrayList<>();
        for (MissionRequirement requirement : mission.getRequirements()) {
            if (requirement.getType() == MissionRequirement.RequirementType.ITEM) {
                collectedItems.addAll(removeItemsFromPlayerAndCollect(player, requirement.getTarget(), requirement.getCount()));
            }
        }
        return collectedItems;
    }

    private static List<ItemStack> removeItemsFromPlayerAndCollect(ServerPlayer player, String itemId, int count) {
        List<ItemStack> collected = new ArrayList<>();
        try {
            ResourceLocation itemLocation = ResourceLocation.parse(itemId);
            Item requiredItem = BuiltInRegistries.ITEM.get(itemLocation);
            if (requiredItem == null) return collected;
            Inventory inventory = player.getInventory();
            int remaining = count;
            for (int i = 0; i < inventory.getContainerSize() && remaining > 0; i++) {
                ItemStack stack = inventory.getItem(i);
                if (!stack.isEmpty() && stack.getItem() == requiredItem) {
                    int removeAmount = Math.min(stack.getCount(), remaining);
                    ItemStack collectedStack = new ItemStack(requiredItem, removeAmount);
                    collected.add(collectedStack);
                    stack.shrink(removeAmount);
                    remaining -= removeAmount;
                    if (stack.isEmpty()) {
                        inventory.setItem(i, ItemStack.EMPTY);
                    }
                }
            }
        } catch (Exception e) {
            // Item ID was invalid
        }
        return collected;
    }
}