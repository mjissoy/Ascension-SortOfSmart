package net.thejadeproject.ascension.physiques;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.events.custom.CultivateEvent;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class PhysiqueEventListener{

    @SubscribeEvent
    public static void gatherEfficiencyMultipliers(GatherEfficiencyModifiersEvent event){
        for(ResourceLocation loc:AscensionRegistries.Physiques.PHSIQUES_REGISTRY.keySet()){
            AscensionRegistries.Physiques.PHSIQUES_REGISTRY.get(loc).onGatherEfficiencyModifiers(event);
        }
    }

}
