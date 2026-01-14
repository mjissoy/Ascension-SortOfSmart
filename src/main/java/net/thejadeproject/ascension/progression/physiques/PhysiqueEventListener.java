package net.thejadeproject.ascension.progression.physiques;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.events.custom.*;

import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.events.custom.skills.PlayerSkillRemoveEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.data_attachments.ModAttachments;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class PhysiqueEventListener{

    @SubscribeEvent
    public static void gatherEfficiencyMultipliers(GatherEfficiencyModifiersEvent event){
        if(event.player.hasData(ModAttachments.PHYSIQUE)){
            IPhysique physique = event.player.getData(ModAttachments.PHYSIQUE).getPhysique();
            if(physique != null) physique.onGatherEfficiencyModifiers(event);
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRealmChange(RealmChangeEvent.Post event){
        if(event.player.hasData(ModAttachments.PHYSIQUE)){
            IPhysique physique = event.player.getData(ModAttachments.PHYSIQUE).getPhysique();
            if(physique != null)  physique.onRealmChangeEvent(event);
        }
    }

    @SubscribeEvent
    public static void onPhysiqueChange(PhysiqueChangeEvent event){
        if(!event.player.hasData(ModAttachments.PHYSIQUE)) return;
        String physique_id = event.player.getData(ModAttachments.PHYSIQUE).getPhysiqueId().toString();
        if(event.newPhysique.equals(physique_id)){
            IPhysique oldPhysique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(ResourceLocation.bySeparator(event.oldPhysique,':'));
            IPhysique newPhysique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(ResourceLocation.bySeparator(event.newPhysique,':'));
            if(oldPhysique != null) oldPhysique.onRemovePhysique(event.player);
            if(newPhysique != null) newPhysique.onPhysiqueAcquisition(event.player);
        }
    }

    @SubscribeEvent
    public static void onSkillRemove(PlayerSkillRemoveEvent event){
        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        ResourceLocation physiqueId = event.player.getData(ModAttachments.PHYSIQUE).getPhysiqueId();

        if(AscensionRegistries.Physiques.PHSIQUES_REGISTRY.containsKey(physiqueId)){
            AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(physiqueId).onSkillRemoveEvent(event);
        }

    }
}
