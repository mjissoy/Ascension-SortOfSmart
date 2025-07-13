package net.thejadeproject.ascension.physiques;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.custom.CultivateEvent;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
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

}
