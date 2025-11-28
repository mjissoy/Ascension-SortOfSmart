package net.thejadeproject.ascension.progression.physiques;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.custom.*;

import net.thejadeproject.ascension.events.custom.cultivation.RealmChangeEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class PhysiqueEventListener{

    @SubscribeEvent
    public static void gatherEfficiencyMultipliers(GatherEfficiencyModifiersEvent event){
        if(event.player.hasData(ModAttachments.PHYSIQUE)){
            String physique = event.player.getData(ModAttachments.PHYSIQUE);
            AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(
                ResourceLocation.fromNamespaceAndPath(physique.split(":")[0],physique.split(":")[1])
            ).onGatherEfficiencyModifiers(event);
        }
    }
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRealmChange(RealmChangeEvent event){
        if(event.player.hasData(ModAttachments.PHYSIQUE)){
            String physique_id = event.player.getData(ModAttachments.PHYSIQUE);
            IPhysique physique =  AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(
                    ResourceLocation.fromNamespaceAndPath(physique_id.split(":")[0],physique_id.split(":")[1])
            );
            physique.onRealmChangeEvent(event);
        }
    }

    @SubscribeEvent
    public static void onPhysiqueChange(PhysiqueChangeEvent event){
        if(!event.player.hasData(ModAttachments.PHYSIQUE)) return;
        String physique_id = event.player.getData(ModAttachments.PHYSIQUE);
        if(event.newPhysique.equals(physique_id)){
            IPhysique oldPhysique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(ResourceLocation.bySeparator(event.oldPhysique,':'));
            IPhysique newPhysique = AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(ResourceLocation.bySeparator(event.newPhysique,':'));
            if(oldPhysique != null) oldPhysique.onRemovePhysique(event.player);
            if(newPhysique != null) newPhysique.onPhysiqueAcquisition(event.player);
        }
    }
}
