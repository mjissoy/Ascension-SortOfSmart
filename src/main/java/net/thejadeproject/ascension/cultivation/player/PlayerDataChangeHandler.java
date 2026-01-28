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
import net.thejadeproject.ascension.data_attachments.attachments.AscensionAttributeWrapper;
import net.thejadeproject.ascension.data_attachments.attachments.PhysiqueAttachment;
import net.thejadeproject.ascension.events.custom.PhysiqueChangeEvent;
import net.thejadeproject.ascension.events.custom.TechniqueChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.network.clientBound.SyncAttackDamageAttribute;
import net.thejadeproject.ascension.network.clientBound.SyncPathDataPayload;
import net.thejadeproject.ascension.network.clientBound.SyncPlayerPhysique;
import net.thejadeproject.ascension.progression.techniques.ITechnique;
import net.thejadeproject.ascension.progression.physiques.IPhysique;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

public class PlayerDataChangeHandler {

    public static void rebirth(Player player){
        // Get the old physique before resetting

        ResourceLocation oldPhysique = player.getData(ModAttachments.PHYSIQUE).getPhysiqueId();

        // Remove all skills
        PlayerSkillData skillData = player.getData(ModAttachments.PLAYER_SKILL_DATA);
        AscensionAttributeWrapper attributeWrapper = player.getData(ModAttachments.ATTRIBUTE_WRAPPER);
        System.out.println("attempting rebirth");
        System.out.println("skills:");
        skillData.printActiveSkills();
        // Set the new physique
        player.setData(ModAttachments.PHYSIQUE,new PhysiqueAttachment(player));


        // Fire physique change event
        NeoForge.EVENT_BUS.post(new PhysiqueChangeEvent(player, oldPhysique.toString(), player.getData(ModAttachments.PHYSIQUE).getPhysiqueId().toString()));


        //TODO update attributes

        // Reset cultivation data for all paths
        for(CultivationData.PathData pathData : player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPaths()) {

            TechniqueChangeEvent.Pre techniqueChangeEvent = new TechniqueChangeEvent.Pre(player,pathData.pathId,pathData.technique,"ascension:none",pathData.techniqueData);
            RealmChangeEvent.Pre realmChangeEvent = new RealmChangeEvent.Pre(player,pathData.pathId,pathData.majorRealm,-1,pathData.minorRealm,-1,0,null);
            NeoForge.EVENT_BUS.post(techniqueChangeEvent);
            NeoForge.EVENT_BUS.post(realmChangeEvent);

            // Send sync packet to reset client-side cultivation data
            pathData.pathProgress = 0;
            pathData.majorRealm = 0;
            pathData.minorRealm = 0;
            pathData.breakthroughData = null;
            pathData.breakingThrough = false;
            pathData.stabilityCultivationTicks = 0;
            NeoForge.EVENT_BUS.post(new RealmChangeEvent.Post(realmChangeEvent));
            pathData.technique = "ascension:none";
            pathData.techniqueData = null;
            PacketDistributor.sendToPlayer((ServerPlayer) player, SyncPathDataPayload.fromPathData(pathData));
            NeoForge.EVENT_BUS.post(new TechniqueChangeEvent.Post(techniqueChangeEvent));


        }
        System.out.println("REMOVING EXTRA MODIFIERS");
        skillData.removeAllNonePermanentSkills();
        attributeWrapper.removeAllNonPermanentModifiers();
    }


