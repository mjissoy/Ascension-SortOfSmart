package net.thejadeproject.ascension.progression.techniques;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.thejadeproject.ascension.AscensionCraft;
import net.thejadeproject.ascension.cultivation.player.data_attachements.CultivationData;
import net.thejadeproject.ascension.cultivation.player.data_attachements.PlayerData;
import net.thejadeproject.ascension.events.custom.GatherEfficiencyModifiersEvent;
import net.thejadeproject.ascension.events.custom.cultivation.MajorRealmChangeEvent;
import net.thejadeproject.ascension.events.custom.cultivation.MinorRealmChangeEvent;
import net.thejadeproject.ascension.registries.AscensionRegistries;
import net.thejadeproject.ascension.util.ModAttachments;

import java.util.List;

@EventBusSubscriber(modid = AscensionCraft.MOD_ID)
public class TechniquesEventListener {
    @SubscribeEvent
    public static void gatherEfficiencyMultipliers(GatherEfficiencyModifiersEvent event){
        //TODO
        //check if player has technique
        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        for(CultivationData.PathData pathData : data.getCultivationData().getPaths()){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(pathData.technique,':')).onGatherEfficiencyModifiers(event);

        }

    }

    @SubscribeEvent
    public static void onMajorRealmChange(MajorRealmChangeEvent event){
        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        if(!data.getCultivationData().getPathData(event.pathId).technique.equals("ascension:none")){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(data.getCultivationData().getPathData(event.pathId).technique,':')).onMajorRealmIncrease(event);
        }
    }
    @SubscribeEvent
    public static void onMinorRealmChange(MinorRealmChangeEvent event){
        PlayerData data = event.player.getData(ModAttachments.PLAYER_DATA);
        if(!data.getCultivationData().getPathData(event.pathId).technique.equals("ascension:none")){
            AscensionRegistries.Techniques.TECHNIQUES_REGISTRY.get(ResourceLocation.bySeparator(data.getCultivationData().getPathData(event.pathId).technique,':')).onMinorRealmIncrease(event);
        }
    }
}
