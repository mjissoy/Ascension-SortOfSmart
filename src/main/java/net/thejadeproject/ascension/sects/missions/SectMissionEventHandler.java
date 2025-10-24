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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.sects.Sect;
import net.thejadeproject.ascension.sects.SectManager;

import java.util.*;

public class SectMissionEventHandler {

    // Track mob kills per player
    private final Map<UUID, Map<String, Integer>> playerKillCounts = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        // Check for completed missions every tick
        if (event.getEntity() instanceof ServerPlayer player) {
            checkMissionCompletions(player);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        // Track mob kills for mission requirements
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (entity instanceof Mob && source.getEntity() instanceof ServerPlayer player) {
            String mobName = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
            trackMobKill(player, mobName);
        }
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        // Track item crafting for missions
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack crafted = event.getCrafting();
            String itemName = BuiltInRegistries.ITEM.getKey(crafted.getItem()).toString();
            // This could be used for "craft X items" missions
        }
    }

    private void trackMobKill(ServerPlayer player, String mobName) {
        UUID playerId = player.getUUID();
        playerKillCounts.putIfAbsent(playerId, new HashMap<>());

        Map<String, Integer> kills = playerKillCounts.get(playerId);
        kills.put(mobName, kills.getOrDefault(mobName, 0) + 1);

        // Update missions that require this mob kill
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

        // Check all accepted missions for completion
        for (SectMission mission : sect.getMissions()) {
            if (mission.isAcceptedBy(player.getUUID())) {
                MissionProgress progress = mission.getProgress(player.getUUID());
                if (progress != null && !progress.isCompleted()) {
                    updateMissionProgress(player, sect, mission, progress);
                }
            }
        }
    }

    private void updateMissionProgress(ServerPlayer player, Sect sect, SectMission mission, MissionProgress progress) {
        boolean allRequirementsMet = true;
        Map<Integer, Integer> currentProgress = new HashMap<>(progress.getProgress());

        for (int i = 0; i < mission.getRequirements().size(); i++) {
            MissionRequirement requirement = mission.getRequirements().get(i);
            int requiredAmount = requirement.getCount();
            int currentAmount = currentProgress.getOrDefault(i, 0);

            switch (requirement.getType()) {
                case ITEM:
                    int itemCount = countPlayerItems(player, requirement.getTarget());
                    // Only update if we found more items
                    if (itemCount > currentAmount) {
                        currentProgress.put(i, Math.min(itemCount, requiredAmount));
                    }
                    if (itemCount < requiredAmount) {
                        allRequirementsMet = false;
                    }
                    break;

                case KILL_MOB:
                    int killCount = playerKillCounts.getOrDefault(player.getUUID(), new HashMap<>())
                            .getOrDefault(requirement.getTarget(), 0);
                    // Only update if we have more kills
                    if (killCount > currentAmount) {
                        currentProgress.put(i, Math.min(killCount, requiredAmount));
                    }
                    if (killCount < requiredAmount) {
                        allRequirementsMet = false;
                    }
                    break;
            }
        }

        // Only update if progress actually changed
        if (!currentProgress.equals(progress.getProgress())) {
            progress.setProgress(currentProgress);

            boolean wasCompleted = progress.isCompleted();
            progress.setCompleted(allRequirementsMet);

            // Save changes
            mission.acceptedBy.put(player.getUUID(), progress);
            SectManager manager = AscensionCraft.getSectManager(player.getServer());
            if (manager != null) {
                manager.setDirty();
            }
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

    // Method to claim mission rewards
    public static boolean claimMissionReward(ServerPlayer player, SectMission mission) {
        SectManager manager = AscensionCraft.getSectManager(player.getServer());
        if (manager == null) return false;

        Sect sect = manager.getPlayerSect(player.getUUID());
        if (sect == null) return false;

        MissionProgress progress = mission.getProgress(player.getUUID());
        if (progress == null || !progress.isCompleted()) {
            return false;
        }

        // Remove required items from player and collect them
        List<ItemStack> submittedItems = removeAndCollectRequiredItems(player, mission);

        // Store the submitted items in the sect
        sect.addMissionSubmission(mission.getMissionId(), submittedItems);

        // Give rewards (merit points and item rewards)
        giveMissionRewards(player, sect, mission);

        // Update mission state
        if (mission.isRepeatable()) {
            // Reset progress for repeatable missions
            MissionProgress newProgress = new MissionProgress();
            mission.acceptedBy.put(player.getUUID(), newProgress);
        } else {
            // Remove from accepted missions for non-repeatable
            mission.completeMission(player.getUUID());
        }

        manager.setDirty();
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

    private static boolean hasRequiredItems(ServerPlayer player, SectMission mission) {
        for (MissionRequirement requirement : mission.getRequirements()) {
            if (requirement.getType() == MissionRequirement.RequirementType.ITEM) {
                int itemCount = countPlayerItemsStatic(player, requirement.getTarget());
                if (itemCount < requirement.getCount()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int countPlayerItemsStatic(ServerPlayer player, String itemId) {
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

    private static void giveMissionRewards(ServerPlayer player, Sect sect, SectMission mission) {
        // Give merit points
        sect.addPlayerMerit(player.getUUID(), mission.getRewardMerit());

        // Give item reward if specified
        if (!mission.getRewardItem().isEmpty()) {
            ItemStack rewardCopy = mission.getRewardItem().copy();
            if (!player.getInventory().add(rewardCopy)) {
                // Drop if inventory is full
                player.drop(rewardCopy, false);
            }
        }
    }

    // Reset kill counts when player logs out
    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            playerKillCounts.remove(player.getUUID());
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