     public static void changePhysique(Player player,ResourceLocation targetPhysique){
        System.out.println("changing physique");
        ResourceLocation oldPhysique = player.getData(ModAttachments.PHYSIQUE).getPhysiqueId();
        IPhysique newPhysique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(targetPhysique);
        player.getData(ModAttachments.PHYSIQUE).setPhysique(targetPhysique);
        NeoForge.EVENT_BUS.post(new PhysiqueChangeEvent(player, oldPhysique.toString(), targetPhysique.toString()));

        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncPlayerPhysique(targetPhysique.toString()));
        //TODO use different function for removing physique
        // Send feedback message to player
        if (player instanceof ServerPlayer serverPlayer) {
            Component physiqueName = Component.literal("None");
            if (!targetPhysique.equals("ascension:none") && newPhysique != null) {
                physiqueName = newPhysique.getDisplayTitle();
            }
            serverPlayer.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("Your cultivation has been reset! New physique: ")
                            .append(physiqueName),
                    true);
        }
    }

    // Original method (for RebirthPill) - resets to empty_vessel
    public static void resetData(Player player) {
        resetData(player, "ascension:empty_vessel");
    }
    //TODO redo
    // New overloaded method that accepts target physique
    //uses rebirth instead of the force remove
    public static void resetData(Player player, String targetPhysiqueId) {
        // Get the old physique before resetting
        System.out.println("resetin");
        ResourceLocation oldPhysique = player.getData(ModAttachments.PHYSIQUE).getPhysiqueId();


        // Set the new physique
        player.getData(ModAttachments.PHYSIQUE).setPhysique(targetPhysiqueId);




        // Reset cultivation data for all paths
        for(CultivationData.PathData pathData : player.getData(ModAttachments.PLAYER_DATA).getCultivationData().getPaths()) {

            TechniqueChangeEvent.Pre techniqueChangeEvent = new TechniqueChangeEvent.Pre(player,pathData.pathId,pathData.technique,"ascension:none",pathData.techniqueData);
            RealmChangeEvent.Pre realmChangeEvent = new RealmChangeEvent.Pre(player,pathData.pathId,pathData.majorRealm,-1,pathData.minorRealm,-1,0,null);
            NeoForge.EVENT_BUS.post(techniqueChangeEvent);
            NeoForge.EVENT_BUS.post(realmChangeEvent);

            // Send sync packet to reset client-side cultivation data
            pathData.pathProgress = 0;
            pathData.majorRealm = 0;
            pathData.minorRealm = 0;
            pathData.technique = "ascension:none";
            pathData.techniqueData = null;
            pathData.breakthroughData = null;
            pathData.breakingThrough = false;
            pathData.stabilityCultivationTicks = 0;

            PacketDistributor.sendToPlayer((ServerPlayer) player, SyncPathDataPayload.fromPathData(pathData));
            NeoForge.EVENT_BUS.post(new TechniqueChangeEvent.Post(techniqueChangeEvent));
            NeoForge.EVENT_BUS.post(new RealmChangeEvent.Post(realmChangeEvent));
        }
        // Sync attack damage attribute
        PacketDistributor.sendToPlayer((ServerPlayer) player,
                new SyncAttackDamageAttribute(player.getAttributeValue(Attributes.ATTACK_DAMAGE)));

        // Trigger physique acquisition for the new physique
        ResourceLocation physiqueResource = ResourceLocation.bySeparator(targetPhysiqueId, ':');
        IPhysique newPhysique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physiqueResource);

        // Fire physique change event
        NeoForge.EVENT_BUS.post(new PhysiqueChangeEvent(player, oldPhysique.toString(), targetPhysiqueId));


        // Sync the new physique to client
        PacketDistributor.sendToPlayer((ServerPlayer) player, new SyncPlayerPhysique(targetPhysiqueId));
        //TODO use different function for removing physique
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
    //TODO redo
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
        RealmChangeEvent realmChangeEvent = new RealmChangeEvent.Pre(
                player, path, majorRealm, 0, minorRealm, 0, 0, null);

        // Create technique change event
        TechniqueChangeEvent techniqueChangeEvent = new TechniqueChangeEvent.Pre(
                player, path, pathData.technique, "ascension:none",pathData.techniqueData);

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
            RealmChangeEvent realmChangeEvent = new RealmChangeEvent.Post(
                    player, pathId, oldMajor, 0, oldMinor, 0, -oldMajor, null);
            NeoForge.EVENT_BUS.post(realmChangeEvent);

            // Sync to client
            PacketDistributor.sendToPlayer((ServerPlayer) player,
                    new SyncPathDataPayload(pathId, 0, 0, 0, "ascension:none", 0));
        }
    }
}