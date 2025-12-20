package net.thejadeproject.ascension.cultivation.player;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerSkillData;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.events.custom.TechniqueChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.network.clientBound.SyncPlayerPhysique;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.progression.physiques.IPhysique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

public class PlayerDataChangeHandler {

    // Original method (for RebirthPill) - resets to empty_vessel
    public static void resetData(Player player) {
        resetData(player, "ascension:empty_vessel");
    }

    // New overloaded method that accepts target physique
    public static void resetData(Player player, String targetPhysiqueId) {
        // Get the old physique before resetting
        String oldPhysique = player.getData(ModAttachments.PHYSIQUE);

        // Remove all skills
        PlayerSkillData skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        skillData.removeAllSkills();

        // Set the new physique
        player.setData(ModAttachments.PHYSIQUE, targetPhysiqueId);

        // Fire physique change event
        NeoForge.EVENT_BUS.post(new PhysiqueChangeEvent(player, oldPhysique, targetPhysiqueId));

        // Reset skill data
        player.setData(ModAttachments.PLAYER_SKILL_DATA, new PlayerSkillData(player));

        // Reset player attributes to default values
        player.setData(ModAttachments.MOVEMENT_SPEED, 0.1);
        player.setData(ModAttachments.ATTACK_DAMAGE, 1.0);
        player.setData(ModAttachments.JUMP_HEIGHT, 0.42);
        player.setData(ModAttachments.MAX_HEALTH, 20.0);
        player.setData(ModAttachments.SAFE_FALL_DISTANCE, 3.0);
        player.setData(ModAttachments.ARMOR, 0.0);
        player.setData(ModAttachments.OXYGEN_BONUS, 0.0);
        player.setData(ModAttachments.WATER_MOVEMENT_EFFICIENCY, 0.0);

        // Reset Minecraft attributes to default
        AttributeSupplier playerSupplier = Player.createAttributes().build();
        for(Holder<Attribute> attributeHolder : player.getAttributes().attributes.keySet()) {
            if(playerSupplier.hasAttribute(attributeHolder)) {
                AttributeInstance instance = player.getAttributes().attributes.get(attributeHolder);
                instance.setBaseValue(playerSupplier.getBaseValue(attributeHolder));
                instance.removeModifiers();
            } else {
                AttributeInstance instance = player.getAttributes().attributes.get(attributeHolder);
                instance.setBaseValue(attributeHolder.value().getDefaultValue());
                instance.removeModifiers();
            }
        }

        // Reset cultivation data for all paths
        for(CultivationData.PathData pathData : player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPaths()) {
            // Send sync packet to reset client-side cultivation data
            PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncPathDataPayload(
                    pathData.pathId,
                    0, // major realm
                    0, // minor realm
                    0, // path progress
                    "ascension:none", // technique
                    0 // stability cultivation ticks
            ));
        }

        // Create new player data
        player.setData(ModAttachments.PLAYER_DATA, new PlayerData(player));

        // Sync attack damage attribute
        PacketDistributor.sendToPlayer((ServerPlayer) player,
                new SyncAttackDamageAttribute(player.getAttributeValue(Attributes.ATTACK_DAMAGE)));

        // Trigger physique acquisition for the new physique
        ResourceLocation physiqueResource = ResourceLocation.bySeparator(targetPhysiqueId, ':');
        IPhysique newPhysique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physiqueResource);
        if (newPhysique != null) {
            newPhysique.onPhysiqueAcquisition(player);
        }

        // Sync the new physique to client
        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncPlayerPhysique(targetPhysiqueId));

        // Send feedback message to player
        if (player instanceof ServerPlayer serverPlayer) {
            Component physiqueName = Component.literal("Empty Vessel");
            if (!targetPhysiqueId.equals("ascension:empty_vessel") && newPhysique != null) {
                physiqueName = newPhysique.getDisplayTitle();
            }
            serverPlayer.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("Your cultivation has been reset! New physique: ")
                            .append(physiqueName),
            true);
        }
    }

    // Removes technique from a specific path without resetting everything
    public static void removeTechnique(Player player, String path) {
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPathData(path);
        if (pathData == null) return;

        ITechnique technique = AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(
                ResourceLocation.bySeparator(pathData.technique, ':'));
        if (technique == null) return;

        int majorRealm = pathData.majorRealm;
        int minorRealm = pathData.minorRealm;

        // Create realm change event to trigger effects
        RealmChangeEvent realmChangeEvent = new RealmChangeEvent(
                player, path, majorRealm, 0, minorRealm, 0, 0, null);

        // Create technique change event
        TechniqueChangeEvent techniqueChangeEvent = new TechniqueChangeEvent(
                player, path, pathData.technique, "ascension:none");

        // Fire events
        NeoForge.EVENT_BUS.post(realmChangeEvent);
        NeoForge.EVENT_BUS.post(techniqueChangeEvent);

        // Reset path data
        pathData.pathProgress = 0;
        pathData.technique = "ascension:none";
        pathData.stabilityCultivationTicks = 0;
        pathData.breakingThrough = false;
        pathData.cultivating = false;
        pathData.majorRealm = 0;
        pathData.minorRealm = 0;
        pathData.breakthroughData = null;

        // Sync to client
        PacketDistributor.sendToPlayer((ServerPlayer) player,
                SyncPathDataPayload.fromPathData(pathData));

        // Send feedback
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    "Technique removed from " + path + " path."
            ));
        }
    }

    // Optional: Method to reset only specific paths while keeping physique
    public static void resetPath(Player player, String pathId) {
        CultivationData.PathData pathData = player.getData(ModAttachments.PLAYER_DATA)
                .getCultivationData().getPathData(pathId);

        if (pathData != null) {
            int oldMajor = pathData.majorRealm;
            int oldMinor = pathData.minorRealm;

            // Reset the path
            pathData.majorRealm = 0;
            pathData.minorRealm = 0;
            pathData.pathProgress = 0;
            pathData.technique = "ascension:none";
            pathData.stabilityCultivationTicks = 0;
            pathData.breakingThrough = false;
            pathData.cultivating = false;
            pathData.breakthroughData = null;

            // Create and post realm change event for negative change
            RealmChangeEvent realmChangeEvent = new RealmChangeEvent(
                    player, pathId, oldMajor, 0, oldMinor, 0, -oldMajor, null);
            NeoForge.EVENT_BUS.post(realmChangeEvent);

            // Sync to client
            PacketDistributor.sendToPlayer((ServerPlayer) player,
                    new SyncPathDataPayload(pathId, 0, 0, 0, "ascension:none", 0));
        }
    }
}