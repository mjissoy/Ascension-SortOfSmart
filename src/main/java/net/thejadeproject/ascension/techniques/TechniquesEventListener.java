package net.thejadeproject.ascension.techniques;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.PlayerData;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.MinorRealmChangeEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;
@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class TechniquesEventListener {
    @SubscribeEvent
    public static void gatherEfficiencyMultipliers(GatherEfficiencyModifiersEvent event){
        //TODO
        //check if player has technique
        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        if(!data.getPathData(event.pathID).technique.equals("ascension:none")){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(data.getPathData(event.pathID).technique,':')).onGatherEfficiencyModifiers(event);
        }
    }

    @SubscribeEvent
    public static void onMajorRealmChange(MajorRealmChangeEvent event){
        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        if(!data.getPathData(event.pathId).technique.equals("ascension:none")){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(data.getPathData(event.pathId).technique,':')).onMajorRealmIncrease(event);
        }
    }
    @SubscribeEvent
    public static void onMinorRealmChange(MinorRealmChangeEvent event){
        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        if(!data.getPathData(event.pathId).technique.equals("ascension:none")){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(data.getPathData(event.pathId).technique,':')).onMinorRealmIncrease(event);
        }
    }
}
